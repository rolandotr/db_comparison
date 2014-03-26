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

	
	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		PoulidorDistanceFraudSimulator sim = new PoulidorDistanceFraudSimulator();
		return new BigDecimal(""+sim.computeDistanceFraud(n));
	}

	@Override
	public BigDecimal getTerroritFraudProbability(int n) {
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
	public int getMemory(int n) {
		return getTotalBitsExchanged(n)+4*n;
	}

	@Override
	public int getMinimumNumberOfCryptoCalls() {
		return 1;
	}

	@Override
	public DBProtocol[] getAllInstances(int factor) {
		return new DBProtocol[]{new PoulidorProtocol()};
	}

	@Override
	public String getIdentifier() {
		return "Poulidor";
	}

}
