package protocols.specifications;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/*
 * Protocol proposed by Rolando y Reynaldo which is asymptotically optimal in terms
 * of mafia fraud and distance fraud. The description of the protocol can be found 
 * in the paper "An asymptotically optimal lookup-based distance bounding protocol'
 */
public class OptimalLookupBasedProtocol extends DBProtocol{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int sizeOfNonces;
	protected int depth;
	
	public static void main(String[] args) {
		try {
			generateDataFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void generateDataFile() throws IOException{
		String newLine = System.getProperty("line.separator");
		OptimalLookupBasedProtocol p = new OptimalLookupBasedProtocol();
		FileWriter writerMafia = new FileWriter(new File(p.getMafiaFileNameOfValues()));
		FileWriter writerDistance = new FileWriter(new File(p.getDistanceFileNameOfValues()));		
		for (int i = 1; i <= 128; i++){
			p.setNumberOfRounds(i);
			BigDecimal mafia = p.getMafiaFraudProbability();
			BigDecimal distance = p.getDistanceFraudProbability();
			writerMafia.write(i+" "+mafia.doubleValue()+newLine);
			writerDistance.write(i+" "+distance.doubleValue()+newLine);
			System.out.println("n = "+i+" Mafia = "+mafia.doubleValue());
			System.out.println("n = "+i+" Distance = "+distance.doubleValue());
			writerMafia.flush();
			writerDistance.flush();
		}
		writerMafia.close();
		writerDistance.close();
	}
	
	public OptimalLookupBasedProtocol(int depth, int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
		this.depth = depth;
	}

	public OptimalLookupBasedProtocol(int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
		this.depth = 1;
	}

	public OptimalLookupBasedProtocol(){
		this(1, SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "optimal-lookup-protocol";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		return computeMyFar(n);

	}

	@Override
	public DBProtocol getInstance() {
		return new OptimalLookupBasedProtocol(depth, SIZE_OF_NONCES);
	}

	@Override
	public int getYearOfPublication() {
		return 2023;
	}

	public BigDecimal computeMyFar(int k) {
		BigDecimal a = new BigDecimal(0.5d);
		a = a.pow(k);
		return a.multiply(new BigDecimal((double)k+1));
	}
	
	private BigDecimal distanceFraudUpperBound(int n){
		BigDecimal a = new BigDecimal(0.5d);
		return a.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		BigDecimal a = new BigDecimal(0.5d);
		return a.pow(n);
	}

	@Override
	public BigDecimal getTerroristFraudProbability() {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return false;
	}


	@Override
	public int getCryptoCalls() {
		return (int)Math.log(n);
	}

	@Override
	public DBProtocol[] getInstances() {
		int x = (int)(Math.log(MAX_N)/Math.log(2))+1;
		OptimalLookupBasedProtocol[] result = new OptimalLookupBasedProtocol[x];
		int n = 1;
		int i = 0;
		while (n <= MAX_N) {
			result[i] = new OptimalLookupBasedProtocol();
			result[i].setNumberOfRounds(n);
			n *= 2;
			i++;
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "optimal-lookup-{"+n+", "+depth+"}";
	}
	

	@Override
	public boolean lackSecurityProof() {
		return false;
	}

	@Override
	public int getSizeOfTheChannel() {
		return 1;
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return sizeOfNonces+n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		BigInteger tmp = new BigInteger(2+"");
		int d = depth;
		tmp = tmp.pow(d+1);
		BigInteger minusTwo = new BigInteger(2+"");
		minusTwo = minusTwo.negate();
		tmp = tmp.add(minusTwo);
		tmp = tmp.multiply(new BigInteger((n/d)+""));
		//dealing with numerical errors
		if (tmp.bitLength() > 30) return Integer.MAX_VALUE/2;
		return tmp.intValue();
	}
	

	@Override
	public int getNumberOfNoncesGenerated() {
		return 1;
	}

	@Override
	public int getBitsGenerated() {
		return 0;
	}

	@Override
	public void setNumberOfRounds(int n){
		this.n = n;
		depth = (int)(Math.log(n)/Math.log(2));
	}
}
