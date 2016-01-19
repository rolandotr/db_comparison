package methodology;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import protocols.specifications.DBProtocol;
import utils.Gnuplot;

import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.SizeOfMessages;
import attributes.TerroristFraudProbability;
import attributes.TotalBitsExchanged;
import attributes.relations.FinalSlowPhaseRelation;
import attributes.relations.IntegerRelation;
import attributes.relations.MemoryRelation;
import attributes.relations.ProbabilityRelation;
import attributes.relations.SizeOfMessagesRelation;
import attributes.scales.KbitsScale;
import attributes.scales.LogScale;
import attributes.scales.NoScale;

/*Trujillo- Apr 16, 2014
 * This is class is designed to provide results when the number of rounds increase. Different to the 
 * class History, here the attribute Year is not relevant.*/
public abstract class Evolution {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		createComparisonTable(args);
		//main2(args);
	}
	
	public static void createComparisonTable(String[] args) throws IOException {
		System.setOut(new PrintStream("evolution.txt"));
		System.out.println("Starting");
		//DBProtocol[][] protocols = DBProtocol.loadProtocolsFairly();
		DBProtocol[][] protocols = new DBProtocol[][]{DBProtocol.loadProtocols()};
		/*Trujillo- Jan 19, 2016
		 * Next, we constrain the protocols to resist mafia fraud with the following
		 * upper bound*/
		protocols = constraintProtocols(protocols, Math.pow(0.5, 196));
		System.out.println("Total protocols: "+protocols.length+" and "+protocols[0].length);
		//protocols = constraintProtocols(protocols);
		//System.out.println("Total protocols: "+protocols.length+" and "+protocols[0].length);
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				//new MafiaFraudProbability(new DoubleRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				//new DistanceFraudProbability(new DoubleRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				//new TerroristFraudProbability(new DoubleRelation(), new LogScale(2)),
				new TotalBitsExchanged(new IntegerRelation(), new NoScale<Integer>()),
				//new TotalBitsExchanged(new BitsExchangedRelation(), new NoScale<Integer>()),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				//new SizeOfMessages(new IntegerRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new Memory(new MemoryRelation(), new KbitsScale()),
				//new Memory(new LongRelation(), new KbitsScale()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
		};
		
		
		ParetoFrontier[] frontiers = ParetoFrontier.computeAllParetoFrontiers(protocols, attributes);
		
		System.out.println("Saving on disk");
		
		History.saveInDiskTheFrontiers(frontiers, "evolution.obj");
		
		
		History.printHistory(protocols, attributes, frontiers);
		
		//frontiers =  orderFrontierByBitsExchanged(frontiers, attributes);
		
		System.out.println("Printing latex table");
		
		History.printLatexTable(frontiers, 0, 1, "evolution_table.tex");

		Gnuplot.createDatafile(frontiers[0].getProtocols(), frontiers[0].getFrontier(), "all");
		//Gnuplot.createDatafile(frontiers[0].getProtocols(), frontiers[0].getFrontier(), "all_relation");
		//Gnuplot.createDatafile(frontiers[0].getProtocols(), frontiers[0].getFrontier(), "all_without_relation");
		
	}
	
	private static ParetoFrontier[] orderFrontierByBitsExchanged(
			ParetoFrontier[] frontiers, Attribute[] attributes) {
		TreeMap<Integer, List<DBProtocol>> result = new TreeMap<>();
		for (int i = 0; i < frontiers.length; i++) {
			DBProtocol[] protocols = frontiers[i].getFrontier();
			for (int j = 0; j < protocols.length; j++) {
				int e = protocols[j].getTotalBitsExchangedDuringFastPhase();
				//System.out.println("Adding protocol "+protocols[j].getIdentifier()+" with e = "+protocols[j].getTotalBitsExchangedDuringFastPhase());
				if (!result.containsKey(e)){
					result.put(e, new LinkedList<DBProtocol>());
				}
				result.get(e).add(protocols[j]);
			}
		}
		ParetoFrontier[] result2 = new ParetoFrontier[result.size()];
		int index = 0;
		for (List<DBProtocol> protocols : result.values()){
			DBProtocol[] pArray = new DBProtocol[protocols.size()];
			int pos = 0;
			for (DBProtocol p : protocols) pArray[pos++] = p;
			//System.out.println("Adding "+pArray.length+" protocols with e = "+pArray[0].getTotalBitsExchangedDuringFastPhase());
			result2[index++] = new ParetoFrontier(pArray, pArray, attributes, null);
		}
		return result2;
	}

	public static void main2(String[] args) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("evolution.obj")));
		ParetoFrontier[] frontiers = (ParetoFrontier[])in.readObject();
		
		History.printLatexTable(frontiers, 0, 32, "evolution_table.tex");

	}
	
	

	/*Trujillo- Apr 16, 2014
	 * In this method we going to constraint the protocols to have:
	 * - A mafia fraud lower than (3/4)^n
	 * - A distance fraud lower than (3/4)^n
	 * - A memory requirement lower than 4Kbytes
	 * */
	public static DBProtocol[][] constraintProtocols(DBProtocol[][] protocols) {
		List<DBProtocol[]> tmpResult = new LinkedList<>();
		for (int i = 0; i < protocols.length; i++) {
			DBProtocol[] pList = protocols[i];
			long memoryUpperBound = 8192;//4Kbyte which is 8192 bits.
			//long memoryUpperBound = Long.MAX_VALUE;//4Kbyte which is 8192 bits.
			List<DBProtocol> tmp = new LinkedList<>();
			for (int j = 0; j < pList.length; j++) {
				//BigDecimal mafiaUpperBound = (new BigDecimal("0.75")).pow(pList[j].getNumberOfRounds());
				//BigDecimal distanceUpperBound = (new BigDecimal("0.75")).pow(pList[j].getNumberOfRounds());
				BigDecimal mafiaUpperBound = (new BigDecimal("1")).pow(pList[j].getNumberOfRounds());
				BigDecimal distanceUpperBound = (new BigDecimal("1")).pow(pList[j].getNumberOfRounds());
				BigDecimal mafia = pList[j].getMafiaFraudProbability();
				BigDecimal distance = pList[j].getDistanceFraudProbability();
				long memory = pList[j].getMemory();
				long bitsExchanged = pList[j].getTotalBitsExchangedDuringFastPhase();
				if (mafia.compareTo(mafiaUpperBound) <= 0 &&
						distance.compareTo(distanceUpperBound) <= 0 &&
						memory <= memoryUpperBound &&
						bitsExchanged <= DBProtocol.MAX_N*2){
					tmp.add(pList[j]);//this protocol meets the constraints.
				}
			}
			if (tmp.isEmpty()) continue;
			DBProtocol[] tmp2 = new DBProtocol[tmp.size()];
			int pos = 0;
			for (DBProtocol p : tmp) {
				tmp2[pos++] = p;
			}
			tmpResult.add(tmp2);
		}
		DBProtocol[][] result = new DBProtocol[tmpResult.size()][];
		int pos = 0;
		for (DBProtocol[] list : tmpResult){
			result[pos++] = list;
		}
		return result;
	}

	public static DBProtocol[][] constraintProtocols(DBProtocol[][] protocols, double mafiaUpperBound) {
		List<DBProtocol[]> tmpResult = new LinkedList<>();
		for (int i = 0; i < protocols.length; i++) {
			DBProtocol[] pList = protocols[i];
			List<DBProtocol> tmp = new LinkedList<>();
			for (int j = 0; j < pList.length; j++) {
				BigDecimal mafia = pList[j].getMafiaFraudProbability();
				if (mafia.doubleValue() <= mafiaUpperBound){
					tmp.add(pList[j]);//this protocol meets the constraints.
				}
			}
			if (tmp.isEmpty()) continue;
			DBProtocol[] tmp2 = new DBProtocol[tmp.size()];
			int pos = 0;
			for (DBProtocol p : tmp) {
				tmp2[pos++] = p;
			}
			tmpResult.add(tmp2);
		}
		DBProtocol[][] result = new DBProtocol[tmpResult.size()][];
		int pos = 0;
		for (DBProtocol[] list : tmpResult){
			result[pos++] = list;
		}
		return result;
	}

	public static DBProtocol[][] constraintProtocolsToMafiaAndDistance(DBProtocol[][] protocols, 
			double mafiaUpperBound, double distanceUpperBound, double terroristUpperBound, 
			int cryptoCallsUpperBound) {
		List<DBProtocol[]> tmpResult = new LinkedList<>();
		for (int i = 0; i < protocols.length; i++) {
			DBProtocol[] pList = protocols[i];
			List<DBProtocol> tmp = new LinkedList<>();
			for (int j = 0; j < pList.length; j++) {
				BigDecimal mafia = pList[j].getMafiaFraudProbability();
				BigDecimal distance = pList[j].getDistanceFraudProbability();
				BigDecimal terrorist = pList[j].getTerroristFraudProbability();
				int cryptoCalls = pList[j].getCryptoCalls();
				if (mafia.doubleValue() <= mafiaUpperBound &&
						distance.doubleValue() <= distanceUpperBound &&
						terrorist.doubleValue() <= terroristUpperBound &&
						cryptoCalls <= cryptoCallsUpperBound){
					tmp.add(pList[j]);//this protocol meets the constraints.
				}
			}
			if (tmp.isEmpty()) continue;
			DBProtocol[] tmp2 = new DBProtocol[tmp.size()];
			int pos = 0;
			for (DBProtocol p : tmp) {
				tmp2[pos++] = p;
			}
			tmpResult.add(tmp2);
		}
		DBProtocol[][] result = new DBProtocol[tmpResult.size()][];
		int pos = 0;
		for (DBProtocol[] list : tmpResult){
			result[pos++] = list;
		}
		return result;
	}
}
