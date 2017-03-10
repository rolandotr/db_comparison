package protocol_specific_calculators;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import graph_mafia_calculator.FastGraphDBMafiaCalculator;

public class RandomCayleyCalculator extends FastGraphDBMafiaCalculator {
	
	int n, initVert, step1, step2;
	
	public RandomCayleyCalculator(int halfOrderLowerSize, int halfOrderUpperSize) {
		SecureRandom random = new SecureRandom();
		n = random.nextInt(halfOrderUpperSize - halfOrderLowerSize) + halfOrderLowerSize;
		initVert = random.nextInt(2 * n);
		step1 = random.nextInt(n) + 1;
		step2 = random.nextInt(n) + 1;
		while (step2 == step1)
			step2 = random.nextInt(n) + 1;
	}

	@Override
	protected int getInitialVertex() {
		return initVert;
	}

	@Override
	protected int getNeighbor(int vertex, int edgeLabel) {
		if (edgeLabel == 0)
			return (vertex + step1) % (2 * n);
		else
			return (vertex + step2) % (2 * n);
	}
	
	protected int getN() {
		return n;
	}
	
	protected Set<Integer> getGenerator() {
		HashSet<Integer> gen = new HashSet<>();
		gen.add(step1); gen.add(step2);
		return gen;
	}

}
