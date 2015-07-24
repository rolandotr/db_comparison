package protocols.specifications;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TreeBasedProtocol extends DBProtocol{

	private static final long serialVersionUID = -2997318102832049477L;
	private int sizeOfNonces;
	protected int depth;
	
	public static void main(String[] args) {
		TreeBasedProtocol t = new TreeBasedProtocol();
		for (int i = 1; i <= 256; i++) {
			t.setNumberOfRounds(i);
			System.out.println("n = "+i+" => resistance = "+t.computeMyFar(i));
		}
	}
	
	public TreeBasedProtocol(int depth, int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
		this.depth = depth;
	}

	public TreeBasedProtocol(int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
		this.depth = 1;
	}

	public TreeBasedProtocol(){
		this(1, SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "tree-based";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		int trees = n/depth;
		BigDecimal p = computeMyFar(depth);
		BigDecimal p1 = p.pow(trees);
		return p1;

	}

	@Override
	public DBProtocol getInstance() {
		return new TreeBasedProtocol(depth, SIZE_OF_NONCES);
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
	public BigDecimal getDistanceFraudProbability() {
		BigDecimal result = distanceFraudUpperBound(depth);
		return result.pow(n/depth);
	}

	@Override
	public BigDecimal getTerroristFraudProbability() {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return false;
	}


	@Override
	public int getCryptoCalls() {
		return 1;
	}

	@Override
	public DBProtocol[] getInstances() {
		TreeBasedProtocol[] result = new TreeBasedProtocol[MAX_N*(32-2+1)];
		for (int i = 0; i < MAX_N; i++){
			for (int depth = 2; depth <= 32; depth++) {
				int index = i*(32-2+1) + depth-2; 
				result[index] = new TreeBasedProtocol();
				result[index].setNumberOfRounds(i+1);
				result[index].depth = depth;
			}
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "Tree-{"+n+", "+depth+"}";
	}
	

	@Override
	public boolean lackSecurityProof() {
		return true;
	}

	@Override
	public int getSizeOfTheChannel() {
		return 1;
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return sizeOfNonces+n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		BigInteger tmp = new BigInteger(2+"");
		tmp = tmp.pow(depth+1);
		BigInteger minusTwo = new BigInteger(2+"");
		minusTwo = minusTwo.negate();
		tmp = tmp.add(minusTwo);
		tmp = tmp.multiply(new BigInteger((n/depth)+""));
		//dealing with numerical errors
		if (tmp.bitLength() > 30) return Integer.MAX_VALUE/2;
		return tmp.intValue();
	}
	

	@Override
	public int getNumberOfNoncesGenerated() {
		return 1;
	}

	@Override
	public int getBitsGenerated() {
		return 0;
	}
	
}
