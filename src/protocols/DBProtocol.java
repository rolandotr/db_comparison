package protocols;

import java.math.BigDecimal;

public abstract class DBProtocol {

	public static final BigDecimal ONE_OVER_TWO = new BigDecimal("0.5");
	public static final BigDecimal THREE_OVER_FOUR = new BigDecimal("0.75");
	public static final BigDecimal ONE = new BigDecimal("1");
	
	/*Trujillo- Mar 24, 2014
	 * All this numbers are in bits*/
	public static final int SIZE_OF_COMMIT = 128;
	public static final int SIZE_OF_NONCES = 128;
	public static final int SIZE_OF_SECRET = 128;
	public static final int SIZE_OF_MAC = 128;
	public static final int SIZE_OF_HASH = 128;

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
	 * confusing for some protocols as presented in the survey right now.*/
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
	
	/*Trujillo- Mar 24, 2014
	 * The factor represents the maximum number of parameter values that should be considered.
	 * Consequently, if a protocol has k parameters, it has, at most, k^factor different instances.*/
	public abstract DBProtocol[] getAllInstances(int factor);
	
	/*Trujillo- Mar 25, 2014
	 * The identifier should uniquely identify the protocols even if the protocol
	 * is the same with different parameters. Remember that n is not considered a parameter.*/
	public abstract String getIdentifier();
	

}
