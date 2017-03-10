package mains;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import protocol_specific_calculators.GenericCayleyCalculator;
import protocol_specific_calculators.GenericCayleyCalculator.CayleyInfo;
import utils.EntropyCalculator;

public class SearchCayleyFamilies {
	
	static DefaultDirectedGraph<String, DefaultEdge> generateGraph(int n, int i, int j) {
		
		DefaultDirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
		graph.addVertex("0");
		Set<Integer> allGenerated = new HashSet<>(), lastGenerated = new HashSet<>();
		allGenerated.add(0);
		lastGenerated.add(0);
		while (lastGenerated.size() != 0) {
			Set<Integer> newlyGenerated = new HashSet<>();
			for (Integer elem : lastGenerated) {
				int gen = (elem.intValue() + i) % (2*n);
				if (!graph.containsVertex(gen + ""))
					graph.addVertex(gen + "");
				if (!graph.containsEdge(elem.toString(), gen + ""))
					graph.addEdge(elem.toString(), gen + "");
				if (!allGenerated.contains(gen)) {	
					newlyGenerated.add(gen);
					allGenerated.add(gen);
				}
				gen = (elem.intValue() + j) % (2*n);
				if (!graph.containsVertex(gen + ""))
					graph.addVertex(gen + "");
				if (!graph.containsEdge(elem.toString(), gen + ""))
					graph.addEdge(elem.toString(), gen + "");
				if (!allGenerated.contains(gen)) {	
					newlyGenerated.add(gen);
					allGenerated.add(gen);
				}
			}
			lastGenerated = newlyGenerated;
		}
		return graph;
	}

	static void printSymmeticEdges(DefaultDirectedGraph<String, DefaultEdge> graph) {
		ArrayList<String> info = new ArrayList<>();
		ArrayList<String> vertList = new ArrayList<>(graph.vertexSet());
		for (int i = 0; i < vertList.size() - 1; i++)
			for (int j = i + 1; j < vertList.size(); j++)
				if (graph.containsEdge(vertList.get(i), vertList.get(j)) && graph.containsEdge(vertList.get(j), vertList.get(i)))
					info.add("(" + vertList.get(i).toString() + "<->" + vertList.get(j) + ")");
		System.out.println("\t\tSymmetic edges: " + info.toString());
	}
	
	static void printSymmeticEdges(Writer out, DefaultDirectedGraph<String, DefaultEdge> graph) throws IOException {
		ArrayList<String> info = new ArrayList<>();
		ArrayList<String> vertList = new ArrayList<>(graph.vertexSet());
		for (int i = 0; i < vertList.size() - 1; i++)
			for (int j = i + 1; j < vertList.size(); j++)
				if (graph.containsEdge(vertList.get(i), vertList.get(j)) && graph.containsEdge(vertList.get(j), vertList.get(i)))
					info.add("(" + vertList.get(i).toString() + "<->" + vertList.get(j) + ")");
		out.write("\t\tSymmetic edges: " + info.toString() + System.getProperty("line.separator"));
	}

	/***
	 * 
	 * @param args
	 * If args.length == 0:
	 * 		By default the search covers the family of all Cayley graphs of order 16
	 * If args.length == 2:
	 * 		The search covers the families of all Cayley graphs of orders 2*min(args[0],args[1]), 
	 * 		2*min(args[0],args[1])+2,...,2*max(args[0],args[1])
	 * 
	 */
	
