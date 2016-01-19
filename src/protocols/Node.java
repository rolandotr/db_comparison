package protocols;

import java.math.BigDecimal;
import java.util.TreeMap;

import protocols.specifications.DBProtocol;
import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.SizeOfMessages;
import attributes.TerroristFraudProbability;
import attributes.TotalBitsExchanged;
import attributes.relations.FinalSlowPhaseRelation;
import attributes.relations.IntegerRelation;
import attributes.relations.MemoryRelation;
import attributes.relations.ProbabilityRelation;
import attributes.relations.SizeOfMessagesRelation;
import attributes.scales.KbitsScale;
import attributes.scales.LogScale;
import attributes.scales.NoScale;

public class Node extends DBProtocol{
	
	public Attribute[] mafia;
	public Attribute[] distance;
	public Attribute[] terrorist;
	public Attribute[] bitsExchanged;
	public Attribute[] sizeOfMessage;
	public Attribute[] cryptoCalls;
	public Attribute[] memory;
	public Attribute[] finalSlowPhase;

	State state;
	
	public Node(DBProtocol[] protocols){
		state = new State(0, 0, 0, 0, 0, 0, 0, 0);
		System.out.println("Sorting attributes");
		mafia = sortProbability(new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)), protocols);
		System.out.println("Mafia size = "+mafia.length);
		distance = sortProbability(new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)), protocols);
		System.out.println("Distance size = "+distance.length);
		terrorist = sortProbability(new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)), protocols);
		System.out.println("Terrorist size = "+terrorist.length);
		bitsExchanged = sortInteger(new TotalBitsExchanged(new IntegerRelation(), new NoScale<Integer>()), protocols);
		System.out.println("Bits Exchanged size = "+bitsExchanged.length);
		sizeOfMessage = sortInteger(new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()), protocols);
		System.out.println("Size of Message size = "+sizeOfMessage.length);
		cryptoCalls = sortInteger(new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()), protocols);
		System.out.println("Crypto Calls size = "+cryptoCalls.length);
		memory = sortLong(new Memory(new MemoryRelation(), new KbitsScale()), protocols);
		System.out.println("Memory size = "+memory.length);
		finalSlowPhase = sortBoolean(new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()), protocols);
		System.out.println("Final Slow Phase size = "+finalSlowPhase.length);
		System.out.println("Sorting finished"); 
	}

	@Override
	public DBProtocol getInstance() {
		return this;
	}

	@Override
	public String getAcronym() {
		return "ToCompareWith";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		return new BigDecimal((Double)mafia[state.mafiaIndex].getValue());
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		return new BigDecimal((Double)distance[state.distanceIndex].getValue());
	}

	@Override
	public BigDecimal getTerroristFraudProbability() {
		return new BigDecimal((Double)terrorist[state.terroristIndex].getValue());
	}

	@Override
	public int getTotalBitsExchangedDuringFastPhase(){
		return (Integer)bitsExchanged[state.bitsExchangedIndex].getValue();
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return (Boolean)finalSlowPhase[state.finalSlowPhaseIndex].getValue();
	}

	@Override
	public int getSizeOfTheChannel() {
		return (Integer)sizeOfMessage[state.sizOfMessageIndex].getValue();
	}

	@Override
	public final long getMemory(){
		return (Long)memory[state.memoryIndex].getValue();
	}
	@Override
	public int getCryptoCalls() {
		return (Integer)cryptoCalls[state.cryptoCallsIndex].getValue();
	}

	@Override
	public boolean lackSecurityProof() {
		return false;
	}

	@Override
	public int getYearOfPublication() {
		return 0;
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return 0;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 0;
	}

	@Override
	public int getNumberOfNoncesGenerated() {
		return 0;
	}

	@Override
	public int getBitsGenerated() {
		return 0;
	}

	@Override
	public DBProtocol[] getInstances() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "ToCompareWith";
	}

	
	private static Attribute[] sortProbability(
			Attribute<Double> probability, DBProtocol[] protocols) {
		TreeMap<Double, Attribute<Double>> result = new TreeMap<>();
		for (DBProtocol p : protocols) {
			Attribute<Double> att = p.getAttribute(probability);
			result.put(att.getValue(), att);
		}
		Attribute<Double>[] array = new Attribute[result.size()];
		int index = 0;
		for (Attribute<Double> a : result.values()){
			if (index > 0 && probability.getEquality().equal(a.getValue(), array[index-1].getValue())) continue;
			array[index] = a;
			index++;
		}
		Attribute[] resultFinal = new Attribute[index];
		System.arraycopy(array, 0, resultFinal, 0, index);
		return resultFinal;
	}
	
	private static Attribute[] sortInteger(
			Attribute<Integer> probability, DBProtocol[] protocols) {
		TreeMap<Integer, Attribute<Integer>> result = new TreeMap<>();
		for (DBProtocol p : protocols) {
			Attribute<Integer> att = p.getAttribute(probability);
			result.put(att.getValue(), att);
		}
		Attribute<Integer>[] array = new Attribute[result.size()];
		int index = 0;
		for (Attribute<Integer> a : result.values()){
			if (index > 0 && probability.getEquality().equal(a.getValue(), array[index-1].getValue())) continue;
			array[index] = a;
			index++;
		}
		Attribute[] resultFinal = new Attribute[index];
		System.arraycopy(array, 0, resultFinal, 0, index);
		return resultFinal;
	}


	private static Attribute[] sortLong(
			Attribute<Long> probability, DBProtocol[] protocols) {
		TreeMap<Long, Attribute<Long>> result = new TreeMap<>();
		for (DBProtocol p : protocols) {
			Attribute<Long> att = p.getAttribute(probability);
			result.put(att.getValue(), att);
		}
		Attribute<Long>[] array = new Attribute[result.size()];
		int index = 0;
		for (Attribute<Long> a : result.values()){
			if (index > 0 && probability.getEquality().equal(a.getValue(), array[index-1].getValue())) continue;
			array[index] = a;
			index++;
		}
		Attribute[] resultFinal = new Attribute[index];
		System.arraycopy(array, 0, resultFinal, 0, index);
		return resultFinal;
	}
	private static Attribute[] sortBoolean(
			Attribute<Boolean> probability, DBProtocol[] protocols) {
		TreeMap<Boolean, Attribute<Boolean>> result = new TreeMap<>();
		for (DBProtocol p : protocols) {
			Attribute<Boolean> att = p.getAttribute(probability);
			result.put(att.getValue(), att);
		}
		Attribute[] array = new Attribute[result.size()];
		int index = 0;
		for (Attribute<Boolean> a : result.values()){
			array[index] = a;
			index++;
		}
		return array;
	}


	public boolean hasMafiaChildren() {
		if (state.mafiaIndex < mafia.length-1) return true; 
		return false;
	}
	public boolean hasDistanceChildren() {
		if (state.distanceIndex < distance.length-1) return true; 
		return false;
	}
	public boolean hasTerroristChildren() {
		if (state.terroristIndex < terrorist.length-1) return true; 
		return false;
	}
	public boolean hasBitsExchangedChildren() {
		if (state.bitsExchangedIndex < bitsExchanged.length-1) return true; 
		return false;
	}
	public boolean hasSizeOfMessageChildren() {
		if (state.sizOfMessageIndex < sizeOfMessage.length-1) return true; 
		return false;
	}
	public boolean hasMemoryChildren() {
		if (state.memoryIndex < memory.length-1) return true; 
		return false;
	}
	public boolean hasCryptoCallsChildren() {
		if (state.cryptoCallsIndex < cryptoCalls.length-1) return true; 
		return false;
	}
	public boolean hasFinalSlowPhaseChildren() {
		if (state.finalSlowPhaseIndex < finalSlowPhase.length-1) return true; 
		return false;
	}

	public void setState(State s) {
		state = s;
	}

	public State getState() {
		// TODO Auto-generated method stub
		return state;
	}
}