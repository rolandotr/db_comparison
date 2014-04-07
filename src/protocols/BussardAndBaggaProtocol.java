package protocols;

import java.math.BigDecimal;

public class BussardAndBaggaProtocol extends DBProtocol{

	public BussardAndBaggaProtocol(){
		
	}
	@Override
	public String getAcronym() {
		return "DBPK-Log";
	}

	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		return ONE_OVER_TWO.pow(n);
	}

	/*Trujillo- Mar 26, 2014
	 * There is a flaw in the distance fraud computation of this protocol. Therefore, 
	 * I assume the worst case, i.e. probability is one*/
	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		return ONE;
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
		return 2*n+SIZE_OF_COMMIT;
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
		return 4;
	}

	@Override
	public DBProtocol[] getAllInstances(int factor) {
		return new DBProtocol[]{new BussardAndBaggaProtocol()};
	}
	@Override
	public DBProtocol[] getDefaultInstances() {
		return new DBProtocol[]{new BussardAndBaggaProtocol()};
	}
	@Override
	public String getIdentifier() {
		return "BB";
	}
	
	@Override
	public int getYearOfPublication() {
		return 2005;
	}

}
