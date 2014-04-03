package protocols;

import java.math.BigDecimal;

public class SwissKnifeProtocol extends DBProtocol{

	private int sizeOfSecret;
	private int sizeOfNonces;
	
	public SwissKnifeProtocol(int sizeOfSecret, int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
		this.sizeOfSecret = sizeOfSecret;
	}
	public SwissKnifeProtocol(){
		this(SIZE_OF_NONCES, SIZE_OF_SECRET);
	}

	@Override
	public String getAcronym() {
		return "Swiss-knife";
	}

	@Override
	public int getYearOfPublication() {
		return 2008;
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
	public BigDecimal getTerroristFraudProbability(int n) {
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
	@Override
	public DBProtocol[] getAllInstances(int factor) {
		return new DBProtocol[]{new SwissKnifeProtocol()};
	}
	@Override
	public String getIdentifier() {
		return "SwissKnife";
	}

}
