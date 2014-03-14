package protocols;

import java.math.BigDecimal;

public class KimAndAvoineProtocol extends DBProtocol{

	private double pd;
	private int sizeOfNonces;
	
	public KimAndAvoineProtocol(double pd, int sizeOfNonces){
		this.pd = pd;
		this.sizeOfNonces = sizeOfNonces;
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
	public BigDecimal getTerroritFraudProbability(int n) {
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

}
