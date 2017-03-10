package random_cayley;

import java.util.ArrayList;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class GenerateCayleyCollection {

	public static void main(String[] args) {
		
		if (args.length == 4) {
		
			RandomCayleyGraphGenerator cayleyGen = new RandomCayleyGraphGenerator(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
			
			for (int i = 0; i < Integer.parseInt(args[0]); i++) {				
				
				DefaultDirectedGraph<String, DefaultEdge> graph = cayleyGen.nextGraph();
				
				System.out.println("Graph:");
				System.out.println(graph.toString());
				printSymmeticEdges(graph);
			}
		}
		else
			System.out.println("Expected 4 parameters");
	}
	
	static void printSymmeticEdges(DefaultDirectedGraph<String, DefaultEdge> graph) {
		ArrayList<String> info = new ArrayList<>();
		ArrayList<String> vertList = new ArrayList<>(graph.vertexSet());
		for (int i = 0; i < vertList.size() - 1; i++)
			for (int j = i + 1; j < vertList.size(); j++)
				if (graph.containsEdge(vertList.get(i), vertList.get(j)) && graph.containsEdge(vertList.get(j), vertList.get(i)))
					info.add("(" + vertList.get(i).toString() + "<->" + vertList.get(j) + ")");
		System.out.println("Symmetic edges: " + info.toString());
	}

}
