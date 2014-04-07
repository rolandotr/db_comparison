package dataset;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import protocols.DBProtocol;

/*Trujillo- Mar 24, 2014
 * This class takes all the protocols implementing the class DBProtocols, runs over all possible 
 * parameter settings defined by those protocols, execute them, and record the values in a file-based
 * local database (.DAT files).
 * 
 * The idea is to run the protocols once and then use the resulting values as many times as needed.
 * 
 * The dataset is organized as follows. There exists a folder named "dataset".
 * Into the dataset folder there are several folder with numbers. The number simply means the number of rounds.
 * For a given n, we have a file .DAT that contains the identifier of the protocol (as defined in the DB protocol class) 
 * and then the values for each considered attribute.
 * 
 * The attributes are those defined in the package attributes.
 *   
 * */
public abstract class DatasetCreation {

	public static final int MAX_N = 128;
	
	public static void main(String[] args) throws IOException {
		int factor = 0;
		int remain = 0;
		int module = 0;
		factor = Integer.parseInt(args[0]);
		remain = Integer.parseInt(args[1]);
		module = Integer.parseInt(args[2]);
		createDataset(factor, remain, module);
	}
	
	
	/*Trujillo- Mar 24, 2014
	 * This method loads first the classes implemented in the package protocols than extend DB protocols.*/
	public static void createDataset(int factor, int remain, int module) throws IOException{
		File f = new File("dataset");
		f.mkdir();		
		DBProtocol[] protocols = DBProtocol.loadProtocols(factor);
		for (int n = 1; n <= MAX_N; n++) {
			if (n % module != remain) continue;
			File nFolder = new File(f.getAbsolutePath()+"/"+n);
			nFolder.mkdir();		
			System.out.println("n = "+n);
			for (int i = 0; i < protocols.length; i++) {
				System.out.println("Analyzing protocol "+protocols[i].getIdentifier());
				createDataForProtocol(protocols[i], nFolder, n);
			}
		}
	}
	
	public static void createDataset(int factor) throws IOException{
		createDataset(factor, 0, 1);
	}

	private static void createDataForProtocol(DBProtocol p,
			File nFolder, int n) throws IOException {
		File f = new File(nFolder.getAbsoluteFile()+"/"+p.getIdentifier()+".DAT");
		f.createNewFile();
		FileWriter writer = new FileWriter(f);
		writer.write("# \t mafia \t distance \t terrorist \t memory \t finalSlowPhase \t multiBit \t cryptoCalls \t bitsExchanged \t year"+System.getProperty("line.separator"));
		System.out.println("Mafia fraud");
		double mafia = p.getMafiaFraudProbability(n).doubleValue();
		System.out.println("Distance Fraud");
		double distance = p.getDistanceFraudProbability(n).doubleValue();
		double terrorist = p.getTerroristFraudProbability(n).doubleValue();
		long memory = p.getMemory(n);
		boolean finalSlowPhase = p.hasFinalSlowPhase();
		boolean multiBit = p.hasMultipleBitExchange();
		int cryptoCalls = p.getCryptoCalls();
		int bitsExchanged = p.getTotalBitsExchanged(n);
		int year = p.getYearOfPublication();
		writer.append("  \t "+mafia+" \t "+distance+" \t "+terrorist+" \t "+memory+" \t "+finalSlowPhase+" \t "+
						multiBit+" \t "+cryptoCalls+" \t "+bitsExchanged+" \t"+year);
		writer.close();
	}

}
