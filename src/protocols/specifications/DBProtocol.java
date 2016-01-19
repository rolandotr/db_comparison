package protocols.specifications;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import methodology.History;


import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.LackOfSecurityProof;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.MultipleBitExchanged;
import attributes.NumberOfRounds;
import attributes.SizeOfMessages;
import attributes.TerroristFraudProbability;
import attributes.TotalBitsExchanged;
import attributes.YearOfPublication;
import attributes.relations.FinalSlowPhaseRelation;
import attributes.relations.IntegerRelation;
import attributes.relations.MemoryRelation;
import attributes.relations.ProbabilityRelation;
import attributes.relations.SizeOfMessagesRelation;
import attributes.scales.KbitsScale;
import attributes.scales.LogScale;
import attributes.scales.NoScale;

public abstract class DBProtocol implements Serializable{

	
	private static final long serialVersionUID = -1906510593510227313L;
	
	public static final BigDecimal ONE_OVER_TWO = new BigDecimal("0.5");
	public static final BigDecimal THREE_OVER_FOUR = new BigDecimal("0.75");
	public static final BigDecimal ONE = new BigDecimal("1");
	
	/*Trujillo- Mar 24, 2014
	 * All this numbers are in bits*/
	public static final int SIZE_OF_COMMIT = 256;
	public static final int SIZE_OF_NONCES = 256;
	public static final int SIZE_OF_SECRET = 256;
	public static final int SIZE_OF_MAC = 256;
	public static final int SIZE_OF_HASH = 256;
	
	public static int MAX_N = 256;
	//public static int MAX_N = 16;
	
	protected int n;
	protected int sizeOfSecret = SIZE_OF_SECRET;

	
	public static void main(String[] args) {
		KbitsScale scaleMem = new KbitsScale();
		LogScale scaleProb = new LogScale(2);
		KimAndAvoineProtocol kim = new KimAndAvoineProtocol(0.85);
		kim.setNumberOfRounds(37);
		System.out.println("KIM and Avoine");
		System.out.println("mafia = "+scaleProb.scale(kim.getMafiaFraudProbability().doubleValue()));
		System.out.println("distance = "+scaleProb.scale(kim.getDistanceFraudProbability().doubleValue()));
		System.out.println("terrorist = "+scaleProb.scale(kim.getTerroristFraudProbability().doubleValue()));
		System.out.println("memory = "+scaleMem.scale(kim.getMemory()));
		SKIProtocol ski = new SKIProtocol(2, DBProtocol.SIZE_OF_NONCES);
		ski.setNumberOfRounds(78);
		System.out.println("SKI");
		System.out.println("mafia = "+scaleProb.scale(ski.getMafiaFraudProbability().doubleValue()));
		System.out.println("distance = "+scaleProb.scale(ski.getDistanceFraudProbability().doubleValue()));
		System.out.println("terrorist = "+scaleProb.scale(ski.getTerroristFraudProbability().doubleValue()));
		System.out.println("memory = "+scaleMem.scale(ski.getMemory()));
		
		System.out.println("Comparing TMA vs Tree");
		DBProtocol tma = new TMAProtocol();
		tma.setNumberOfRounds(2);
		DBProtocol tree = new TreeBasedProtocol(2, DBProtocol.SIZE_OF_NONCES);
		tree.setNumberOfRounds(2);
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TotalBitsExchanged(new IntegerRelation(), new NoScale<Integer>()),
				//new TotalBitsExchanged(new BitsExchangedRelation(), new NoScale<Integer>()),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new Memory(new MemoryRelation(), new KbitsScale()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
		};
		
