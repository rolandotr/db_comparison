package mains;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.TreeMap;

import methodology.Evolution;
import methodology.History;
import methodology.InternalState;
import methodology.ParetoFrontier;
import protocols.specifications.DBProtocol;
import utils.Progress;
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

public class Comparison {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		int exponent = Integer.parseInt(args[0]);
		System.setOut(new PrintStream("evolution-"+exponent+".txt"));
		System.out.println("Starting");
		double mafiaUpperBound = Math.pow(0.5, exponent);
		//DBProtocol[][] protocols = DBProtocol.loadProtocolsFairly();
		DBProtocol[][] protocols = new DBProtocol[][]{DBProtocol.loadProtocols()};
		System.out.println("Total protocols: "+protocols.length+" and "+protocols[0].length);
		protocols = Evolution.constraintProtocols(protocols, mafiaUpperBound);
		System.out.println("Total protocols: "+protocols.length+" and "+protocols[0].length);
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
		
		String fileName = "comparison_"+exponent+"_"+DBProtocol.MAX_N+".obj";
		InternalState state = null;
		File f = new File(fileName);
		if (f.exists()){
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			state = (InternalState)in.readObject();
			in.close();
		}
		else{
			state = new InternalState(fileName);
			state.setIndexesToBeRemoved(new TreeMap<Integer, List<Integer>>());
			state.setProgress(new Progress(0));
		}
		
		//ParetoFrontier[] frontiers = ParetoFrontier.computeAllParetoFrontiers(protocols, attributes, state);
		ParetoFrontier[] frontiers = ParetoFrontier.computeAllParetoFrontiers(protocols, attributes);
		
		System.out.println("Saving on disk");
		
		History.saveInDiskTheFrontiers(frontiers, "finalResult_"+exponent+".obj");
		
		
		History.printHistory(protocols, attributes, frontiers);
		
		System.out.println("Printing latex table");
		
		History.printLatexTable(frontiers, 0, 1, "evolution_table.tex");

	}
}
