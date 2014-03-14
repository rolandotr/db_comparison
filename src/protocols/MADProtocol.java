package protocols;

import java.math.BigDecimal;

public class MADProtocol extends DBProtocol{

	private int sizeOfRandomNumbers;
	private int outputSizeOfMAC;
	private int sizeOfTheSecret;
	
	public MADProtocol(int sizeOfRandomNumbers, int outputSizeOfMAC, int sizeOfTheSecret){
		this.sizeOfRandomNumbers = sizeOfRandomNumbers;
		this.outputSizeOfMAC = outputSizeOfMAC;
		this.sizeOfTheSecret = sizeOfTheSecret;
	}
	@Override
	public String getAcronym() {
		return "MAD";
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
		return (sizeOfRandomNumbers+sizeOfTheSecret+outputSizeOfMAC)*2;
	}

}
