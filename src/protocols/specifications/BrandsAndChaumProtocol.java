package protocols.specifications;

import java.math.BigDecimal;

public class BrandsAndChaumProtocol extends DBProtocol{
	
	private static final long serialVersionUID = 4953569397230687732L;

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
	public BigDecimal getMafiaFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

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
		return 2;
	}


	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new BrandsAndChaumProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "BC-{"+n+"}";
	}

	@Override
	public int getYearOfPublication() {
		return 1993;
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
	public DBProtocol getInstance() {
		return new BrandsAndChaumProtocol();
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 3*sizeOfCommit;
	}

	@Override
	public int getNumberOfNoncesGenerated() {
		return 0;
	}

	@Override
	public int getBitsGenerated() {
		return n;
	}

}
