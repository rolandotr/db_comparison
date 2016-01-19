package protocols.specifications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

/*Trujillo- May 23, 2014
 * Protocol published in the paper "Distance bounding facing both mafia and distance frauds"*/
public class TMAProtocol extends DBProtocol{

	private static final long serialVersionUID = -8092121387109907430L;

	private int sizeOfNonces = SIZE_OF_NONCES;
	
	@Override
	public String getAcronym() {
		return "TMA";
	}

	BigDecimal[] mafiaFraud = new BigDecimal[257];

	@Override
	public BigDecimal getMafiaFraudProbability() {
		/*if (mafiaFraud[n] != null) return mafiaFraud[n];
		if (n == 0) return BigDecimal.ONE;
		BigDecimal tmp1 = computeMGivenDifferent(n);
		BigDecimal tmp2 = new BigDecimal(0.5);
		tmp2 = tmp2.pow(n);
		BigDecimal tmp3 = new BigDecimal(1);
		tmp3 = tmp3.add(tmp2.negate());
		tmp3 = tmp3.multiply(tmp1);
		tmp3 = tmp3.add(tmp2);
		mafiaFraud[n] = tmp3;
		return mafiaFraud[n];*/
		if (n == 0) return BigDecimal.ONE;
		return mafiaFraudFromDisk2(n);
	}

	public BigDecimal getMafiaFraudProbabilityExpensiveMethod() {
		if (n == 0) return BigDecimal.ONE;
		BigDecimal tmp1 = computeMGivenDifferent(n);
		BigDecimal tmp2 = new BigDecimal(0.5);
		tmp2 = tmp2.pow(n);
		BigDecimal tmp3 = new BigDecimal(1);
		tmp3 = tmp3.add(tmp2.negate());
		tmp3 = tmp3.multiply(tmp1);
		tmp3 = tmp3.add(tmp2);
		return tmp3;
	}

	@Override
	public DBProtocol getInstance() {
		return new TMAProtocol();
	}
	BigDecimal[] MGivenDifferent = new BigDecimal[257];

	private BigDecimal computeMGivenDifferent(int i) {
		if (MGivenDifferent[i] != null) return MGivenDifferent[i];
		if (i == 0) return new BigDecimal(1);
		BigDecimal tmp1 = computeMGivenDifferentGivenM(i);
		BigDecimal tmp2 = new BigDecimal(1d/(Math.pow(2, i)-1));
		BigDecimal tmp3 = computeMGivenDifferent(i-1);
		BigDecimal tmp4 = new BigDecimal(1);
		tmp4 = tmp4.add(tmp2.negate());
		tmp4 = tmp4.multiply(tmp3);
		tmp4 = tmp4.add(tmp2);
		tmp4 = tmp4.multiply(tmp1);
		MGivenDifferent[i] = tmp4;
		return MGivenDifferent[i];
	}
	
	BigDecimal[] MGivenDifferentGivenM = new BigDecimal[257];

	private BigDecimal computeMGivenDifferentGivenM(int i) {
		if (MGivenDifferentGivenM[i] != null) return MGivenDifferentGivenM[i];
		if (i == 0) return new BigDecimal(0);
		double tmp1 = Math.pow(0.5, i);
		tmp1 = 1 - tmp1;
		tmp1 = 2*tmp1;
		tmp1 = 1d/tmp1;
		tmp1 = 1-tmp1;
		BigDecimal tmp2 = new BigDecimal(tmp1);
		BigDecimal tmp3 = new BigDecimal(1);
		tmp3 = tmp3.add(tmp2.negate());
		BigDecimal tmp4 = computeMGivenDifferentGivenMGivenEqual(i);
		BigDecimal tmp5 = computeMGivenDifferentGivenMGivenDifferent(i);
		tmp4 = tmp4.multiply(tmp2);
		tmp5 = tmp5.multiply(tmp3);
		tmp4 = tmp4.add(tmp5);
		MGivenDifferentGivenM[i] = tmp4;
		return tmp4;
	}

	private BigDecimal computeMGivenDifferentGivenMGivenDifferent(int i) {
		return new BigDecimal(0.5);
	}

	private BigDecimal computeMGivenDifferentGivenMGivenEqual(int i) {
		return computeSGivenMGivenDifferent(i-1);
	}

	BigDecimal[] SGivenMGivenDifferent = new BigDecimal[257];
	
	private BigDecimal computeSGivenMGivenDifferent(int i) {
		if (SGivenMGivenDifferent[i] != null) return SGivenMGivenDifferent[i];
		if (i == 0) return new BigDecimal(1);
		BigDecimal tmp1 = computeEqualChallengesGivenDifferentGiven(i);
		BigDecimal tmp2 = BigDecimal.ONE;
		tmp2 = tmp2.add(tmp1.negate());
		BigDecimal tmp3 = new BigDecimal(0.5);
		tmp3 = tmp3.multiply(tmp2);
		//BigDecimal tmp4 = computeSGivenMGivenDifferent(i-1);
		BigDecimal tmp4 = BigDecimal.ONE;
		tmp4 = tmp4.multiply(tmp1);
		SGivenMGivenDifferent[i] = tmp4.add(tmp3);
		return SGivenMGivenDifferent[i];
	}

	BigDecimal[] EqualChallengesGivenDifferentGiven = new BigDecimal[257];

