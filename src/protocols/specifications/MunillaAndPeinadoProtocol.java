package protocols.specifications;

import java.math.BigDecimal;

/*Trujillo- Mar 7, 2014
 * We are not considering the fact that this protocol is not a 1-bit
 * protocol.*/
public class MunillaAndPeinadoProtocol extends DBProtocol{

	private static final long serialVersionUID = -1730976116865665981L;

	private double pf;
	private int sizeOfNonces;
	private int sizeOfHash = SIZE_OF_HASH;
	
	public MunillaAndPeinadoProtocol(double pf, int sizeOfNonces){
		this.pf = pf;
		this.sizeOfNonces = sizeOfNonces;
	}
	
	public MunillaAndPeinadoProtocol(double pf){
		this(pf, SIZE_OF_NONCES);
	}
	
	public MunillaAndPeinadoProtocol(){
		this(0, SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "MP";
	}

	@Override
	public int getYearOfPublication() {
		return 2006;
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		BigDecimal blackBoxPostAsk;
		BigDecimal tmp = new BigDecimal((pf/2)+"");
		tmp = tmp.negate();
		tmp = ONE.add(tmp);
		blackBoxPostAsk = tmp.pow(n);
		BigDecimal preAsk;
		if (pf < ((double)4)/7){
			tmp = new BigDecimal((pf)+"");
			tmp = tmp.negate();
			tmp = ONE.add(tmp);
			preAsk = tmp.pow(n);
		}
		else{
			tmp = new BigDecimal((pf)+"");
			tmp = tmp.multiply(THREE_OVER_FOUR);
			preAsk = tmp.pow(n);
		}
		if (preAsk.compareTo(blackBoxPostAsk) > 0) return preAsk;
		return blackBoxPostAsk;
	}

	@Override
	public DBProtocol getInstance() {
		return new MunillaAndPeinadoProtocol(pf);
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		BigDecimal tmp = new BigDecimal((pf/4)+"");
		tmp = tmp.negate();
		tmp = ONE.add(tmp);
		return tmp.pow(n);
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
		double[] pfs = new double[]{0, 0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 
				0.4, 0.45, 0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9,
				0.95, 1};
		/*double[] pfs = new double[]{0, 0.1, 0.2, 0.3,  
				0.4, 0.5, 0.6, 0.7, 0.8, 0.9,
				1};*/
		MunillaAndPeinadoProtocol[] result = new MunillaAndPeinadoProtocol[MAX_N*pfs.length];
		for (int i = 0; i < MAX_N; i++){
			for (int j = 0; j < pfs.length; j++) {
				int index = i*pfs.length + j; 
				result[index] = new MunillaAndPeinadoProtocol();
				result[index].setNumberOfRounds(i+1);
				result[index].pf = pfs[j];
			}
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "MP-{"+n+", "+pf+"}";
	}

	@Override
	public boolean lackSecurityProof() {
		return true;
	}

	@Override
	public int getSizeOfTheChannel() {
		return 2;
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return sizeOfNonces+n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 3*n+sizeOfHash;
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
