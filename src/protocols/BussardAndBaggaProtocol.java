package protocols;

import java.math.BigDecimal;

public class BussardAndBaggaProtocol extends DBProtocol{

	@Override
	public String getAcronym() {
		return "DBPK-Log";
	}

	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		return null;
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
		throw new RuntimeException("to do");
	}

}
