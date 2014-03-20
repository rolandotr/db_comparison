package protocols;

import java.math.BigDecimal;

public class TreeBasedProtocol extends DBProtocol{

	private int sizeOfNonces;
	
	
	/*Trujillo- Mar 7, 2014
	 * This value define the depth of the trees. When depth = 0, a single tree
	 * is assumed. Otherwise, n/depth trees are considered*/
	private int depth;
	
	public TreeBasedProtocol(int depth, int sizeOfNonces){
		this.depth = depth;
		this.sizeOfNonces = sizeOfNonces;
	}

	@Override
	public String getAcronym() {
		return "tree-based";
	}

	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		if (depth == 0) return computeMyFar(n);
		BigDecimal p = computeMyFar(depth);
		BigDecimal p1 = p.pow(n/depth);
		return p1;

	}

	public BigDecimal computeMyFar(int k) {
		BigDecimal a = new BigDecimal(0.5d);
		a = a.pow(k);
		return a.multiply(new BigDecimal((double)k/2+1));
	}

	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		TreeBasedDistanceFraudSimulator sim = new TreeBasedDistanceFraudSimulator();
		return new BigDecimal(""+sim.computeDistanceFraud(n));
	}

	@Override
	public BigDecimal getTerroritFraudProbability(int n) {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return false;
	}

	/*Trujillo- Mar 14, 2014
	 * In the protocol, the prover also sends some bits from the hash value.
	 * However, we consider here such a value negligible.*/
	@Override
	public int getTotalBitsExchanged(int n) {
		return 2*sizeOfNonces;
	}

	@Override
	public boolean hasMultipleBitExchange() {
		return false;
	}

	@Override
	public int getMemory(int n) {
		if (depth == 0)
			return getTotalBitsExchanged(n)+(int)Math.pow(2, n+1)-2;
		else return getTotalBitsExchanged(n)+(n/depth)*((int)Math.pow(2, depth+1)-2);

	}

	@Override
	public int getMinimumNumberOfCryptoCalls() {
		return 1;
	}

}
