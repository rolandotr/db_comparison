package protocols.specifications;

import java.math.BigDecimal;

public class MADProtocol extends DBProtocol{

	private static final long serialVersionUID = -7056798457379510464L;

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
	public BigDecimal getMafiaFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public DBProtocol getInstance() {
		return new MADProtocol();
	}

	@Override
	public BigDecimal getTerroristFraudProbability() {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return true;
	}

	@Override
	public int getCryptoCalls() {
		return 4;
	}
	
	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new MADProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "MAD-{"+n+"}";
	}


	@Override
	public int getYearOfPublication() {
		return 2003;
	}

	@Override
	public boolean lackSecurityProof() {
		return true;
	}

	@Override
	public int getSizeOfTheChannel() {
		return 1;
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return n+sizeOfRandomNumbers+2*outputSizeOfMAC;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 3*outputSizeOfMAC;
	}

	@Override
	public int getNumberOfNoncesGenerated() {
		return 1;
	}

	@Override
	public int getBitsGenerated() {
		// TODO Auto-generated method stub
		return n;
	}

}
