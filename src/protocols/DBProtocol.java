package protocols;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import attributes.Attribute;
import attributes.DistanceFraudProbability;
import attributes.DoubleAttribute;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.TerroristFraudProbability;
import attributes.TotalBitsExchanged;
import attributes.YearOfPublication;

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
	
	public abstract BigDecimal getTerroristFraudProbability(int n);
	
	public abstract boolean hasFinalSlowPhase();
	
	public abstract boolean hasMultipleBitExchange();
	
	public abstract int getYearOfPublication();
	
	/*Trujillo- Mar 17, 2014
	 * We define memory as the memory required during the slow phase
	 * that is provided by the method getTotalBitsExchanged plus the 
	 * number of bits exchanged during the fast phase. Note that, this could be quite
	 * confusing for some protocols as presented in the survey right now.*/
	public abstract long getMemory(int n);
	
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
	
	/*Trujillo- Apr 3, 2014
	 * */
	public Attribute getAttribute(Attribute a, int n){
		if (a instanceof DistanceFraudProbability){
			return new DistanceFraudProbability(getDistanceFraudProbability(n).doubleValue(), 
					((DoubleAttribute)a).scale);
		}
		else if (a instanceof MafiaFraudProbability){
			return new MafiaFraudProbability(getMafiaFraudProbability(n).doubleValue(), 
					((DoubleAttribute)a).scale);
		} 
		else if (a instanceof TerroristFraudProbability){
			return new TerroristFraudProbability(getTerroristFraudProbability(n).doubleValue(), 
					((DoubleAttribute)a).scale);
		} 
		else if (a instanceof TotalBitsExchanged){
			return new TotalBitsExchanged(getTotalBitsExchanged(n), 
					((DoubleAttribute)a).scale);
		} 
		else if (a instanceof Memory){
			return new Memory(getMemory(n), 
					((DoubleAttribute)a).scale);
		}
		else if (a instanceof FinalSlowPhase){
			return new FinalSlowPhase(hasFinalSlowPhase());
		}
		else if (a instanceof YearOfPublication){
			return new YearOfPublication(getYearOfPublication());
		}
		else{
			throw new RuntimeException("Unsuported attribute: "+a.toString());
		}
	}
	
	/*Trujillo- Mar 24, 2014
	 * The protocols to be loaded. Note that for each it might be several settings. The number of settings is bounded
	 * by factor^m where m is the number of parameters that can be changed. Also note that the number of rounds is not
	 * a parameter.*/
	public static DBProtocol[] loadProtocols(int factor) {
		List<DBProtocol[]> protocols = new LinkedList<>();
		int length = 0;
		DBProtocol[] tmp = new BrandsAndChaumProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new BussardAndBaggaProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new HanckeAndKuhnProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new KimAndAvoineProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new MADProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new MunillaAndPeinadoProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new PoulidorProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new RasmussenAndCapckunProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new SKIProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new SwissKnifeProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		tmp = new TreeBasedProtocol().getAllInstances(factor);
		length += tmp.length;
		protocols.add(tmp);
		DBProtocol[] result = new DBProtocol[length];
		int index = 0;
		for (DBProtocol[] list : protocols) {
			for (int i = 0; i < list.length; i++) {
				result[index] = list[i];
				index++;
			}
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DBProtocol){
			DBProtocol tmp = (DBProtocol)obj;
			return tmp.getIdentifier().equals(this.getIdentifier());
		}
		else return false;
	}

	@Override
	public String toString() {
		return getIdentifier();
	}
}

