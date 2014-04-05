package protocols;

import java.math.BigDecimal;

public class HanckeAndKuhnProtocol extends DBProtocol{

	private int sizeOfNonces;
	
	public HanckeAndKuhnProtocol(int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
	}

	public HanckeAndKuhnProtocol(){
		this(SIZE_OF_NONCES);
	}
	@Override
	public String getAcronym() {
		return "HK";
	}

	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		return THREE_OVER_FOUR.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		return THREE_OVER_FOUR.pow(n);
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
		return sizeOfNonces*2;
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
	public int getMinimumNumberOfCryptoCalls() {
		return 1;
	}
	@Override
	public DBProtocol[] getAllInstances(int factor) {
		return new DBProtocol[]{new HanckeAndKuhnProtocol()};
	}

	@Override
	public String getIdentifier() {
		return "HK";
	}

	@Override
	public int getYearOfPublication() {
		return 2005;
	}

}
