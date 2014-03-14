package protocols;

import java.math.BigDecimal;

public class RasmussenAndCapckunProtocol extends DBProtocol{

	private int sizeOfSignature;
	
	public RasmussenAndCapckunProtocol(int sizeOfSignature){
		this.sizeOfSignature = sizeOfSignature;
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
	public BigDecimal getMafiaFraudProbability(int n) {
		return ONE_OVER_TWO.pow(n);
	}

	/*Trujillo- Mar 14, 2014
	 * Here apply the same comment than for mafia. This is decision
	 * has been taken because is otherwise unfair to compare this
	 * protocol with others. */
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
		return sizeOfSignature;
	}

}
