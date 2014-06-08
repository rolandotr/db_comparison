package protocols.specifications;

import java.math.BigDecimal;

public class YKHLProtocol extends DBProtocol{
	
	private static final long serialVersionUID = -7060835761171756392L;
	protected double pd;
	protected int sizeOfNonce;
	
	public YKHLProtocol(double pd){
		this(pd, SIZE_OF_NONCES);
	}
	
	public YKHLProtocol(double pd, int sizeOfNonces){
		this.pd = pd;
		this.sizeOfNonce = sizeOfNonces;
	}
	
	
	public YKHLProtocol() {
	}

	@Override
	public String getAcronym() {
		return "YKHL";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		if (pd == 1) return mafiaFraudPdOne(n);
		else if (pd == 0.75) return mafiaFraudPdThreeQuarter(n);
		else throw new UnsupportedOperationException();
	}

	@Override
	public DBProtocol getInstance() {
		return new YKHLProtocol(pd);
	}

	private BigDecimal mafiaFraudPdOne(int n) {
		BigDecimal tmp = new BigDecimal(""+((double)5)/8);
		tmp = tmp.pow(n);
		for (int i = 1; i <= n; i++) {
			BigDecimal tmp2 = new BigDecimal(""+((double)1)/4);
			BigDecimal tmp3 = new BigDecimal(""+((double)5)/8);
			BigDecimal tmp4 = new BigDecimal(""+((double)3)/4);
			tmp3 = tmp3.pow(i-1);
			tmp4 = tmp4.pow(n-i);
			tmp = tmp.add(tmp2.multiply(tmp3).multiply(tmp4));
		}
		return tmp;
	}


	private BigDecimal mafiaFraudPdThreeQuarter(int n) {
		BigDecimal tmp = new BigDecimal(""+((double)1)/2);
		tmp = tmp.pow(n);
		for (int i = 1; i <= n; i++) {
			BigDecimal tmp2 = new BigDecimal(""+((double)3)/8);
			BigDecimal tmp3 = new BigDecimal(""+((double)1)/2);
			BigDecimal tmp4 = new BigDecimal(""+((double)5)/8);
			tmp3 = tmp3.pow(i-1);
			tmp4 = tmp4.pow(n-i);
			tmp = tmp.add(tmp2.multiply(tmp3).multiply(tmp4));
		}
		return tmp;
	}


	@Override
	public BigDecimal getDistanceFraudProbability() {
		BigDecimal tmp = new BigDecimal(""+((double)7)/8);
		return tmp.pow(n);
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
	public boolean lackSecurityProof() {
		return true;
	}

	@Override
	public int getYearOfPublication() {
		return 2010;
	}


	@Override
	public DBProtocol[] getInstances() {
		double[] pds = new double[]{0.75, 1};
		YKHLProtocol[] result = new YKHLProtocol[MAX_N*pds.length];
		for (int i = 0; i < MAX_N; i++){
			for (int j = 0; j < pds.length; j++) {
				int index = i*pds.length + j; 
				result[index] = new YKHLProtocol();
				result[index].setNumberOfRounds(i+1);
				result[index].pd = pds[j];
			}
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "YKHL-{"+n+", "+pd+"}";
	}

	@Override
	public int getCryptoCalls() {
		return 1;
	}

	@Override
	public int getSizeOfTheChannel() {
		return 1;
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return sizeOfNonce+n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 3*n;
	}

	@Override
	public int getNumberOfNoncesGenerated() {
		return 1;
	}

	@Override
	public int getBitsGenerated() {
		return n;
	}

}
