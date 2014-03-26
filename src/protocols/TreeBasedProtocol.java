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

	public TreeBasedProtocol(){
		this(0, SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "tree-based";
	}

	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		if (depth == 0) return computeMyFar(n);
		int realDepth = (depth > n)?n:depth;
		BigDecimal p = computeMyFar(realDepth);
		BigDecimal p1 = p.pow(n/realDepth);
		return p1;

	}

	public BigDecimal computeMyFar(int k) {
		BigDecimal a = new BigDecimal(0.5d);
		a = a.pow(k);
		return a.multiply(new BigDecimal((double)k/2+1));
	}

	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		if (depth == 0) {
			TreeBasedDistanceFraudSimulator sim = new TreeBasedDistanceFraudSimulator();
			return new BigDecimal(""+sim.computeDistanceFraud(n));
		}
		else{
			int realDepth = (depth > n)?n:depth;
			TreeBasedDistanceFraudSimulator sim = new TreeBasedDistanceFraudSimulator();
			BigDecimal p = new BigDecimal(""+sim.computeDistanceFraud(depth));
			BigDecimal p1 = p.pow(n/realDepth);
			return p1;
		}
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

	/*Trujillo- Mar 25, 2014
	 * depth = 0 is the first of them. After that we take 1, 2, 4, etc*/
	@Override
	public DBProtocol[] getAllInstances(int factor) {
		DBProtocol[] result = new DBProtocol[factor];
		result[0] = new TreeBasedProtocol();
		int d = 1;
		for (int i = 1; i < factor; i++) {
			result[i] = new TreeBasedProtocol(d, SIZE_OF_NONCES);
			d += 1;
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "Tree_depth_"+depth;
	}

}
