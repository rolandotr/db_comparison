package protocols.specifications;

import java.math.BigDecimal;

public class KimAndAvoineProtocol extends DBProtocol{

	private static final long serialVersionUID = 2943291302333585452L;

	private double pd;
	private int sizeOfNonces;
	
	public KimAndAvoineProtocol(double pd, int sizeOfNonces){
		this.pd = pd;
		this.sizeOfNonces = sizeOfNonces;
	}
	
	public KimAndAvoineProtocol(double pd){
		this(pd, SIZE_OF_NONCES);
	}
	
	public KimAndAvoineProtocol(){
		this(0, SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "KA2";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		BigDecimal result = new BigDecimal("0.75");
		int alpha = (int)(n*pd);
		result = result.pow(n-alpha);
		BigDecimal half = new BigDecimal(0.5d);
		BigDecimal tmp = half.pow(alpha);
		result = result.multiply(tmp);
		tmp = half.pow(n+1);
		tmp = tmp.multiply(new BigDecimal(alpha));
		result = result.add(tmp);
		return result;
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		int alpha = (int)(n*pd);
		BigDecimal a = new BigDecimal("0.75");
		return a.pow(n-alpha);
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
		return new KimAndAvoineProtocol(pd);
	}


	@Override
	public int getCryptoCalls() {
		return 1;
	}

	@Override
	public DBProtocol[] getInstances() {
		double[] pds = new double[]{0, 0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 
				0.4, 0.45, 0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9,
				0.95, 1};
		/*double[] pds = new double[]{0, 0.1, 0.2, 0.3, 
				0.4, 0.5, 0.6, 0.7, 0.8, 0.9,
				1};*/
		KimAndAvoineProtocol[] result = new KimAndAvoineProtocol[MAX_N*pds.length];
		for (int i = 0; i < MAX_N; i++){
			for (int j = 0; j < pds.length; j++) {
				int index = i*pds.length + j; 
				result[index] = new KimAndAvoineProtocol();
				result[index].setNumberOfRounds(i+1);
				result[index].pd = pds[j];
			}
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "KA-{"+n+", "+pd+"}";
	}


	@Override
	public int getYearOfPublication() {
		return 2011;
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
		return 3*n;
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
