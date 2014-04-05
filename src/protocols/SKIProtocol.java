package protocols;

import java.math.BigDecimal;

public class SKIProtocol extends DBProtocol{

	private int t; 
	private int sizeOfNonces;
	
	public SKIProtocol(int t, int sizeOfNonces){
		this.t = t;
		this.sizeOfNonces = sizeOfNonces;
	}
	
	public SKIProtocol(){
		this(1, SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "SKI";
	}

	/*Trujillo- Mar 14, 2014
	 * Since the size of the challenges is t, we need to look first for the fair
	 * number of rounds. The other considerations is that q = 2 according to 
	 * the paper where this protocol was proposed. Note that, other values of
	 * q might be chosen.*/
	@Override
	public BigDecimal getMafiaFraudProbability(int n) {
		n = getFairNumberOfRounds(n, t);
		double q = 2;
		double tmp = (q+t-1)/(q*t);
		BigDecimal result = new BigDecimal(""+tmp);
		return result.pow(n);
	}

	/*Trujillo- Mar 14, 2014
	 * Apparently, the distance fraud resistance of this protocol is upper bounded
	 * by HK. So, this is what is implemented here.*/
	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		return THREE_OVER_FOUR.pow(n);
	}

	/*Trujillo- Mar 14, 2014
	 * Again, as in mafia fraud, we take q = 2*/
	@Override
	public BigDecimal getTerroristFraudProbability(int n) {
		if (t <= 2) return ONE;
		n = getFairNumberOfRounds(n, t);
		double q = 2;
		double tmp = (q*t+2*(1-q))/(q*t);
		BigDecimal result = new BigDecimal(""+tmp);
		return result.pow(n);
	}

	@Override
	public int getYearOfPublication() {
		return 2013;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return false;
	}

	@Override
	public int getTotalBitsExchanged(int n) {
		return 2*sizeOfNonces+t*n+1;
	}

	/*Trujillo- Mar 17, 2014
	 * This protocol is not designed to have a single bit exchange.
	 * Otherwise it does not resist terrorist fraud.*/
	@Override
	public boolean hasMultipleBitExchange() {
		return true;
	}

	@Override
	public long getMemory(int n) {
		return getTotalBitsExchanged(n)+t*n;
	}

	@Override
	public int getMinimumNumberOfCryptoCalls() {
		return 1;
	}

	@Override
	public DBProtocol[] getAllInstances(int factor) {
		DBProtocol[] result = new DBProtocol[factor];
		for (int i = 1; i <= factor; i++) {
			result[i-1] = new SKIProtocol(i, SIZE_OF_NONCES);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "SKI_t_"+t;
	}

	
}
