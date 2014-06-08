package protocols.specifications;

import java.math.BigDecimal;

public class SKIProtocol extends DBProtocol{

	private static final long serialVersionUID = 1957979388314502266L;
	private int t; 
	private int sizeOfNonces;
	private int sizeOfFunction = SIZE_OF_MAC;
	private int q;
	
	public SKIProtocol(int t, int q, int sizeOfNonces){
		this.t = t;
		this.sizeOfNonces = sizeOfNonces;
		this.q = q;
	}
	public SKIProtocol(int t, int sizeOfNonces){
		this(t, 2, sizeOfNonces);
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
	public BigDecimal getMafiaFraudProbability() {
		double tmp = ((double)(q+t-1))/(q*t);
		BigDecimal result = new BigDecimal(""+tmp);
		return result.pow(n);
	}

	/*Trujillo- Mar 14, 2014
	 * Apparently, the distance fraud resistance of this protocol is upper bounded
	 * by HK. So, this is what is implemented here.*/
	@Override
	public BigDecimal getDistanceFraudProbability() {
		return THREE_OVER_FOUR.pow(n);
	}

	/*Trujillo- Mar 14, 2014
	 * Again, as in mafia fraud, we take q = 2*/
	@Override
	public BigDecimal getTerroristFraudProbability() {
		double tmp = ((double)(q*t+2*(1-q)))/(q*t);
		BigDecimal result = new BigDecimal(""+tmp);
		return result.pow(n);
	}

	@Override
	public int getYearOfPublication() {
		return 2013;
	}

	@Override
	public DBProtocol getInstance() {
		return new SKIProtocol(t, q, SIZE_OF_NONCES);
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return false;
	}

	@Override
	public int getCryptoCalls() {
		return 1;
	}

	@Override
	public DBProtocol[] getInstances() {
		SKIProtocol[] result = new SKIProtocol[MAX_N*(32-2+1)];
		for (int i = 0; i < MAX_N; i++){
			for (int t = 2; t <= 32; t++) {
				int index = i*(32-2+1) + t-2; 
				result[index] = new SKIProtocol();
				result[index].setNumberOfRounds(i+1);
				result[index].t = t;
				result[index].q = 2;
			}
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "SKI-{"+n+", "+t+"}";
	}
	
	
	@Override
	public boolean lackSecurityProof() {
		return false;
	}
	@Override
	public int getSizeOfTheChannel() {
		return t;
	}
	@Override
	public int getTotalMsgSizeReceived() {
		return sizeOfNonces+sizeOfFunction+n;
	}
	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return sizeOfFunction+getSizeOfTheChannel()*n;
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
