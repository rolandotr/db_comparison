package protocol_specific_calculators;


import java.math.BigDecimal;

import graph_mafia_calculator.FastGraphDBMafiaCalculator;

public class TreeBasedCalculator extends FastGraphDBMafiaCalculator{

	@Override
	protected int getInitialVertex() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	protected int getNeighbor(int vertex, int edgeLabel) {
		// TODO Auto-generated method stub
		return 2*vertex + edgeLabel + 1;
	}
	
	
	public static void main(String[] args) {
		
		int n = 6;
		
		TreeBasedCalculator tree = new TreeBasedCalculator();
		
		System.out.println(tree.mafia(n));
		
		//This is the proven theoretical mafia fraud prob
		System.out.println(BigDecimal.valueOf(.5).pow(n).multiply(BigDecimal.valueOf(n/2.0 + 1)));
		
	}
	
}
