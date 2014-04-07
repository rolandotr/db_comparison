package protocols;

import java.math.BigDecimal;

public class KimAndAvoineProtocol extends DBProtocol{

	private double pd;
	private int sizeOfNonces;
	
	public KimAndAvoineProtocol(double pd, int sizeOfNonces){
		this.pd = pd;
		this.sizeOfNonces = sizeOfNonces;
	}
	
	public KimAndAvoineProtocol(){
		this(0, SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "KA2";
	}

	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		BigDecimal result = new BigDecimal("0.75");
		int alpha = (int)(n*pd);
		result = result.pow(n-alpha);
		BigDecimal half = new BigDecimal(0.5d);
		BigDecimal tmp = half.pow(alpha);
		result = result.multiply(tmp);
		tmp = half.pow(n+1);
		tmp = tmp.multiply(new BigDecimal(alpha));
		result = result.add(tmp);
		return result;
	}

	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		BigDecimal a = new BigDecimal("0.75");
		BigDecimal b = new BigDecimal(""+pd/4);
		return (a.add(b)).pow(n);
	}

	@Override
	public BigDecimal getTerroristFraudProbability(int n) {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return false;
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
		return getTotalBitsExchanged(n)+2*n;
	}

	@Override
	public int getCryptoCalls() {
		return 1;
	}

	@Override
	public DBProtocol[] getAllInstances(int factor) {
		double pd = 0;
		DBProtocol[] result = new DBProtocol[factor+1];
		for (int i = 0; i <= factor; i++) {
			/*Trujillo- Mar 24, 2014
			 * this is computed in that way to avoid numerical error.*/
			pd = 1 - (factor-i)/(double)factor;
			result[i] = new KimAndAvoineProtocol(pd, SIZE_OF_NONCES);
			if (pd > 1) pd = 1;
		}
		return result;
	}

	@Override
	public DBProtocol[] getDefaultInstances() {
		return new DBProtocol[]{
				new KimAndAvoineProtocol(0, SIZE_OF_NONCES),
				new KimAndAvoineProtocol(0.2, SIZE_OF_NONCES),
				new KimAndAvoineProtocol(0.4, SIZE_OF_NONCES),
				new KimAndAvoineProtocol(0.5, SIZE_OF_NONCES),
				new KimAndAvoineProtocol(0.6, SIZE_OF_NONCES),
				new KimAndAvoineProtocol(0.8, SIZE_OF_NONCES),
				new KimAndAvoineProtocol(1, SIZE_OF_NONCES),
		};
	}
	@Override
	public String getIdentifier() {
		return "KA-"+pd;
	}

	@Override
	public int getYearOfPublication() {
		return 2011;
	}

}
