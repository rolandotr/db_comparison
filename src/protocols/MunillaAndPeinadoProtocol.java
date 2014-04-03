package protocols;

import java.math.BigDecimal;

/*Trujillo- Mar 7, 2014
 * We are not considering the fact that this protocol is not a 1-bit
 * protocol.*/
public class MunillaAndPeinadoProtocol extends DBProtocol{

	private double pf;
	private int sizeOfNonces;
	
	public MunillaAndPeinadoProtocol(double pf, int sizeOfNonces){
		this.pf = pf;
		this.sizeOfNonces = sizeOfNonces;
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
	public BigDecimal getMafiaFraudProbability(int n) {
		BigDecimal blackBoxPreAsk;
		if (pf < (double)(4)/7){
			BigDecimal tmp = new BigDecimal(pf+"");
			tmp = tmp.negate();
			tmp = ONE.add(tmp);
			blackBoxPreAsk = tmp.pow(n);
		}
		else{
			BigDecimal tmp = new BigDecimal(pf+"");
			tmp = THREE_OVER_FOUR.multiply(tmp);
			blackBoxPreAsk = tmp.pow(n);
		}
		BigDecimal blackBoxPostAsk;
		BigDecimal tmp = new BigDecimal((pf/2)+"");
		tmp = tmp.negate();
		tmp = ONE.add(tmp);
		blackBoxPostAsk = tmp.pow(n);
		if (blackBoxPreAsk.compareTo(blackBoxPostAsk) >= 0) return blackBoxPreAsk;
		else return blackBoxPostAsk;
	}

	@Override
	public BigDecimal getDistanceFraudProbability(int n) {
		BigDecimal tmp = new BigDecimal((pf/4)+"");
		tmp = tmp.negate();
		tmp = ONE.add(tmp);
		return tmp.pow(n);
	}

	@Override
	public BigDecimal getTerroristFraudProbability(int n) {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return true;
	}

	@Override
	public int getTotalBitsExchanged(int n) {
		return 2*sizeOfNonces+3*n;
	}

	@Override
	public boolean hasMultipleBitExchange() {
		return true;
	}

	@Override
	public int getMemory(int n) {
		return getTotalBitsExchanged(n)+2*n;
	}

	@Override
	public int getMinimumNumberOfCryptoCalls() {
		return 2;
	}

	@Override
	public DBProtocol[] getAllInstances(int factor) {
		double pf = 0;
		DBProtocol[] result = new DBProtocol[factor+1];
		for (int i = 0; i <= factor; i++) {
			/*Trujillo- Mar 24, 2014
			 * this is computed in that way to avoid numerical error.*/
			pf = 1 - (factor-i)/(double)factor;
			result[i] = new MunillaAndPeinadoProtocol(pf, SIZE_OF_NONCES);
			if (pf > 1) pf = 1;
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "MP_pf_"+pf;
	}

}
