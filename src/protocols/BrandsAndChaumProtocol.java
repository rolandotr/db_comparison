package protocols;

import java.math.BigDecimal;

public class BrandsAndChaumProtocol extends DBProtocol{
	
	private int sizeOfCommit;
	
	public BrandsAndChaumProtocol(int sizeOfCommit) {
		this.sizeOfCommit = sizeOfCommit;
	}
	
	public BrandsAndChaumProtocol() {
		this(SIZE_OF_COMMIT);
	}
	
	@Override
	public String getAcronym() {
		return "BC";
	}

	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getTerroristFraudProbability(int n) {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return true;
	}

	@Override
	public boolean hasMultipleBitExchange() {
		return false;
	}
	@Override
	public long getMemory(int n) {
		return getTotalBitsExchanged(n) + 2*n;
	}
	
	@Override
	public int getCryptoCalls() {
		return 2;
	}

	@Override
	public int getTotalBitsExchanged(int n) {
		return 2*sizeOfCommit;
	}

	@Override
	public DBProtocol[] getAllInstances(int e, int factor) {
		if (e < 2) return null;
		return new DBProtocol[]{new BrandsAndChaumProtocol()};
	}

	@Override
	public DBProtocol[] getDefaultInstances(int e) {
		if (e < 2) return null;
		return new DBProtocol[]{new BrandsAndChaumProtocol()};
	}

	@Override
	public String getIdentifier() {
		return "BC";
	}

	@Override
	public int getYearOfPublication() {
		return 1993;
	}

	@Override
	public boolean lackSecurityProof() {
		return true;
	}

}
