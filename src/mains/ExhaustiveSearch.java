package mains;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import protocol_specific_calculators.GenericOutDegree2Calculator;
import utils.EntropyCalculator;

public class ExhaustiveSearch {
	
	/***
	 * 
	 * @param args
	 * If args.lenght == 1:
	 * 		args[0]: Value of n. The search is performed on all graphs of order 2*n such that all vertices 
	 * 		have out-degree 2. 
	 * If args.lenght == 2:
	 * 		The edges going out of the vertex 0 are fixed to be (0, args[0]) and (0, args[1]) 
	 * 		Thus, the search is performed on all graphs of order 2*n containing these edges and satisfying that 
	 * 		every other vertex has out-degree 2. 
	 * 		This allows to parallelize the search by dividing the search space into (2n - 1)(2n - 2) 
	 * 		equal size regions.
	 * If args.lenght == 3:
	 * 		A combination of the two previous options: args[0] is the value of n and the edges 
	 * 		going out of the vertex 0 are fixed to be (0, args[1]) and (0, args[2])    
	 */

	public static void main(String[] args) throws IOException {
		
		int n = 8;
		boolean storeAllInfo = false;
		boolean saveAllGraphs = false;
		
		TreeMap<BigDecimal, ArrayList<String>> graphsPerMafiaSuccProb = new TreeMap<>();
		
		int iterateFrom = 0, defFst = 1, defSec = 2;
		String cacheFileName = "last-stored";
		String rankingFileName = "ranking";
		String allGraphsFileName = "all-graphs";
		
		if (args.length == 1) {
			n = Integer.parseInt(args[0]);
		}
		else if (args.length == 2) {
			iterateFrom = 1;
			defFst = Integer.parseInt(args[0]);
			defSec = Integer.parseInt(args[1]);
			if (defFst == defSec)
				defFst--;
			cacheFileName += "-" + defFst + "-" + defSec;
			rankingFileName += "-" + defFst + "-" + defSec;
			allGraphsFileName += "-" + defFst + "-" + defSec;
		}
		else if (args.length == 3) {
			n = Integer.parseInt(args[0]);
			iterateFrom = 1;
			defFst = Integer.parseInt(args[1]);
			defSec = Integer.parseInt(args[2]);
			if (defFst == defSec)
				defFst--;
			if (Integer.max(defFst, defSec) > 2*n - 1) {
				defFst = Integer.min(defFst, defSec);
				defSec = 2*n - 1;
				if (defFst > 2*n - 2)
					defFst = 2*n - 2;
			}
			cacheFileName += "-" + defFst + "-" + defSec;
			rankingFileName += "-" + defFst + "-" + defSec;
			allGraphsFileName += "-" + defFst + "-" + defSec;
		}
		cacheFileName += ".txt";
		rankingFileName += ".txt";
		allGraphsFileName += ".txt";
		
		GenericOutDegree2Calculator calculator = new GenericOutDegree2Calculator(cacheFileName, n, iterateFrom, defFst, defSec);
		
		do {
			
			DefaultDirectedGraph<String, DefaultEdge> genGraph = calculator.familyIterator.generateGraph();
			
			if (genGraph.vertexSet().size() == 2*n) {   // No point analyzing if the resulting graph is not connected
				BigDecimal mafiaSuccProb = calculator.mafia(calculator.familyIterator.n);
				
				if (mafiaSuccProb.compareTo(calculator.familyIterator.smallestMafiaSucccesProb) <= 0) {   // mafiaSuccProb <= smallestMafiaSucccesProb
					FileWriter file = new FileWriter(rankingFileName, true);					
					if (mafiaSuccProb.compareTo(calculator.familyIterator.smallestMafiaSucccesProb) < 0) {   //  mafiaSuccProb < smallestMafiaSucccesProb
						calculator.familyIterator.smallestMafiaSucccesProb = mafiaSuccProb;
						file.write("========================================================================" + System.getProperty("line.separator"));
					}
					file.write(calculator.familyIterator.overallGraphCount + "-th ghaph (" + calculator.familyIterator.connectedGraphCount + "-th connected graph):" + System.getProperty("line.separator"));
					file.write(calculator.familyIterator.toString() + System.getProperty("line.separator"));
					file.write("Mafia success probability: " + mafiaSuccProb + System.getProperty("line.separator"));
					file.write("Entropy (length-2n walks, non-squared probs): " + EntropyCalculator.entropy(genGraph, 2*n, "0", false, false) + System.getProperty("line.separator"));
					file.write("Entropy (all walk lengths, non-squared probs): " + EntropyCalculator.entropy(genGraph, 2*n, "0", true, false) + System.getProperty("line.separator"));
					file.write("Entropy (all walk lengths, squared probs): " + EntropyCalculator.entropy(genGraph, 2*n, "0", true, true) + System.getProperty("line.separator"));
					file.write("Entropy per walk length vector (non-squared probs): " + EntropyCalculator.entropyVector(genGraph, 2*n, "0") + System.getProperty("line.separator"));					
					file.close();
				}
				
				if (storeAllInfo) {
					if (graphsPerMafiaSuccProb.containsKey(mafiaSuccProb))
						graphsPerMafiaSuccProb.get(mafiaSuccProb).add(calculator.familyIterator.toString());
					else {
						ArrayList<String> newList = new ArrayList<>();
						newList.add(calculator.familyIterator.toString());
						graphsPerMafiaSuccProb.put(mafiaSuccProb, newList);
					}
				}
				
				if (saveAllGraphs) {
					FileWriter file = new FileWriter(allGraphsFileName, true);
					file.write(calculator.familyIterator.overallGraphCount + "-th ghaph (" + calculator.familyIterator.connectedGraphCount + "-th connected graph):" + System.getProperty("line.separator"));
					file.write(calculator.familyIterator.toString() + System.getProperty("line.separator"));
					file.write("Mafia success probability: " + mafiaSuccProb + System.getProperty("line.separator"));
					file.write("Entropy (length 2n walks, non-squared probs): " + EntropyCalculator.entropy(genGraph, 2*n, "0", false, false) + System.getProperty("line.separator"));
					file.write("Entropy (all walk lengths, non-squared probs): " + EntropyCalculator.entropy(genGraph, 2*n, "0", true, false) + System.getProperty("line.separator"));
					file.write("Entropy (all walk lengths, squared probs): " + EntropyCalculator.entropy(genGraph, 2*n, "0", true, true) + System.getProperty("line.separator"));
					file.write("Entropy per walk length vector (non-squared probs): " + EntropyCalculator.entropyVector(genGraph, 2*n, "0") + System.getProperty("line.separator"));
					file.write("========================================================================" + System.getProperty("line.separator"));
					file.close();
				}
			}	
			calculator.familyIterator.saveCurrentState(cacheFileName);
			
		} while (calculator.familyIterator.next());
				
		if (storeAllInfo) {
			Writer out = new FileWriter("general-info-" + n + ".txt", true);
			out.write("========================================================================" + System.getProperty("line.separator"));
			out.write("Sorted by mafia success probability" + System.getProperty("line.separator"));
			out.write("========================================================================" + System.getProperty("line.separator"));
			for (BigDecimal mafiaValue : graphsPerMafiaSuccProb.keySet()) {
				out.write("Mafia success probability: " + mafiaValue + System.getProperty("line.separator"));
				for (int gr = 0; gr < graphsPerMafiaSuccProb.get(mafiaValue).size(); gr++) {
					out.write("\t" + gr + "-th ghaph:" + System.getProperty("line.separator"));
					out.write("\t\t" + graphsPerMafiaSuccProb.get(mafiaValue).get(gr) + System.getProperty("line.separator"));
				}
				out.write("========================================================================" + System.getProperty("line.separator"));
			}			
			out.close();
		}

	}

}
