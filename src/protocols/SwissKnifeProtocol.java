package protocols;

import java.math.BigDecimal;

public class SwissKnifeProtocol extends DBProtocol{

	private int sizeOfSecret;
	private int sizeOfNonces;
	
	public SwissKnifeProtocol(int sizeOfSecret, int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
		this.sizeOfSecret = sizeOfSecret;
	}
	@Override
	public String getAcronym() {
		return "Swiss-knife";
	}

	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		return THREE_OVER_FOUR.pow(n);
	}

	@Override
	public BigDecimal getTerroritFraudProbability(int n) {
		return THREE_OVER_FOUR.pow(n);
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return true;
	}

	@Override
	public int getTotalBitsExchanged(int n) {
		return (sizeOfSecret+sizeOfNonces)*2+n;
	}
	@Override
	public boolean hasMultipleBitExchange() {
		return false;
	}
	@Override
	public int getMemory(int n) {
		return getTotalBitsExchanged(n)+3*n+2*sizeOfSecret;
	}
	@Override
	public int getMinimumNumberOfCryptoCalls() {
		return 2;
	}

}
