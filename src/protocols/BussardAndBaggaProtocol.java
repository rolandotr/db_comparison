package protocols;

import java.math.BigDecimal;

public class BussardAndBaggaProtocol extends DBProtocol{

	
	protected int sizeOfCommit;
	
	public BussardAndBaggaProtocol(int sizeOfCommit){
		this.sizeOfCommit = sizeOfCommit;
	}
	public BussardAndBaggaProtocol(){	
		this(SIZE_OF_COMMIT);
	}
	
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
		return ONE_OVER_TWO.pow(n);
	}

	/*Trujillo- Apr 17, 2014
	 * An attack shown by Avoine et al. 2011 shows that the resistance to terrorist fraud is actually 1*/
	@Override
	public BigDecimal getTerroristFraudProbability(int n) {
		return ONE;
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
	public DBProtocol[] getAllInstances(int e, int factor) {
		if (e < 2) return null;
		return new DBProtocol[]{new BussardAndBaggaProtocol()};
	}
	@Override
	public DBProtocol[] getDefaultInstances(int e) {
		if (e < 2) return null;
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
	@Override
	public boolean lackSecurityProof() {
		return true;
	}

}