	public static void main(String[] args) throws IOException {
		
		int graphCount = 0;
		
		int startValue = 8, endValue = 8;
		
		if (args.length == 2) {
			startValue = Integer.parseInt(args[0]);
			endValue = Integer.parseInt(args[1]);
			if (startValue > endValue) {
				int tmp = startValue;
				startValue = endValue;
				endValue = tmp;
			}
		}
		
		for (int n = startValue; n <= endValue; n++) {
			TreeMap<BigDecimal, TreeSet<CayleyInfo>> graphsPerMafiaSuccProb = new TreeMap<>();
			TreeMap<CayleyInfo, BigDecimal> mafiaSuccProbPerGraph = new TreeMap<>();
			TreeMap<Double, TreeSet<CayleyInfo>> graphsPerEntropy = new TreeMap<>(Collections.reverseOrder());
			TreeMap<CayleyInfo, Double> entropyPerGraph = new TreeMap<>();
			for (int i = 1; i <= 2*n - 2; i++) {
				for (int j = i + 1; j <= 2*n - 1; j++) {
					DefaultDirectedGraph<String, DefaultEdge> genGraph = generateGraph(n, i, j);
					if (genGraph.vertexSet().size() == 2*n) {   // {i,j} is a generator of {0,...,2n-1}
						GenericCayleyCalculator mafiaCalculator = new GenericCayleyCalculator(n, i, j);
						CayleyInfo grInfo = mafiaCalculator.new CayleyInfo(n, i, j);
						System.out.println(graphCount + "-th ghaph:");
						System.out.println("n = " + n + ", i = " + i + ", j = " + j);
						BigDecimal mafiaSuccProb = mafiaCalculator.mafia(mafiaCalculator.getN());
						System.out.println("Mafia success probability: " + mafiaSuccProb);
						if (graphsPerMafiaSuccProb.containsKey(mafiaSuccProb))
							graphsPerMafiaSuccProb.get(mafiaSuccProb).add(grInfo);
						else {
							TreeSet<CayleyInfo> newSet = new TreeSet<>();
							newSet.add(grInfo);
							graphsPerMafiaSuccProb.put(mafiaSuccProb, newSet);
						}
						mafiaSuccProbPerGraph.put(grInfo, mafiaSuccProb);
						double entropy = EntropyCalculator.entropy(genGraph, n, "0", true, false);
						System.out.println("Entropy: " + entropy);
						if (graphsPerEntropy.containsKey(entropy))
							graphsPerEntropy.get(entropy).add(mafiaCalculator.new CayleyInfo(n, i, j));
						else {
							TreeSet<CayleyInfo> newSet = new TreeSet<>();
							newSet.add(mafiaCalculator.new CayleyInfo(n, i, j));
							graphsPerEntropy.put(entropy, newSet);
						}
						entropyPerGraph.put(grInfo, entropy);
						System.out.println("========================================================================");
						graphCount++;
					}
				}
			}
			
			ArrayList<CayleyInfo> grInfos = new ArrayList<>(mafiaSuccProbPerGraph.keySet());
			
			int lessAndLessCount = 0, lessAndLeqCount = 0, lessAndEqCount = 0, lessAndGeqCount = 0, lessAndGreaterCount = 0, lessCount = 0;
			int leqAndLessCount = 0, leqAndLeqCount = 0, leqAndEqCount = 0, leqAndGeqCount = 0, leqAndGreaterCount = 0, leqCount = 0;
			int eqAndLessCount = 0, eqAndLeqCount = 0, eqAndEqCount = 0, eqAndGeqCount = 0, eqAndGreaterCount = 0, eqCount = 0;
			int geqAndLessCount = 0, geqAndLeqCount = 0, geqAndEqCount = 0, geqAndGeqCount = 0, geqAndGreaterCount = 0, geqCount = 0;
			int greaterAndLessCount = 0, greaterAndLeqCount = 0, greaterAndEqCount = 0, greaterAndGeqCount = 0, greaterAndGreaterCount = 0, greaterCount = 0;
			
			for (int i = 0; i < grInfos.size() - 1; i++)
				for (int j = i + 1; j < grInfos.size(); j++)
					if (mafiaSuccProbPerGraph.get(grInfos.get(i)).compareTo(mafiaSuccProbPerGraph.get(grInfos.get(j))) < 0) {
						lessCount++;
						leqCount++;
						if (entropyPerGraph.get(grInfos.get(i)) < entropyPerGraph.get(grInfos.get(j))) {
							lessAndLessCount++;
							lessAndLeqCount++;
							leqAndLessCount++;
							leqAndLeqCount++;
						}
						else if (entropyPerGraph.get(grInfos.get(i)) == entropyPerGraph.get(grInfos.get(j))) {
							lessAndLeqCount++;
							lessAndEqCount++;
							lessAndGeqCount++;
							leqAndLeqCount++;
							leqAndEqCount++;
							leqAndGeqCount++;
						}
						else if (entropyPerGraph.get(grInfos.get(i)) > entropyPerGraph.get(grInfos.get(j))) {
							lessAndGeqCount++;
							lessAndGreaterCount++;
							leqAndGeqCount++;
							leqAndGreaterCount++;
						}
					}
					else if (mafiaSuccProbPerGraph.get(grInfos.get(i)).compareTo(mafiaSuccProbPerGraph.get(grInfos.get(j))) == 0) {
						leqCount++;
						eqCount++;
						geqCount++;
						if (entropyPerGraph.get(grInfos.get(i)) < entropyPerGraph.get(grInfos.get(j))) {
							leqAndLessCount++;
							leqAndLeqCount++;
							eqAndLessCount++;
							eqAndLeqCount++;
							geqAndLessCount++;
							geqAndLeqCount++;
						}
						else if (entropyPerGraph.get(grInfos.get(i)) == entropyPerGraph.get(grInfos.get(j))) {
							leqAndLeqCount++;
							leqAndEqCount++;
							leqAndGeqCount++;
							eqAndLeqCount++;
							eqAndEqCount++;
							eqAndGeqCount++;
							geqAndLeqCount++;
							geqAndEqCount++;
							geqAndGeqCount++;
						}
						else if (entropyPerGraph.get(grInfos.get(i)) > entropyPerGraph.get(grInfos.get(j))) {
							leqAndGeqCount++;
							leqAndGreaterCount++;
							eqAndGeqCount++;
							eqAndGreaterCount++;
							geqAndGeqCount++;
							geqAndGreaterCount++;
						}
					}
					else if (mafiaSuccProbPerGraph.get(grInfos.get(i)).compareTo(mafiaSuccProbPerGraph.get(grInfos.get(j))) > 0) {
						geqCount++;
						greaterCount++;
						if (entropyPerGraph.get(grInfos.get(i)) < entropyPerGraph.get(grInfos.get(j))) {
							geqAndLessCount++;
							geqAndLeqCount++;
							greaterAndLessCount++;
							greaterAndLeqCount++;
						}
						else if (entropyPerGraph.get(grInfos.get(i)) == entropyPerGraph.get(grInfos.get(j))) {
							geqAndLeqCount++;
							geqAndEqCount++;
							geqAndGeqCount++;
							greaterAndLeqCount++;
							greaterAndEqCount++;
							greaterAndGeqCount++;
						}
						else if (entropyPerGraph.get(grInfos.get(i)) > entropyPerGraph.get(grInfos.get(j))) {
							geqAndGeqCount++;
							geqAndGreaterCount++;
							greaterAndGeqCount++;
							greaterAndGreaterCount++;
						}
					}
			
			System.out.println("Testing hypotheses:");
			System.out.println("<<mafia-succ-prob(G1) w.r.t. mafia-succ-prob(G2)");
			System.out.println("\tentropy(G1) w.r.t. entropy(G2)>>");
			System.out.println("mafia-succ-prob(G1) < mafia-succ-prob(G2): " + lessCount);
			System.out.println("\tentropy(G1) < entropy(G2): " + lessAndLessCount);
			System.out.println("\tentropy(G1) <= entropy(G2): " + lessAndLeqCount);
			System.out.println("\tentropy(G1) == entropy(G2): " + lessAndEqCount);
			System.out.println("\tentropy(G1) >= entropy(G2): " + lessAndGeqCount);
			System.out.println("\tentropy(G1) > entropy(G2): " + lessAndGreaterCount);
			
			System.out.println("mafia-succ-prob(G1) <= mafia-succ-prob(G2): " + leqCount);
			System.out.println("\tentropy(G1) < entropy(G2): " + leqAndLessCount);
			System.out.println("\tentropy(G1) <= entropy(G2): " + leqAndLeqCount);
			System.out.println("\tentropy(G1) == entropy(G2): " + leqAndEqCount);
			System.out.println("\tentropy(G1) >= entropy(G2): " + leqAndGeqCount);
			System.out.println("\tentropy(G1) > entropy(G2): " + leqAndGreaterCount);
			
			System.out.println("mafia-succ-prob(G1) == mafia-succ-prob(G2): " + eqCount);
			System.out.println("\tentropy(G1) < entropy(G2): " + eqAndLessCount);
			System.out.println("\tentropy(G1) <= entropy(G2): " + eqAndLeqCount);
			System.out.println("\tentropy(G1) == entropy(G2): " + eqAndEqCount);
			System.out.println("\tentropy(G1) >= entropy(G2): " + eqAndGeqCount);
			System.out.println("\tentropy(G1) > entropy(G2): " + eqAndGreaterCount);
			
			System.out.println("mafia-succ-prob(G1) >= mafia-succ-prob(G2): " + geqCount);
			System.out.println("\tentropy(G1) < entropy(G2): " + geqAndLessCount);
			System.out.println("\tentropy(G1) <= entropy(G2): " + geqAndLeqCount);
			System.out.println("\tentropy(G1) == entropy(G2): " + geqAndEqCount);
			System.out.println("\tentropy(G1) >= entropy(G2): " + geqAndGeqCount);
			System.out.println("\tentropy(G1) > entropy(G2): " + geqAndGreaterCount);
			
			System.out.println("mafia-succ-prob(G1) > mafia-succ-prob(G2): " + greaterCount);
			System.out.println("\tentropy(G1) < entropy(G2): " + greaterAndLessCount);
			System.out.println("\tentropy(G1) <= entropy(G2): " + greaterAndLeqCount);
			System.out.println("\tentropy(G1) == entropy(G2): " + greaterAndEqCount);
			System.out.println("\tentropy(G1) >= entropy(G2): " + greaterAndGeqCount);
			System.out.println("\tentropy(G1) > entropy(G2): " + greaterAndGreaterCount);
			System.out.println("========================================================================");
			
			Writer out = new FileWriter("info-" + n + "-mafia-succ-prob.txt", true);
			out.write("========================================================================" + System.getProperty("line.separator"));
			out.write("Sorted by mafia success probability" + System.getProperty("line.separator"));
			out.write("========================================================================" + System.getProperty("line.separator"));
			for (BigDecimal mafiaValue : graphsPerMafiaSuccProb.keySet()) {
				out.write("Mafia success probability: " + mafiaValue + System.getProperty("line.separator"));
				int gr = 0;
				for (CayleyInfo grInfo : graphsPerMafiaSuccProb.get(mafiaValue)) {
					out.write("\t" + gr + "-th ghaph:" + System.getProperty("line.separator"));
					out.write("\t\tn = " + grInfo.n + ", i = " + grInfo.i + ", j = " + grInfo.j + System.getProperty("line.separator"));
					DefaultDirectedGraph<String, DefaultEdge> cg = generateGraph(n, grInfo.i, grInfo.j);
					out.write("\t\t" + cg.toString() + System.getProperty("line.separator"));
					printSymmeticEdges(out, cg);
					gr++;
				}
				out.write("========================================================================" + System.getProperty("line.separator"));
			}			
			out.close();
			
			out = new FileWriter("info-" + n + "-entropy.txt", true);
			out.write("========================================================================" + System.getProperty("line.separator"));
			out.write("Sorted by entropy" + System.getProperty("line.separator"));
			out.write("========================================================================" + System.getProperty("line.separator"));
			for (double entropyValue : graphsPerEntropy.keySet()) {
				out.write("Entropy: " + entropyValue + System.getProperty("line.separator"));
				int gr = 0;
				for (CayleyInfo grInfo : graphsPerEntropy.get(entropyValue)) {
					out.write("\t" + gr + "-th ghaph:" + System.getProperty("line.separator"));
					out.write("\t\tn = " + grInfo.n + ", i = " + grInfo.i + ", j = " + grInfo.j + System.getProperty("line.separator"));
					DefaultDirectedGraph<String, DefaultEdge> cg = generateGraph(n, grInfo.i, grInfo.j);
					out.write("\t\t" + cg.toString() + System.getProperty("line.separator"));
					printSymmeticEdges(out, cg);
					gr++;
				}
				out.write("========================================================================" + System.getProperty("line.separator"));
			}			
			out.close();
			
		}

	}

}
