package protocol_specific_calculators;

import java.util.HashSet;
import java.util.Set;
import graph_mafia_calculator.FastGraphDBMafiaCalculator;

public class GenericCayleyCalculator extends FastGraphDBMafiaCalculator {
	
	int n, step1, step2;
	
	public GenericCayleyCalculator(int ord, int s1, int s2) {
		n = ord;
		step1 = s1;
		step2 = s2;
	}

	@Override
	protected int getInitialVertex() {
		return 0;
	}

	@Override
	protected int getNeighbor(int vertex, int edgeLabel) {
		if (edgeLabel == 0)
			return (vertex + step1) % (2 * n);
		else
			return (vertex + step2) % (2 * n);
	}
	
	public int getN() {
		return n;
	}
	
	protected Set<Integer> getGenerator() {
		HashSet<Integer> gen = new HashSet<>();
		gen.add(step1); gen.add(step2);
		return gen;
	}
	
	public class CayleyInfo implements Comparable<CayleyInfo> {
		
		public int n, i, j;
		
		public CayleyInfo(int n, int i, int j) {
			this.n = n;
			this.i = i;
			this.j = j;
		}
		
		@Override
		public int compareTo(CayleyInfo ci) {
			if (this.n < ci.n)
				return -1;
			if (this.n > ci.n)
				return 1;
			if (this.i < ci.i)
				return -1;
			if (this.i > ci.i)
				return 1;
			if (this.j < ci.j)
				return -1;
			if (this.j > ci.j)
				return 1;
			return 0;
		}
	}
	
}
