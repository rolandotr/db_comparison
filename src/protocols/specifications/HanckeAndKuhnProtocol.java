package protocols.specifications;

import java.math.BigDecimal;

public class HanckeAndKuhnProtocol extends DBProtocol{

	private static final long serialVersionUID = -1906428862080738642L;
	private int sizeOfNonces;
	private int sizeOfHash = SIZE_OF_HASH;
	
	public static void main(String[] args) {
		HanckeAndKuhnProtocol p = new HanckeAndKuhnProtocol();
		p.setNumberOfRounds(4);
		System.out.println(p.getMafiaFraudProbability().doubleValue());
	}
	public HanckeAndKuhnProtocol(int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
	}

	public HanckeAndKuhnProtocol(){
		this(SIZE_OF_NONCES);
	}
	@Override
	public String getAcronym() {
		return "HK";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		return THREE_OVER_FOUR.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		return THREE_OVER_FOUR.pow(n);
	}

	@Override
	public BigDecimal getTerroristFraudProbability() {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return false;
	}

	@Override
	public DBProtocol getInstance() {
		return new HanckeAndKuhnProtocol();
	}

	@Override
	public int getCryptoCalls() {
		return 1;
	}
	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new HanckeAndKuhnProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "HK-{"+n+"}";
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
		return sizeOfNonces+n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 2*n;
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
