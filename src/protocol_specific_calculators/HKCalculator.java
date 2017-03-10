package protocol_specific_calculators;

import java.math.BigDecimal;

import graph_mafia_calculator.FastGraphDBMafiaCalculator;

public class HKCalculator extends FastGraphDBMafiaCalculator{

	@Override
	protected int getInitialVertex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getNeighbor(int vertex, int edgeLabel) {
		// TODO Auto-generated method stub
		return vertex + 1 + edgeLabel + vertex % 2;
	}
	
	
	public static void main(String[] args) {
		
		int n = 6;
		HKCalculator hk = new HKCalculator();
		
		System.out.println(hk.mafia(n));
		
		//This is the proven theoretical mafia fraud prob
		System.out.println(BigDecimal.valueOf(.75).pow(n));
		
	}

}
