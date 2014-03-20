package protocols;

import java.math.BigDecimal;

public abstract class DBProtocol {

	public static final BigDecimal ONE_OVER_TWO = new BigDecimal("0.5");
	public static final BigDecimal THREE_OVER_FOUR = new BigDecimal("0.75");
	public static final BigDecimal ONE = new BigDecimal("1");
	
	public abstract String getAcronym();
	
	public abstract BigDecimal getMafiaFraudProbability(int n);

	public abstract BigDecimal getDistanceFraudProbability(int n);
	
	public abstract BigDecimal getTerroritFraudProbability(int n);
	
	public abstract boolean hasFinalSlowPhase();
	
	public abstract boolean hasMultipleBitExchange();
	
	/*Trujillo- Mar 17, 2014
	 * We define memory as the memory required during the slow phase
	 * that is provided by the method getTotalBitsExchanged plus the 
	 * number of bits exchanged during the fast phase. Note that, this could be quite
	 * confusing for some protocols as presented in the sirvey right now.*/
	public abstract int getMemory(int n);
	
	public abstract int getMinimumNumberOfCryptoCalls();
	
	/*Trujillo- Mar 7, 2014
	 * It is unfair to compare protocols with 1-bit vs x-bits of
	 * challenge response during the fast phase. This method is implemented
	 * to solve this issue.*/
	public int getFairNumberOfRounds(int n, int size){
		return n/size;
	}
	
	public abstract int getTotalBitsExchanged(int n);

}
