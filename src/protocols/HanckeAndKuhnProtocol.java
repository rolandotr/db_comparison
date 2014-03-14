package protocols;

import java.math.BigDecimal;

public class HanckeAndKuhnProtocol extends DBProtocol{

	private int sizeOfNonces;
	
	public HanckeAndKuhnProtocol(int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
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
	public BigDecimal getTerroritFraudProbability(int n) {
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

}
