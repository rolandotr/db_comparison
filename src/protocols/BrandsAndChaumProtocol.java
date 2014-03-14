package protocols;

import java.math.BigDecimal;

public class BrandsAndChaumProtocol extends DBProtocol{
	
	private int sizeOfCommitmentAndSignature;
	
	public BrandsAndChaumProtocol(int sizeOfCommitmentAndSignature) {
		this.sizeOfCommitmentAndSignature = sizeOfCommitmentAndSignature;
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
	public BigDecimal getTerroritFraudProbability(int n) {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return true;
	}

	@Override
	public int getTotalBitsExchanged(int n) {
		return 2*sizeOfCommitmentAndSignature+n;
	}

}
