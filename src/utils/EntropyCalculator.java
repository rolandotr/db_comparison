package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class EntropyCalculator {
	
	public static double entropy(DefaultDirectedGraph<String, DefaultEdge> graph, int walkLength, String initialVertex, boolean allLengths, boolean squareProbabilities) {
		
		ArrayList<String> vertList = new ArrayList<String>(graph.vertexSet());
		
		// Create initial probability vector
		OpenMapRealMatrix initProb = new OpenMapRealMatrix(1, graph.vertexSet().size());
		
		// Initialize initial probability vector
		for (int i = 0; i < vertList.size(); i++)
			if (vertList.get(i).equals(initialVertex))
				initProb.setEntry(0, i, 1d);
			else
				initProb.setEntry(0, i, 0d);
		
		// Create transition probability matrix
		OpenMapRealMatrix transProbMatrix = new OpenMapRealMatrix(graph.vertexSet().size(), graph.vertexSet().size());
		
		// Initialize transition probability matrix
		for (int i = 0; i < vertList.size(); i++)
			for (int j = 0; j < vertList.size(); j++)
				if (graph.containsEdge(vertList.get(i), vertList.get(j)))
					transProbMatrix.setEntry(i, j, 1d / (double)graph.outDegreeOf(vertList.get(i)));
				else
					transProbMatrix.setEntry(i, j, 0d);
		
		// Iterate updating powers of transition probability matrix
		ArrayList<OpenMapRealMatrix> allLengthProbMatrices = new ArrayList<>();
		OpenMapRealMatrix matrixMultiplier = transProbMatrix;
		if (allLengths)
			allLengthProbMatrices.add(new OpenMapRealMatrix(initProb.multiply(transProbMatrix)));
		for (int len = 2; len <= walkLength; len++) {
			matrixMultiplier = (OpenMapRealMatrix)matrixMultiplier.multiply(transProbMatrix);
			if (allLengths)
				allLengthProbMatrices.add(initProb.multiply(matrixMultiplier));
		}
		
		/* Compute entropy of the distribution (\pi(v_0),\pi(v_1),...,\pi(v_{2n-1})),
		 * where \pi(v_i) is the probability that a walkLength-length walk starting at vertex v_0
		 * ends at vertex v_i  
		 */
		
		double entropySum = 0d;
		
		OpenMapRealMatrix finalProb = initProb.multiply(matrixMultiplier);
		
		for (int i = 0; i < vertList.size(); i++) {
			double prob = 0d;
			if (allLengths) {
				for (int l = 0; l < walkLength; l++)
					prob += allLengthProbMatrices.get(l).getEntry(0, i) * (Math.pow(2d, (double)(l + 1)) / (Math.pow(2d, walkLength) - 1d));
			}
			else 
				prob = finalProb.getEntry(0, i);
			if (squareProbabilities)
				prob *= prob;
			if (prob > 0d)
				entropySum += -(prob * Math.log(prob));
		}
		
		return entropySum;
	}
	
	public static List<Double> entropyVector(DefaultDirectedGraph<String, DefaultEdge> graph, int walkLength, String initialVertex) {
		
		ArrayList<String> vertList = new ArrayList<String>(graph.vertexSet());
		
		// Create initial probability vector
		OpenMapRealMatrix initProb = new OpenMapRealMatrix(1, graph.vertexSet().size());
		
		// Initialize initial probability vector
		for (int i = 0; i < vertList.size(); i++)
			if (vertList.get(i).equals(initialVertex))
				initProb.setEntry(0, i, 1d);
			else
				initProb.setEntry(0, i, 0d);
		
		// Create transition probability matrix
		OpenMapRealMatrix transProbMatrix = new OpenMapRealMatrix(graph.vertexSet().size(), graph.vertexSet().size());
		
		// Initialize transition probability matrix
		for (int i = 0; i < vertList.size(); i++)
			for (int j = 0; j < vertList.size(); j++)
				if (graph.containsEdge(vertList.get(i), vertList.get(j)))
					transProbMatrix.setEntry(i, j, 1d / (double)graph.outDegreeOf(vertList.get(i)));
				else
					transProbMatrix.setEntry(i, j, 0d);
		
		// Iterate updating powers of transition probability matrix
		ArrayList<OpenMapRealMatrix> allLengthProbMatrices = new ArrayList<>();
		OpenMapRealMatrix matrixMultiplier = transProbMatrix;
		allLengthProbMatrices.add(new OpenMapRealMatrix(initProb.multiply(transProbMatrix)));
		for (int len = 2; len <= walkLength; len++) {
			matrixMultiplier = (OpenMapRealMatrix)matrixMultiplier.multiply(transProbMatrix);
			allLengthProbMatrices.add(initProb.multiply(matrixMultiplier));
		}
		
		/* Compute entropy of the distribution (\pi(v_0),\pi(v_1),...,\pi(v_{2n-1})),
		 * where \pi(v_i) is the probability that a walkLength-length walk starting at vertex v_0
		 * ends at vertex v_i  
		 */
		
		
		List<Double> vector = new ArrayList<>();
		
		for (int l = 0; l < walkLength; l++) {
			double entropySum = 0d;
			for (int i = 0; i < vertList.size(); i++) {
				double prob = allLengthProbMatrices.get(l).getEntry(0, i) * (Math.pow(2d, (double)(l + 1)) / (Math.pow(2d, walkLength) - 1d));
				if (prob > 0d)
					entropySum += -(prob * Math.log(prob));
			}
			vector.add(entropySum);
		}
		
		return vector;
	}

}