	private BigDecimal computeEqualChallengesGivenDifferentGiven(int i) {
		if (EqualChallengesGivenDifferentGiven[i] != null) return EqualChallengesGivenDifferentGiven[i];
		BigDecimal tmp1 = computeSGivenMGivenDifferent(i-1);
		double tmp2 = Math.pow(0.5, i);
		double tmp3 = 0.5- tmp2;
		double tmp4 = 1 - tmp2;
		tmp4 = tmp3/tmp4;
		BigDecimal tmp5 = new BigDecimal(tmp4);
		BigDecimal tmp6 = computeMGivenDifferent(i-1);
		BigDecimal tmp7 = computeMGivenDifferent(i);
		BigDecimal tmp8  = new BigDecimal(tmp6.doubleValue()/tmp7.doubleValue());
		EqualChallengesGivenDifferentGiven[i] = tmp1.multiply(tmp8.multiply(tmp5));
		return EqualChallengesGivenDifferentGiven[i];
	}

	BigDecimal[] distanceFraud = new BigDecimal[257];

	@Override
	public BigDecimal getDistanceFraudProbability() {
		//return distanceFraud(n, 0);
		if (n == 0) return BigDecimal.ONE;
		return new BigDecimal(distanceFraudFromDisk2(n));
	}
	
	
	public BigDecimal distanceFraud(int n, int q) {
		if (n == 0) return new BigDecimal(1);
		if (distanceFraud[n] != null) return distanceFraud[n];
		BigDecimal tmp1 = distanceFraud(n-1, q);
		BigDecimal tmp2 = computeFGivenD(n-1);
		tmp2 = tmp2.multiply(new BigDecimal(0.5));
		tmp1 = tmp1.multiply(new BigDecimal(0.25));
		distanceFraud[n] = tmp2.add(tmp1);
		return distanceFraud[n];
	}


	BigDecimal[] FGivenD = new BigDecimal[257];

	
	private BigDecimal computeFGivenD(int i) {
		if (i == 0) return BigDecimal.ONE;
		if (FGivenD[i] != null) return FGivenD[i];
		BigDecimal tmp2 = distanceFraud(i-1, 0);
		tmp2 = tmp2.multiply(new BigDecimal("0.125"));
		BigDecimal tmp5 = computeFGivenD(i-1);
		//tmp5 = tmp5.multiply(new BigDecimal("0.53125"));
		tmp5 = tmp5.multiply(new BigDecimal("0.5"));
		tmp5 = tmp5.add(tmp2);
		FGivenD[i] = tmp5;
		return FGivenD[i];
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
	public int getSizeOfTheChannel() {
		return 1;
	}

	@Override
	public boolean lackSecurityProof() {
		return true;
	}

	@Override
	public int getYearOfPublication() {
		return 2014;
	}

	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new TMAProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "TMA-{"+n+"}";
	}

	@Override
	public int getCryptoCalls() {
		return 1;
	}

	/** Apr 20, 2010 TnT Comment
	 * El valor del distance fraud esta en disco. Depende del valor de a, el archivo se llama "new_tree_a.dat". 
	 * Y entonces en este archivo podras encontrar la ronda a la que te encuentras.*/
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
	private double[] distances2;
	/*La informacion sobre el distance esta en un fichero de nombre "d_FDA-"+n*/
	private void fillDistance2(int n) throws IOException {
		//System.out.println("loaded file for distance fraud of the New protocol");
		String tmp;
		String[] split;
		int r;
		distances2 = new double[n];
		File f = new File(getDistanceFileNameOfValues());
		BufferedReader reader = new BufferedReader(new FileReader(f));
		while (reader.ready()){
			tmp = reader.readLine();
			split = tmp.split(" ");
			if (Integer.parseInt(split[0]) == n) break;
			distances2[Integer.parseInt(split[0])] = Double.parseDouble(split[1]);
		}
		reader.close();
	}	

	
	private double[] mafia2;

	/** Apr 20, 2010 TnT Comment
	 * El valor del distance fraud esta en disco. Depende del valor de a, el archivo se llama "tree_a.dat". 
	 * Y entonces en este archivo podras encontrar la ronda a la que te encuentras.*/
	private BigDecimal mafiaFraudFromDisk2(int n){
		if (mafia2 == null) {
			try {
				fillMafia2(257);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return new BigDecimal(mafia2[n]);
	}

	/*La informacion sobre el distance esta en un fichero de nombre "d_FDA-"+n*/
	private void fillMafia2(int n) throws IOException {
		//System.out.println("loaded FDA file for mafia fraud");
		String tmp;
		String[] split;
		int r;
		mafia2 = new double[n];
		File f = new File(getMafiaFileNameOfValues());
		BufferedReader reader = new BufferedReader(new FileReader(f));
		while (reader.ready()){
			tmp = reader.readLine();
			split = tmp.split(" ");
			if (Integer.parseInt(split[0]) == n) break;
			mafia2[Integer.parseInt(split[0])] = Double.parseDouble(split[1]);
		}
		reader.close();
	}
	public static void main(String[] args) {
		TMAProtocol p = new TMAProtocol();
		p.setNumberOfRounds(256);
		BigDecimal mafia = p.getMafiaFraudProbabilityExpensiveMethod();
		System.out.println("n = "+256+" Mafia = "+mafia.doubleValue());
		BigDecimal distance = p.distanceFraud(p.getNumberOfRounds(), 0);
		System.out.println("n = "+256+" Distance = "+distance.doubleValue());
		//generateDataFile();
	}

	public String getMafiaFileNameOfValues(){
		return "mafia_"+getAcronym()+"_values.DAT";
	}
	public String getDistanceFileNameOfValues(){
		return "distance_"+getAcronym()+"_values.DAT";
	}

	public static void generateDataFile() throws IOException{
		String newLine = System.getProperty("line.separator");
		TMAProtocol p = new TMAProtocol();
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

	@Override
	public int getTotalMsgSizeReceived() {
		return sizeOfNonces + n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 3*n;
	}

	@Override
	public int getNumberOfNoncesGenerated() {
		return 1;
	}

	@Override
	public int getBitsGenerated() {
		return 0;
	}
	

}
