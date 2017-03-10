package protocol_specific_calculators;

import graph_mafia_calculator.FastGraphDBMafiaCalculator;

public class PoulidorCalculator extends FastGraphDBMafiaCalculator {

	int n;

	public PoulidorCalculator(int n) {
		// TODO Auto-generated constructor stub
		this.n = n;
	}

	@Override
	protected int getInitialVertex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getNeighbor(int vertex, int edgeLabel) {
		// TODO Auto-generated method stub
		return (vertex + 1 + edgeLabel) % (2 * n);
	}

	public static void main(String[] args) {

		int n = 2;

		PoulidorCalculator p = new PoulidorCalculator(n);
		System.out.println(p.mafia(n));
	}

}
