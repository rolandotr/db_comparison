package random_cayley;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class RandomCayleyGraphGenerator {
	
	protected SecureRandom random;
	protected int minVertCount;
	protected int maxVertCount;
	protected int genSize;
	
	public RandomCayleyGraphGenerator(int minVert, int maxVert, int genSz) {
		random = new SecureRandom();
		minVertCount = minVert;
		maxVertCount = maxVert;
		genSize = genSz;
	}
	
	public DefaultDirectedGraph<String, DefaultEdge> nextGraph() {
		int n = random.nextInt(maxVertCount - minVertCount) + minVertCount;
		System.out.println("n = " + n + " (graph order " + 2*n + ")");
		int start = random.nextInt(n);
		System.out.println("Starting element: " + start);
		Set<Integer> generator = new HashSet<>();
		int gen = random.nextInt(n) + 1;
		generator.add(gen);
		while (generator.size() < genSize) {
			gen = random.nextInt(n) + 1;
			while (generator.contains(gen))
				gen = random.nextInt(n) + 1;
			generator.add(gen);
		}
		System.out.println("Generator: " + generator.toString());
		return CayleyGraphBuilderIntegerGruop.buildGraph(start, generator, new IntegerSumModK(2*n));
	}
	
}
