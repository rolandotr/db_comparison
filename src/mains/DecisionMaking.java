package mains;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import methodology.Evolution;
import methodology.History;
import methodology.ParetoFrontier;
import protocols.specifications.DBProtocol;
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

public class DecisionMaking {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		double[] thresholds = new double[]{Math.pow(0.5, 32), 
				Math.pow(0.5, 64), Math.pow(0.5, 128),};
		int[] thresholdsCrypto = new int[]{1,4};
		for (int i = 0; i < thresholds.length; i++) {
			for (int j = 0; j < thresholdsCrypto.length; j++) {
				bestProtocols(thresholds[i], 1, 1, thresholdsCrypto[j], "mafia-"+i+"-"+j);
				bestProtocols(thresholds[i], thresholds[i], 1, thresholdsCrypto[j], "mafia-distance"+i+"-"+j);
				bestProtocols(thresholds[i], thresholds[i], thresholds[i], thresholdsCrypto[j], "mafia-distance-terrorist"+i+"-"+j);
			}
		}
	}
	
	public static void bestProtocols(double mafiaUpperBound, 
			double distanceUpperBound, double terroristUpperBound, int cryptoOperations, String name) throws FileNotFoundException, IOException {
		System.setOut(new PrintStream(name+".txt"));		
		System.out.println("Starting");
		DBProtocol[][] protocols = new DBProtocol[][]{DBProtocol.loadProtocols()};
		System.out.println("Total protocols: "+protocols.length+" and "+protocols[0].length);
		protocols = Evolution.constraintProtocols(protocols);
		ParetoFrontier[] frontiers = null;
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TotalBitsExchanged(new IntegerRelation(), new NoScale<Integer>()),
				//new TotalBitsExchanged(new BitsExchangedRelation(), new NoScale<Integer>()),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new Memory(new MemoryRelation(), new KbitsScale()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
		};
		
		if (protocols.length == 0){
			System.out.println("No protocols after constraining");
		}
		else{
			System.out.println("Total protocols: "+protocols.length+" and "+protocols[0].length);
			protocols = Evolution.constraintProtocolsToMafiaAndDistance(protocols, mafiaUpperBound, 
					distanceUpperBound, terroristUpperBound, cryptoOperations);
			if (protocols.length == 0){
				frontiers = new ParetoFrontier[]{};
				System.out.println("No protocols after constraining");
			}
			else{
				System.out.println("Total protocols: "+protocols.length+" and "+protocols[0].length);
				frontiers = ParetoFrontier.computeAllParetoFrontiers(protocols, attributes);
			}		
		}
		System.out.println("Saving on disk");
		
		History.saveInDiskTheFrontiers(frontiers, name+".obj");
		
		
		History.printHistory(protocols, attributes, frontiers);
		
		System.out.println("Printing latex table");
		
		History.printLatexTable(frontiers, 0, 1, name+"_table.tex");


	}
}
