package protocol_specific_calculators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import graph_mafia_calculator.FastGraphDBMafiaCalculator;

public class GenericOutDegree2Calculator extends FastGraphDBMafiaCalculator {
	
	public class OutDegree2DigraphFamilyIterator {
		
		protected class OutDegree2VertInfo {
			
			private int ord, mod, first, second;
			
			public OutDegree2VertInfo(int o, int m) {
				ord = o;
				mod = m;
				first = (ord + 1) % mod;
				second = (ord + 2) % mod;
			}
			
			public OutDegree2VertInfo(int o, int m, int f, int s) {
				ord = o;
				mod = m;
				first = f;
				second = s;
			}
			
			boolean next() {
				second = (second + 1) % mod;
				if (second == ord) {
					first = (first + 1) % mod;
					if ((first + 1) % mod == ord) {
						first = (ord + 1) % mod;
						second = (ord + 2) % mod;
						return true;
					}
					else
						second = (first + 1) % mod;
				}
				return false;
			}
		}
		
		public int n;
		protected ArrayList<OutDegree2VertInfo> vertices;
		protected int iterateFromVertexOrd;
		boolean graphWasGenerated;
		DefaultDirectedGraph<String, DefaultEdge> graph;
		public long overallGraphCount;
		public long connectedGraphCount;
		public BigDecimal smallestMafiaSucccesProb;
		
		public OutDegree2DigraphFamilyIterator(int n) {
			this.n = n;
			vertices = new ArrayList<>();
			for (int i = 0; i < 2*n; i++)
				vertices.add(new OutDegree2VertInfo(i, 2*n));
			iterateFromVertexOrd = 0;
			graph = null;
			graphWasGenerated = false;
			overallGraphCount = 1;
			connectedGraphCount = 0;
			smallestMafiaSucccesProb = new BigDecimal(1.1);
		}
		
		public OutDegree2DigraphFamilyIterator(int n, int fixedFirst, int fixedSecond) {
			this.n = n;
			vertices = new ArrayList<>();
			vertices.add(new OutDegree2VertInfo(0, 2*n, fixedFirst, fixedSecond));
			for (int i = 1; i < 2*n; i++)
				vertices.add(new OutDegree2VertInfo(i, 2*n));
			iterateFromVertexOrd = 1;
			graph = null;
			graphWasGenerated = false;
			overallGraphCount = 1;
			connectedGraphCount = 0;
			smallestMafiaSucccesProb = new BigDecimal(1.1);
		}
		
