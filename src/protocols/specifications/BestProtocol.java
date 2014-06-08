package protocols.specifications;

import java.math.BigDecimal;

public class BestProtocol extends DBProtocol{

	@Override
	public DBProtocol getInstance() {
		return new BestProtocol();
	}

	@Override
	public String getAcronym() {
		return "best";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getTerroristFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return false;
	}

	@Override
	public int getSizeOfTheChannel() {
		return 1;
	}

	@Override
	public boolean lackSecurityProof() {
		return false;
	}

	@Override
	public int getYearOfPublication() {
		return 0;
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return 0;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 0;
	}

	@Override
	public int getNumberOfNoncesGenerated() {
		return 0;
	}

	@Override
	public int getBitsGenerated() {
		return 0;
	}

	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new BestProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "Best-{"+n+"}";
	}

	@Override
	public int getCryptoCalls() {
		return 0;
	}

}
