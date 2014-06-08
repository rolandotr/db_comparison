package methodology;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import protocols.specifications.DBProtocol;

import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.SizeOfMessages;
import attributes.TerroristFraudProbability;
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
		main1(args);
		//main2(args);
	}
	
	public static void main1(String[] args) throws IOException {
		System.setOut(new PrintStream("evolution.txt"));
		DBProtocol[][] protocols = DBProtocol.loadProtocolsFairly();
		protocols = constraintProtocols(protocols);
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				//new TotalBitsExchanged(new IntegerRelation(), new NoScale<Integer>()),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new Memory(new MemoryRelation(), new KbitsScale()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
		};
		
		ParetoFrontier[] frontiers = ParetoFrontier.computeAllParetoFrontiers(protocols, attributes);
		
		
		History.saveInDiskTheFrontiers(frontiers, "evolution.obj");
		
		
		History.printHistory(protocols, attributes, frontiers);
		
		
		History.printLatexTable(frontiers, 0, 32, "evolution_table.tex");

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
			List<DBProtocol> tmp = new LinkedList<>();
			for (int j = 0; j < pList.length; j++) {
				BigDecimal mafiaUpperBound = (new BigDecimal("0.75")).pow(pList[j].getNumberOfRounds());
				BigDecimal distanceUpperBound = (new BigDecimal("0.75")).pow(pList[j].getNumberOfRounds());
				BigDecimal mafia = pList[j].getMafiaFraudProbability();
				BigDecimal distance = pList[j].getDistanceFraudProbability();
				long memory = pList[j].getMemory();
				if (mafia.compareTo(mafiaUpperBound) <= 0 &&
						distance.compareTo(distanceUpperBound) <= 0 &&
						memory <= memoryUpperBound){
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
