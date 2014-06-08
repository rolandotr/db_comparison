package protocols.specifications;

import java.math.BigDecimal;

public class SwissKnifeProtocol extends DBProtocol{

	private static final long serialVersionUID = -7118040638225859003L;
	private int sizeOfSecret;
	private int sizeOfNonces;
	private int sizeOfHash = SIZE_OF_HASH;
	
	public SwissKnifeProtocol(int sizeOfSecret, int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
		this.sizeOfSecret = sizeOfSecret;
	}
	public SwissKnifeProtocol(){
		this(SIZE_OF_NONCES, SIZE_OF_SECRET);
	}

	@Override
	public String getAcronym() {
		return "Swiss-knife";
	}

	@Override
	public int getYearOfPublication() {
		return 2008;
	}
	
	@Override
	public BigDecimal getMafiaFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public DBProtocol getInstance() {
		return new SwissKnifeProtocol();
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		return THREE_OVER_FOUR.pow(n);
	}

	@Override
	public BigDecimal getTerroristFraudProbability() {
		return THREE_OVER_FOUR.pow(n);
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return true;
	}

	@Override
	public int getCryptoCalls() {
		return 2;
	}
	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new SwissKnifeProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}
	@Override
	public String getIdentifier() {
		return "SwissKnife-{"+n+"}";
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
		return 2*sizeOfNonces+n+sizeOfHash;
	}
	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 2*n+2*sizeOfHash;
	}
	@Override
	public int getNumberOfNoncesGenerated() {
		return 1;
	}
	@Override
	public int getBitsGenerated() {
		return 0;
	}

}
