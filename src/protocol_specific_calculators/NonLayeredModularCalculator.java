package protocol_specific_calculators;

import graph_mafia_calculator.FastGraphDBMafiaCalculator;

public class NonLayeredModularCalculator extends FastGraphDBMafiaCalculator {

	private int h;

	public NonLayeredModularCalculator(int h) {
		super();
		this.h = h;
	}

	@Override
	protected int getInitialVertex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getNeighbor(int vertex, int edgeLabel) {
		// TODO Auto-generated method stub
		return (2 * vertex + edgeLabel) % h;
	}

	public static void main(String[] args) {

		int n = 4, h = 2 * n;

		System.out.println(new NonLayeredModularCalculator(h).mafia(n));
		System.out.println(new PoulidorCalculator(n).mafia(n));

	}

}
