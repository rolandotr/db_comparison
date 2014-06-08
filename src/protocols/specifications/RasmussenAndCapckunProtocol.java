package protocols.specifications;

import java.math.BigDecimal;

public class RasmussenAndCapckunProtocol extends DBProtocol{

	private static final long serialVersionUID = 3460958443329278821L;
	private int sizeOfSignature;
	private int maxSizeOfNonce;
	
	public RasmussenAndCapckunProtocol(int sizeOfSignature, int maxSizeOfNonce){
		this.sizeOfSignature = sizeOfSignature;
		this.maxSizeOfNonce = maxSizeOfNonce;
	}

	public RasmussenAndCapckunProtocol(){
		this(SIZE_OF_COMMIT, SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "RC";
	}

	/*Trujillo- Mar 14, 2014
	 * Note that, the real mafia fraud resistance of this protocol
	 * depends on the size of the nonces. However, in this implementation
	 * we assume that such a size is equal to the number of rounds n. And, 
	 * of course, the number of rounds should be sufficiently large in this case.*/
	@Override
	public BigDecimal getMafiaFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public DBProtocol getInstance() {
		return new RasmussenAndCapckunProtocol();
	}

	@Override
	public int getYearOfPublication() {
		return 2010;
	}

	/*Trujillo- Mar 14, 2014
	 * Here apply the same comment than for mafia. This is decision
	 * has been taken because is otherwise unfair to compare this
	 * protocol with others. */
	@Override
	public BigDecimal getDistanceFraudProbability() {
		return ONE_OVER_TWO.pow(n);
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
		return 3;
	}

	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new RasmussenAndCapckunProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "RC-{"+n+"}";
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
		return maxSizeOfNonce;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 2*sizeOfSignature;
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
