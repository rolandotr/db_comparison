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

	public MADProtocol(){
		this(SIZE_OF_NONCES, SIZE_OF_MAC, SIZE_OF_SECRET);
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
	
	@Override
	public boolean hasMultipleBitExchange() {
		return false;
	}
	@Override
	public int getMemory(int n) {
		return getTotalBitsExchanged(n)+2*n;
	}
	@Override
	public int getMinimumNumberOfCryptoCalls() {
		return 4;
	}
	@Override
	public DBProtocol[] getAllInstances(int factor) {
		return new DBProtocol[]{new MADProtocol(SIZE_OF_NONCES, SIZE_OF_MAC, SIZE_OF_SECRET)};
	}

	@Override
	public String getIdentifier() {
		return "MAD";
	}

}