		if (tma.dominate(tree, attributes)){
			History.printInfoOfDomination(tma, tree, attributes);
		}
		else{
			History.printInfoOfDomination(tree, tma, attributes);
		}

	}
	
	/*Trujillo- May 26, 2014
	 * Is a kind of clone*/
	public boolean isSameInstanceRegardlessRounds(DBProtocol p){
		DBProtocol tmp1 = this.getInstance();
		DBProtocol tmp2 = p.getInstance();
		tmp1.setNumberOfRounds(1);
		tmp2.setNumberOfRounds(1);
		return tmp1.equals(tmp2);
	}
	
	public abstract DBProtocol getInstance();
	
	public abstract String getAcronym();
	
	public abstract BigDecimal getMafiaFraudProbability();

	public abstract BigDecimal getDistanceFraudProbability();
	
	public abstract BigDecimal getTerroristFraudProbability();
	
	public abstract boolean hasFinalSlowPhase();
	
	public final boolean hasMultipleBitExchange(){
		return getSizeOfTheChannel() > 1;
	}

	public abstract int getSizeOfTheChannel();

	public abstract boolean lackSecurityProof();
	
	public abstract int getYearOfPublication();
	
	public int getNumberOfRounds(){
		return n;
	}
	
	public void setNumberOfRounds(int n){
		this.n = n;
	}
	
	
	public long getMemory(){
		return getTotalMsgSizeReceived()+
				getTotalOutputSizeOfCallsToFunctions()+
				SIZE_OF_NONCES*getNumberOfNoncesGenerated()+
				getBitsGenerated();
	}
		
	public abstract int getTotalMsgSizeReceived();
	public abstract int getTotalOutputSizeOfCallsToFunctions();
	public abstract int getNumberOfNoncesGenerated();
	public abstract int getBitsGenerated();
	
	public int getTotalBitsExchangedDuringFastPhase(){
		return getNumberOfRounds()*getSizeOfTheChannel()*2;
	}
	
	/*Trujillo- Apr 5, 2014
	 * Predefined instances of the protocol*/
	public abstract DBProtocol[] getInstances();
	
	/*Trujillo- Mar 25, 2014
	 * The identifier should uniquely identify the protocols even if the protocol
	 * is the same with different parameters.*/
	public abstract String getIdentifier();
	

	/*Trujillo- Apr 5, 2014
	 * Minimum number of crypto calls*/
	public abstract int getCryptoCalls();

	/*Trujillo- Apr 3, 2014
	 * */
	public Attribute getAttribute(Attribute a){
		if (a instanceof DistanceFraudProbability){
			return new DistanceFraudProbability(getDistanceFraudProbability().doubleValue(), 
					((DistanceFraudProbability)a).getEquality(), ((DistanceFraudProbability)a).getScale());
		}
		else if (a instanceof MafiaFraudProbability){
			return new MafiaFraudProbability(getMafiaFraudProbability().doubleValue(), 
					((MafiaFraudProbability)a).getEquality(), ((MafiaFraudProbability)a).getScale());
		} 
		else if (a instanceof TerroristFraudProbability){
			return new TerroristFraudProbability(getTerroristFraudProbability().doubleValue(), 
					((TerroristFraudProbability)a).getEquality(), ((TerroristFraudProbability)a).getScale());
		} 
		else if (a instanceof Memory){
			return new Memory(getMemory(), 
					((Memory)a).getEquality(), ((Memory)a).getScale());
		}
		else if (a instanceof FinalSlowPhase){
			return new FinalSlowPhase(hasFinalSlowPhase(), ((FinalSlowPhase)a).getEquality(), ((FinalSlowPhase)a).getScale());
		}
		else if (a instanceof YearOfPublication){
			return new YearOfPublication(getYearOfPublication(), 
					((YearOfPublication)a).getEquality(), ((YearOfPublication)a).getScale());
		}
		else if (a instanceof CryptoCalls){
			return new CryptoCalls(getCryptoCalls(), 
					((CryptoCalls)a).getEquality(), ((CryptoCalls)a).getScale());
		}
		else if (a instanceof MultipleBitExchanged){
			return new MultipleBitExchanged(hasMultipleBitExchange(), 
					((MultipleBitExchanged)a).getEquality(), ((MultipleBitExchanged)a).getScale());
		}
		else if (a instanceof LackOfSecurityProof){
			return new LackOfSecurityProof(lackSecurityProof(), 
					((LackOfSecurityProof)a).getEquality(), ((LackOfSecurityProof)a).getScale());
		}
		else if (a instanceof NumberOfRounds){
			return new NumberOfRounds(getNumberOfRounds(), 
					((NumberOfRounds)a).getEquality(), ((NumberOfRounds)a).getScale());
		}
		else if (a instanceof SizeOfMessages){
			return new SizeOfMessages(getSizeOfTheChannel(), 
					((SizeOfMessages)a).getEquality(), ((SizeOfMessages)a).getScale());
		}
		else if (a instanceof TotalBitsExchanged){
			return new TotalBitsExchanged(getTotalBitsExchangedDuringFastPhase(), 
					((TotalBitsExchanged)a).getEquality(), ((TotalBitsExchanged)a).getScale());
		}
		else{
			throw new RuntimeException("Unsuported attribute: "+a.toString());
		}
	}
	

	/*Trujillo- Apr 5, 2014
	 * This load protocols with default parameters*/
	public static DBProtocol[] loadProtocols() {
		List<DBProtocol[]> protocols = new LinkedList<>();
		int length = 0;
		DBProtocol[] tmp = new BrandsAndChaumProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new BussardAndBaggaProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new HanckeAndKuhnProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new KimAndAvoineProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new MADProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new MunillaAndPeinadoProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new PoulidorProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new RasmussenAndCapckunProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new SKIProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new SwissKnifeProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new TMAProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new TreeBasedProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		tmp = new YKHLProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);
		/*tmp = new BestProtocol().getInstances();
		length += tmp.length;
		protocols.add(tmp);*/
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

	/*Trujillo- May 23, 2014
	 * This provide different list of protocols. The protocol within each list
	 * has the total number of bits sent during the fast phase equal.*/
	public static DBProtocol[][] loadProtocolsFairly() {
		DBProtocol[] protocols = loadProtocols();
		System.out.println("The total number of protocols to be compared is "+protocols.length);
		TreeMap<Integer, List<DBProtocol>> tmp = new TreeMap<>();
		for (int i = 0; i < protocols.length; i++) {
			//System.out.println(protocols[i].getIdentifier());
			int e = protocols[i].getTotalBitsExchangedDuringFastPhase();
			if (!tmp.containsKey(e)){
				tmp.put(e, new LinkedList<DBProtocol>());
			}
			tmp.get(e).add(protocols[i]);
		}
		DBProtocol[][] result = new DBProtocol[tmp.size()][];
		int pos = 0;
		for (List<DBProtocol> list : tmp.values()){
			result[pos] = new DBProtocol[list.size()];
			result[pos] = list.toArray(result[pos]);
			pos++;
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
	
	public boolean dominate(DBProtocol p, Attribute[] attributes){
		boolean result = false;
		for (int i = 0; i < attributes.length; i++) {
			Attribute a = this.getAttribute(attributes[i]);
			Attribute b = p.getAttribute(attributes[i]);
			if (b.dominate(a)) return false;
			if (a.dominate(b)) result = true;
		}
		return result;
	}

	public boolean isEqual(DBProtocol p, Attribute[] attributes) {
		for (int i = 0; i < attributes.length; i++) {
			Attribute a = this.getAttribute(attributes[i]);
			Attribute b = p.getAttribute(attributes[i]);
			if (!a.isEqual(b)) return false;
		}
		return true;
	}
}

