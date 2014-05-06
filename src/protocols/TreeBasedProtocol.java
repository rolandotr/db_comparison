package protocols;

import java.math.BigDecimal;
import java.math.BigInteger;

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
		if (depth >= n) return ONE;//because this is not the correct protocol for this n, but depth = 0.
		if (depth == 0) return computeMyFar(n);
		int realDepth = (depth > n)?n:depth;
		BigDecimal p = computeMyFar(realDepth);
		BigDecimal p1 = p.pow(n/realDepth);
		return p1;

	}

	@Override
	public int getYearOfPublication() {
		return 2009;
	}

	public BigDecimal computeMyFar(int k) {
		BigDecimal a = new BigDecimal(0.5d);
		a = a.pow(k);
		return a.multiply(new BigDecimal((double)k/2+1));
	}
	private BigDecimal distanceFraudUpperBound(int n){
		BigDecimal half = new BigDecimal(0.5d);
		BigDecimal p = new BigDecimal(1);
		for (int i = 1; i <= n; i++){
			p = p.multiply(half.add(half.pow(i+1)));
		}
		BigDecimal four = new BigDecimal(4d);
		BigDecimal sqrt = half.pow(2*n).add(((four).multiply(half.pow(n))).negate()).add(four.multiply(p));
		BigDecimal result = half.pow(n);
		double tt = (result.doubleValue() + Math.sqrt(sqrt.doubleValue()))/2;
		return new BigDecimal(tt);
	}

	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		if (depth >= n) return ONE;//because this is not the correct protocol for this n, but depth = 0.
		int realDepth = (depth > n)?n:depth;
		if (realDepth == 4)
			return new BigDecimal(Math.pow(0.28, n/4));
		if (realDepth == 2) return new BigDecimal(Math.pow(0.5625, n/2));
		if (realDepth == 3) return new BigDecimal(Math.pow(0.4, n/3));
		if (realDepth == 1) return new BigDecimal(Math.pow(0.75, n));		
		BigDecimal result;
		if (depth == 0) {
			return distanceFraudUpperBound(n);
		}
		else {
			result = distanceFraudUpperBound(realDepth);
			return result.pow(n/realDepth);
		}
		
		/*if (depth == 0) {
			TreeBasedDistanceFraudSimulator sim = new TreeBasedDistanceFraudSimulator();
			return new BigDecimal(""+sim.computeDistanceFraud(n));
		}
		else{
			int realDepth = (depth > n)?n:depth;
			TreeBasedDistanceFraudSimulator sim = new TreeBasedDistanceFraudSimulator();
			BigDecimal p = new BigDecimal(""+sim.computeDistanceFraud(depth));
			BigDecimal p1 = p.pow(n/realDepth);
			return p1;
		}*/
	}

	@Override
	public BigDecimal getTerroristFraudProbability(int n) {
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
	public long getMemory(int n) {
		//System.out.println("Depth = "+depth+", n = "+n);
		if (depth == 0){
			BigInteger tmp = new BigInteger(2+"");
			tmp = tmp.pow(n+1);
			tmp = tmp.add(new BigInteger((getTotalBitsExchanged(n)-2)+""));
			//dealing with numerical errors
			if (tmp.bitLength() > 63) return Long.MAX_VALUE;
			return tmp.longValue();
		}else{
			int realDepth = (depth > n)?n:depth;
			BigInteger tmp = new BigInteger(2+"");
			tmp = tmp.pow(realDepth+1);
			BigInteger minusTwo = new BigInteger(2+"");
			minusTwo = minusTwo.negate();
			tmp = tmp.add(minusTwo);
			tmp = tmp.multiply(new BigInteger((n/realDepth)+""));
			tmp = tmp.add(new BigInteger((getTotalBitsExchanged(n))+""));
			//dealing with numerical errors
			if (tmp.bitLength() > 63) return Long.MAX_VALUE;
			return tmp.longValue();
		}
	}

	@Override
	public int getCryptoCalls() {
		return 1;
	}

	/*Trujillo- Mar 25, 2014
	 * depth = 0 is the first of them. After that we take 1, 2, 4, etc*/
	@Override
	public DBProtocol[] getAllInstances(int e, int factor) {
		if (e < 2) return null;
		int max = (e/2 >= factor)?factor:e/2;
		DBProtocol[] result = new DBProtocol[max];
		for (int i = 0; i < max; i++) {
			result[i] = new TreeBasedProtocol(i, SIZE_OF_NONCES);
		}
		return result;
	}

	@Override
	public DBProtocol[] getDefaultInstances(int e) {
		if (e >= 24){
			return new DBProtocol[]{
					new TreeBasedProtocol(0, SIZE_OF_NONCES),
					new TreeBasedProtocol(2, SIZE_OF_NONCES),
					new TreeBasedProtocol(4, SIZE_OF_NONCES),
					new TreeBasedProtocol(6, SIZE_OF_NONCES),
					new TreeBasedProtocol(8, SIZE_OF_NONCES),
					new TreeBasedProtocol(12, SIZE_OF_NONCES),
			};
		}
		if (e >= 16){
			return new DBProtocol[]{
					new TreeBasedProtocol(0, SIZE_OF_NONCES),
					new TreeBasedProtocol(2, SIZE_OF_NONCES),
					new TreeBasedProtocol(4, SIZE_OF_NONCES),
					new TreeBasedProtocol(6, SIZE_OF_NONCES),
					new TreeBasedProtocol(8, SIZE_OF_NONCES),
			};
		}
		if (e >= 12){
			return new DBProtocol[]{
					new TreeBasedProtocol(0, SIZE_OF_NONCES),
					new TreeBasedProtocol(2, SIZE_OF_NONCES),
					new TreeBasedProtocol(4, SIZE_OF_NONCES),
					new TreeBasedProtocol(6, SIZE_OF_NONCES),
			};
		}
		if (e >= 8){
			return new DBProtocol[]{
					new TreeBasedProtocol(0, SIZE_OF_NONCES),
					new TreeBasedProtocol(2, SIZE_OF_NONCES),
					new TreeBasedProtocol(4, SIZE_OF_NONCES),
			};
		}
		if (e >= 4){
			return new DBProtocol[]{
					new TreeBasedProtocol(0, SIZE_OF_NONCES),
					new TreeBasedProtocol(2, SIZE_OF_NONCES),
			};
		}
		else {
			return new DBProtocol[]{
					new TreeBasedProtocol(0, SIZE_OF_NONCES),
			};
		}
	}

	@Override
	public String getIdentifier() {
		return "Tree-"+depth;
	}

	@Override
	public boolean lackSecurityProof() {
		return true;
	}

}
