package protocols.specifications;

import java.math.BigDecimal;
import java.math.BigInteger;

public class OptimalLookupProtocol extends DBProtocol{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int sizeOfNonces;
	protected int depth;
		
	public OptimalLookupProtocol(int depth, int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
		this.depth = depth;
	}

	public OptimalLookupProtocol(int sizeOfNonces){
		this.sizeOfNonces = sizeOfNonces;
		this.depth = 1;
	}

	public OptimalLookupProtocol(){
		this(1, SIZE_OF_NONCES);
	}
	
	@Override
	public String getAcronym() {
		return "optimal-lookup";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		return computeMyFar(n);
	}

	@Override
	public DBProtocol getInstance() {
		return new OptimalLookupProtocol(depth, SIZE_OF_NONCES);
	}

	@Override
	public int getYearOfPublication() {
		return 2023;
	}

	public BigDecimal computeMyFar(int k) {
		BigDecimal a = new BigDecimal(0.5d);
		a = a.pow(k);
		return a.multiply(new BigDecimal((double)k+1));
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		BigDecimal half = new BigDecimal(0.5d);
		return half.pow(n);
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
	public int getCryptoCalls() {
		int calls = n/depth;
		if (n % depth == 0) return calls;
		else return calls+1;
	}

	@Override
	public DBProtocol[] getInstances() {
		OptimalLookupProtocol[] result = new OptimalLookupProtocol[MAX_N*32];
		for (int i = 0; i < MAX_N; i++){
			for (int depth = 1; depth <= 32; depth++) {
				int index = i*32 + depth-1; 
				result[index] = new OptimalLookupProtocol();
				result[index].setNumberOfRounds(i+1);
				result[index].depth = depth;
			}
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "Optimal-{"+n+", "+depth+"}";
	}
	

	@Override
	public boolean lackSecurityProof() {
		return false;
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
		BigInteger tmp = new BigInteger(2+"");
		tmp = tmp.pow(depth+1);
		BigInteger minusTwo = new BigInteger(2+"");
		minusTwo = minusTwo.negate();
		tmp = tmp.add(minusTwo);
		tmp = tmp.multiply(new BigInteger((n/depth)+""));
		int remaining = n % depth;
		if (remaining > 1) {
			BigInteger tmp2 = new BigInteger(2+"");
			tmp2 = tmp2.pow(remaining+1);
			tmp2 = tmp2.add(minusTwo);
			tmp = tmp.multiply(tmp2);
		}
		//dealing with numerical errors
		if (tmp.bitLength() > 30) return Integer.MAX_VALUE/2;
		return tmp.intValue();
	}
	
	@Override
	public long getMemory(){
		long result = getTotalMsgSizeReceived()+
				SIZE_OF_NONCES*getNumberOfNoncesGenerated()+
				getBitsGenerated();
		BigInteger tmp = new BigInteger(2+"");
		tmp = tmp.pow(depth+1);
		BigInteger minusTwo = new BigInteger(2+"");
		minusTwo = minusTwo.negate();
		tmp = tmp.add(minusTwo);
		return result + tmp.longValue();
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
