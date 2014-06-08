package protocols.specifications;

import java.math.BigDecimal;

public class BussardAndBaggaProtocol extends DBProtocol{

	
	private static final long serialVersionUID = -5406876953380111587L;
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
	public BigDecimal getMafiaFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	/*Trujillo- Apr 17, 2014
	 * An attack shown by Avoine et al. 2011 shows that the resistance to terrorist fraud is actually 1*/
	@Override
	public BigDecimal getTerroristFraudProbability() {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return true;
	}

	@Override
	public DBProtocol getInstance() {
		return new BussardAndBaggaProtocol();
	}


	@Override
	public int getCryptoCalls() {
		return 4;
	}

	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new BussardAndBaggaProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "BB-{"+n+"}";
	}
	
	@Override
	public int getYearOfPublication() {
		return 2005;
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
		return n+sizeOfCommit;
	}
	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return n;
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
