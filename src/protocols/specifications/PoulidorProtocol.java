package protocols.specifications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import utils.Combinatory;

public class PoulidorProtocol extends DBProtocol{

	
	private static final long serialVersionUID = -3040121041933506115L;
	private int sizeOfNonces;
	
	public PoulidorProtocol(int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
	}
	
	public PoulidorProtocol(){
		this(SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "Poulidor";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		/*BigDecimal half = new BigDecimal(0.5);
		BigDecimal result = half.pow(n);
		for (int i = 1; i <= n; i++){
			result = result.add((half.pow(i)).multiply(maxProb2(i, n)));
		}
		return result;*/
		//if (n == 0) return BigDecimal.ONE;
		return new BigDecimal(""+mafiaFraudFromDisk2(n));
	}
	@Override
	public int getYearOfPublication() {
		return 2010;
	}

	private BigDecimal maxProb2(int t, int n) {
		BigDecimal result = new BigDecimal(1);
		//System.out.println("m = "+m);
		for (int i = t; i <= n; i++){
			//System.out.println("best = "+ index+" prob = "+max);
			result = result.multiply(prob(i, i, t));
		}
		return result;
	}
	private BigDecimal prob(int i, int j, int t) {
		if (i < t && i == j) return new BigDecimal(1);
		if (i < t && i != j) return new BigDecimal(0.5);
		if (i >= t && j < t) return new BigDecimal(0.5);
		i = i-t+1;
		j = j-t+1;
		BigDecimal half = new BigDecimal(0.5);
		BigInteger sum = new BigInteger(0+"");
		int max = (i > j)?i:j;
		for (int k = 1; k <= 2*max+2; k++){
			sum = sum.add(walks(1, k, i-1).multiply(walks(2, k, j-1)));
			sum = sum.add(walks(2, k, i-1).multiply(walks(1, k, j-1)));
		}
		BigDecimal result = half.pow(i+j).multiply(new BigDecimal(sum.doubleValue()));
		result = result.add(half);
		return result;
	}

	private BigInteger walks(int i, int k, int j) {
		if (j == 0){
			if (i == k) return new BigInteger(1+"");
			else return new BigInteger(0+"");
		}
		if (2*j < k - i) return new BigInteger(0+"");
		if (k < i+j) return new BigInteger(0+"");
		return Combinatory.comb(j, k-i-j);
	}

	
	private BigDecimal computeProb2(int j, int i){
		BigInteger den = (new BigInteger(""+2)).pow(i+j);
		BigInteger t1, t2;
		BigDecimal result = new BigDecimal(0+"");
		int max = (i > j)?i:j;
		for (int k = max; k <= 2*max; k++){
			if (k-i > i) break;
			if (k-j > j) break;
			t1 = Combinatory.comb(i, k-i);
			t2 = Combinatory.comb(j, k-j);
			result = result.add(new BigDecimal((t1.multiply(t2))));
		}
		result = result.divide(new BigDecimal(den));
		return result;
	}

	@Override
	public DBProtocol getInstance() {
		return new PoulidorProtocol();
	}
	
	@Override
	public BigDecimal getDistanceFraudProbability() {
		/*if (n == 0) return BigDecimal.ONE;
		if (n == 1) return new BigDecimal("0.75");
		double p  = 1;
		for (int i = 1; i <= n; i++){
			p = p*(0.5 + computeProb2(i, i).doubleValue()/2);
		}
		BigDecimal half = new BigDecimal(0.5d);
		BigDecimal four = new BigDecimal(4d);
		BigDecimal sqrt = half.pow(2*n).add(((four).multiply(half.pow(n))).negate()).add(four.multiply(new BigDecimal(p)));
		BigDecimal result = half.pow(n);
		return new BigDecimal(result.doubleValue() + Math.sqrt(sqrt.doubleValue())/2);*/
		//if (n == 0) return BigDecimal.ONE;
		return new BigDecimal(distanceFraudFromDisk2(n));
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
		return 1;
	}

	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new PoulidorProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "Poulidor-{"+n+"}";
	}

	@Override
	public boolean lackSecurityProof() {
		return true;
	}

	@Override
	public int getSizeOfTheChannel() {
		return 1;
	}

	
	public static void generateDataFile() throws IOException{
		String newLine = System.getProperty("line.separator");
		PoulidorProtocol p = new PoulidorProtocol();
		FileWriter writerMafia = new FileWriter(new File(p.getMafiaFileNameOfValues()));
		FileWriter writerDistance = new FileWriter(new File(p.getDistanceFileNameOfValues()));		
		for (int i = 0; i <= 257; i++){
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
	
	
	public String getMafiaFileNameOfValues(){
		return "mafia_"+getAcronym()+"_values.DAT";
	}
	public String getDistanceFileNameOfValues(){
		return "distance_"+getAcronym()+"_values.DAT";
	}

	public static void main(String[] args) {
		PoulidorProtocol p = new PoulidorProtocol();
		p.setNumberOfRounds(256);
		BigDecimal mafia = p.getMafiaFraudProbability();
		System.out.println("n = "+256+" Mafia = "+mafia.doubleValue());
		BigDecimal distance = p.getDistanceFraudProbability();
		System.out.println("n = "+256+" Distance = "+distance.doubleValue());
		//generateDataFile();
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return sizeOfNonces+n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 4*n;
	}

	@Override
	public int getNumberOfNoncesGenerated() {
		return 1;
	}

	@Override
	public int getBitsGenerated() {
		return 0;
	}

	private double[] mafia2;

	private double mafiaFraudFromDisk2(int n){
		if (mafia2 == null) {
			try {
				fillMafia2(257);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return mafia2[n];
	}

	/*La informacion sobre el distance esta en un fichero de nombre "d_FDA-"+n*/
	private void fillMafia2(int n) throws IOException {
		String tmp;
		String[] split;
		int r;
		mafia2 = new double[n+1];
		for (int i = 1; i <= n; i++){
			File f = new File(this.getMafiaFileNameOfValues());
			BufferedReader reader = new BufferedReader(new FileReader(f));
			while (reader.ready()){
				tmp = reader.readLine();
				split = tmp.split(" ");
				mafia2[Integer.parseInt(split[0])] = Double.parseDouble(split[1]);
			}
			reader.close();
		}
	}	

	private double[] distances2;

	private double distanceFraudFromDisk2(int n){
		if (distances2 == null) {
			try {
				fillDistance2(257);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return distances2[n];
	}

	/*La informacion sobre el distance esta en un fichero de nombre "d_FDA-"+n*/
	private void fillDistance2(int n) throws IOException {
		String tmp;
		String[] split;
		int r;
		distances2 = new double[n+1];
		for (int i = 1; i <= n; i++){
			File f = new File(this.getDistanceFileNameOfValues());
			BufferedReader reader = new BufferedReader(new FileReader(f));
			while (reader.ready()){
				tmp = reader.readLine();
				split = tmp.split(" ");
				distances2[Integer.parseInt(split[0])] = Double.parseDouble(split[1]);
			}
			reader.close();
		}
	}	


}
