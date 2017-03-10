package protocol_specific_calculators;

import graph_mafia_calculator.FastGraphDBMafiaCalculator;

public class ModularCalculator extends FastGraphDBMafiaCalculator {

	int h;

	public ModularCalculator(int h) {
		// TODO Auto-generated constructor stub
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
		int level = vertex / h;
		return (level + 1) * h + (2 * vertex + edgeLabel) % h;
	}

	public static void main(String[] args) {

		int n = 6, h = 4;

		ModularCalculator m = new ModularCalculator(h);
		System.out.println(m.mafia(n));

	}

}
