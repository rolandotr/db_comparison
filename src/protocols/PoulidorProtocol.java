package protocols;

import java.math.BigDecimal;
import java.math.BigInteger;

import utils.Combinatory;

public class PoulidorProtocol extends DBProtocol{

	
	private int sizeOfNonces;
	
	public PoulidorProtocol(int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
	}
	
	public PoulidorProtocol(){
		this(SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "Poulidor";
	}

	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		BigDecimal half = new BigDecimal(0.5);
		BigDecimal result = half.pow(n);
		for (int i = 1; i <= n; i++){
			result = result.add((half.pow(i)).multiply(maxProb2(i, n)));
		}
		return result;
	}
	@Override
	public int getYearOfPublication() {
		return 2010;
	}

	private BigDecimal maxProb2(int t, int n) {
		BigDecimal result = new BigDecimal(1);
		//System.out.println("m = "+m);
		for (int i = t; i <= n; i++){
			//System.out.println("best = "+ index+" prob = "+max);
			result = result.multiply(prob(i, i, t));
		}
		return result;
	}
	private BigDecimal prob(int i, int j, int t) {
		if (i < t && i == j) return new BigDecimal(1);
		if (i < t && i != j) return new BigDecimal(0.5);
		if (i >= t && j < t) return new BigDecimal(0.5);
		i = i-t+1;
		j = j-t+1;
		BigDecimal half = new BigDecimal(0.5);
		BigInteger sum = new BigInteger(0+"");
		int max = (i > j)?i:j;
		for (int k = 1; k <= 2*max+2; k++){
			sum = sum.add(walks(1, k, i-1).multiply(walks(2, k, j-1)));
			sum = sum.add(walks(2, k, i-1).multiply(walks(1, k, j-1)));
		}
		BigDecimal result = half.pow(i+j).multiply(new BigDecimal(sum.doubleValue()));
		result = result.add(half);
		return result;
	}

	private BigInteger walks(int i, int k, int j) {
		if (j == 0){
			if (i == k) return new BigInteger(1+"");
			else return new BigInteger(0+"");
		}
		if (2*j < k - i) return new BigInteger(0+"");
		if (k < i+j) return new BigInteger(0+"");
		return Combinatory.comb(j, k-i-j);
	}

	
	private BigDecimal computeProb2(int j, int i){
		BigInteger den = (new BigInteger(""+2)).pow(i+j);
		BigInteger t1, t2;
		BigDecimal result = new BigDecimal(0+"");
		int max = (i > j)?i:j;
		for (int k = max; k <= 2*max; k++){
			if (k-i > i) break;
			if (k-j > j) break;
			t1 = Combinatory.comb(i, k-i);
			t2 = Combinatory.comb(j, k-j);
			result = result.add(new BigDecimal((t1.multiply(t2))));
		}
		result = result.divide(new BigDecimal(den));
		return result;
	}

	
	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		if (n == 0) return BigDecimal.ONE;
		if (n == 1) return new BigDecimal("0.75");
		double p  = 1;
		for (int i = 1; i <= n; i++){
			p = p*(0.5 + computeProb2(i, i).doubleValue()/2);
		}
		BigDecimal half = new BigDecimal(0.5d);
		BigDecimal four = new BigDecimal(4d);
		BigDecimal sqrt = half.pow(2*n).add(((four).multiply(half.pow(n))).negate()).add(four.multiply(new BigDecimal(p)));
		BigDecimal result = half.pow(n);
		return new BigDecimal(result.doubleValue() + Math.sqrt(sqrt.doubleValue())/2);
		/*PoulidorDistanceFraudSimulator sim = new PoulidorDistanceFraudSimulator();
		return new BigDecimal(""+sim.computeDistanceFraud(n));*/
	}

	@Override
	public BigDecimal getTerroristFraudProbability(int n) {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return true;
	}

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
		return getTotalBitsExchanged(n)+4*n;
	}

	@Override
	public int getCryptoCalls() {
		return 1;
	}

	@Override
	public DBProtocol[] getAllInstances(int factor) {
		return new DBProtocol[]{new PoulidorProtocol()};
	}

	@Override
	public DBProtocol[] getDefaultInstances() {
		return new DBProtocol[]{new PoulidorProtocol()};
	}

	@Override
	public String getIdentifier() {
		return "Poulidor";
	}

}