		public OutDegree2DigraphFamilyIterator(String filePath, int defN, int defIterFrom, int defFixedFst, int defFixedSec) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(filePath));
				reader.readLine();   // Skip first line of the file, which only contains human-oriented info
				overallGraphCount = Integer.parseInt(reader.readLine());
				boolean thisIsConnected = (Integer.parseInt(reader.readLine()) == 1);
				if (thisIsConnected)
					connectedGraphCount = Integer.parseInt(reader.readLine()) - 1;
				else
					connectedGraphCount = Integer.parseInt(reader.readLine());
				n = Integer.parseInt(reader.readLine());
				iterateFromVertexOrd = Integer.parseInt(reader.readLine());
				vertices = new ArrayList<>();
				for (int i = 0; i < 2*n; i++) {
					String[] verts = reader.readLine().split(" ");
					if (verts.length == 2)
						vertices.add(new OutDegree2VertInfo(i, 2*n, Integer.parseInt(verts[0]), Integer.parseInt(verts[1])));
					else {
						reader.close();
						throw new IOException();
					}
				}
				smallestMafiaSucccesProb = new BigDecimal(reader.readLine());
				reader.close();
				graph = null;
				graphWasGenerated = false;
			}
			catch (IOException ioEx) {
				// This will be the default family iterator
				n = defN;
				iterateFromVertexOrd = defIterFrom;
				vertices = new ArrayList<>(); 
				if (iterateFromVertexOrd == 1) {
					vertices.add(new OutDegree2VertInfo(0, 2*n, defFixedFst, defFixedSec));
					for (int i = 1; i < 2*n; i++)
						vertices.add(new OutDegree2VertInfo(i, 2*n));
				}
				else {
					for (int i = 0; i < 2*n; i++)
						vertices.add(new OutDegree2VertInfo(i, 2*n));
				}
				graph = null;
				graphWasGenerated = false;
				overallGraphCount = 1;
				connectedGraphCount = 0;
				smallestMafiaSucccesProb = new BigDecimal(1.1);
			}
		}
		
		public boolean next() {
			int i = iterateFromVertexOrd;
			for (; i < 2*n && vertices.get(i).next(); i++);
			overallGraphCount++;
			graphWasGenerated = false;
			return (i < 2*n);
		}
		
		@Override
		public String toString() {
			String representation = "[";
			if (vertices.size() > 0)
				representation += "0->(" + vertices.get(0).first + "," + vertices.get(0).second + ")";
			for (int i = 1; i < vertices.size(); i++)
				representation += ", " + i + "->(" + vertices.get(i).first + "," + vertices.get(i).second + ")";
			return representation + "]";
		}
		
		public void saveCurrentState (String filePath) {
			try {
				FileWriter file;
				file = new FileWriter(filePath, false);
				long overallGraphTotal = (long)Math.pow(2*n*n - 3*n + 1, 2*n - iterateFromVertexOrd);
				file.write(overallGraphCount + "/" + overallGraphTotal + " (" + (100 * (double)overallGraphCount / (double)overallGraphTotal) + " %), " + connectedGraphCount + " connected" + System.getProperty("line.separator"));
				file.write(familyIterator.overallGraphCount + System.getProperty("line.separator"));
				if (graph != null && graph.vertexSet().size() == 2*n)
					file.write("1" + System.getProperty("line.separator"));
				else
					file.write("0" + System.getProperty("line.separator"));
				file.write(familyIterator.connectedGraphCount + System.getProperty("line.separator"));
				file.write(familyIterator.n + System.getProperty("line.separator"));
				file.write(familyIterator.iterateFromVertexOrd + System.getProperty("line.separator"));
				for (int i = 0; i < familyIterator.vertices.size(); i++)
					file.write(familyIterator.vertices.get(i).first + " " + familyIterator.vertices.get(i).second + System.getProperty("line.separator"));
				file.write(familyIterator.smallestMafiaSucccesProb.toString() + System.getProperty("line.separator"));
				file.close();
			} catch (IOException e) {
				System.err.println("Tried to save status but error occurred");
			}
		}
		
		public DefaultDirectedGraph<String, DefaultEdge> generateGraph() {
			if (graphWasGenerated)
				return graph;
			else {
				graph = new DefaultDirectedGraph<>(DefaultEdge.class);
				graph.addVertex("0");
				Set<Integer> allGenerated = new HashSet<>(), lastGenerated = new HashSet<>();
				allGenerated.add(0);
				lastGenerated.add(0);
				while (lastGenerated.size() != 0) {
					Set<Integer> newlyGenerated = new HashSet<>();
					for (Integer elem : lastGenerated) {
						int gen = this.vertices.get(elem).first;
						if (!graph.containsVertex(gen + ""))
							graph.addVertex(gen + "");
						if (!graph.containsEdge(elem.toString(), gen + ""))
							graph.addEdge(elem.toString(), gen + "");
						if (!allGenerated.contains(gen)) {	
							newlyGenerated.add(gen);
							allGenerated.add(gen);
						}
						gen = this.vertices.get(elem).second;
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
				graphWasGenerated = true;
				if (graph.vertexSet().size() == 2*n)
					connectedGraphCount++;
				return graph;
			}
		}
	}
	
	public OutDegree2DigraphFamilyIterator familyIterator;
	
	public GenericOutDegree2Calculator (int n) {
		familyIterator = new OutDegree2DigraphFamilyIterator(n);
	}
	
	public GenericOutDegree2Calculator (String filePath, int n, int from, int fst, int sec) {
		familyIterator = new OutDegree2DigraphFamilyIterator(filePath, n, from, fst, sec);
	}

	@Override
	protected int getInitialVertex() {
		return 0;
	}

	@Override
	protected int getNeighbor(int vertex, int edgeLabel) {
		if (edgeLabel == 0)
			return familyIterator.vertices.get(vertex).first;
		else
			return familyIterator.vertices.get(vertex).second;
	}
	
}
