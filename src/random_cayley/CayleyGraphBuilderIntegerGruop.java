package random_cayley;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public abstract class CayleyGraphBuilderIntegerGruop {
		
	public static DefaultDirectedGraph<String, DefaultEdge> buildGraph(Integer startElement, Set<Integer> generator, IntegerGroupOperation operation) {
		
		DefaultDirectedGraph<String, DefaultEdge> digraph = new DefaultDirectedGraph<>(DefaultEdge.class);
		
		Set<Integer> lastCreatedElements = new HashSet<>();
		Set<Integer> allCreatedElements = new HashSet<>();
		
		digraph.addVertex(startElement.toString());
		lastCreatedElements.add(startElement);
		allCreatedElements.add(startElement);
		
		while (lastCreatedElements.size() > 0) {
			Set<Integer> newlyCreatedElements = new HashSet<>();
			for (Integer existingElem : lastCreatedElements)
				for (Integer genElem : generator) {
					
					Integer newElem = operation.compute(existingElem, genElem);
					
					if (!digraph.containsVertex(newElem.toString()))
						digraph.addVertex(newElem.toString());
					
					if (!digraph.containsEdge(existingElem.toString(), newElem.toString()))
						digraph.addEdge(existingElem.toString(), newElem.toString());
					
					if (!allCreatedElements.contains(newElem)) {
						newlyCreatedElements.add(newElem);
						allCreatedElements.add(newElem);
					}
				}
			lastCreatedElements = newlyCreatedElements;
		}
		return digraph;
	}
}
