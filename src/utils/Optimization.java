package utils;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import methodology.History;
import protocols.Node;
import protocols.State;
import protocols.specifications.BestProtocol;
import protocols.specifications.DBProtocol;
import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.SizeOfMessages;
import attributes.TerroristFraudProbability;
import attributes.relations.FinalSlowPhaseRelation;
import attributes.relations.IntegerRelation;
import attributes.relations.MemoryRelation;
import attributes.relations.ProbabilityRelation;
import attributes.relations.SizeOfMessagesRelation;
import attributes.scales.KbitsScale;
import attributes.scales.LogScale;
import attributes.scales.NoScale;

public class Optimization {

	public static void main(String[] args) throws FileNotFoundException {
		System.setOut(new PrintStream("optimization_reduced"+DBProtocol.MAX_N+".txt"));

		createRoot();
	}
	
	public static void createRoot(){
		System.out.println("Starting");
		//DBProtocol[][] protocols = DBProtocol.loadProtocolsFairly();
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				//new TotalBitsExchanged(new IntegerRelation(), new NoScale<Integer>()),
				//new TotalBitsExchanged(new BitsExchangedRelation(), new NoScale<Integer>()),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new Memory(new MemoryRelation(), new KbitsScale()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
		};
		DBProtocol[] protocols = DBProtocol.loadProtocols();
		System.out.println("Total protocols: "+protocols.length);
		Node root = new Node(protocols);
		System.out.println("Starting the optimization process");
		List<DBProtocol>  nonDominated = getNonDpominated(root, protocols, attributes);
		
		System.out.println("Protocols dominating root = "+nonDominated.size());
		
		//search(root, protocols, attributes, new State[]{root.getState()});
		//search(root, protocols, attributes);
		DBProtocol.MAX_N = 2*DBProtocol.MAX_N;
		protocols = DBProtocol.loadProtocols();
		System.out.println("Total protocols: "+protocols.length);
		searchByIteration(root, protocols, attributes);
		
	}

	private static void search(Node root, DBProtocol[] protocols, Attribute[] attributes, State[] states){
		List<DBProtocol> tmp;
		Hashtable<String, String> names;
		boolean[] stateStop = new boolean[states.length];
		int removed = 0;
		for (int i = 0; i < states.length; i++){
			root.setState(states[i]);
			BestProtocol best = new BestProtocol();
			best.setNumberOfRounds(root.getTotalBitsExchangedDuringFastPhase()/2);
			if (!best.dominate(root, attributes)){
				//entonces nadie podra dominar a este hombre!
				tmp = new LinkedList<>();
			}
			else {
				System.out.println("The best succeed");
				tmp = getNonDpominated(root, protocols, attributes);
			}
			names = getProtocolNames(tmp);
			if (tmp.size() > 0){
			//if (names.size() == 1){
				System.out.println("One solution found, which is");
				History.printInfo(root, attributes);
				for (DBProtocol p : tmp) {
					//History.printInfo(p, attributes);
					System.out.print(p.getIdentifier()+", ");
				}
			}
			if (tmp.size() > 0){				
				stateStop[i] = true;
				removed++;
			}
		}
		System.out.println(removed*100d/states.length+" % removed: "+removed+ " out of "+states.length);
		List<State> newStates = new LinkedList<>();
		for (int i = 0; i < stateStop.length; i++){
			if (!stateStop[i]){
				root.setState(states[i]);
				if (root.hasMafiaChildren()){
					State s = states[i].clone();
					s.moveToMafiaChild();
					newStates.add(s);
				}
				if (root.hasDistanceChildren()){
					State s = states[i].clone();
					s.moveToDistanceChild();
					newStates.add(s);
				}
				if (root.hasTerroristChildren()){
					State s = states[i].clone();
					s.moveToTerroristChild();
					newStates.add(s);
				}
				/*if (root.hasBitsExchangedChildren()){
					State s = states[i].clone();
					s.moveToBitsExchangedChild();
					newStates.add(s);
				}*/
				if (root.hasSizeOfMessageChildren()){
					State s = states[i].clone();
					s.moveToSizeOfMessagesChild();
					newStates.add(s);
				}
				if (root.hasCryptoCallsChildren()){
					State s = states[i].clone();
					s.moveToCryptoCallsChild();
					newStates.add(s);
				}
				if (root.hasMemoryChildren()){
					State s = states[i].clone();
					s.moveToMemoryChild();
					newStates.add(s);
				}
				if (root.hasFinalSlowPhaseChildren()){
					State s = states[i].clone();
					s.moveToFinalSlowPhaseChild();
					newStates.add(s);
				}
			}
		}
		State[] result = new State[newStates.size()];
		newStates.toArray(result);
		search(root, protocols, attributes, result);
	}
	
	private static void search(Node root, DBProtocol[] protocols, Attribute[] attributes){
		List<DBProtocol> tmp;
		Hashtable<String, String> names;
		BestProtocol best = new BestProtocol();
		best.setNumberOfRounds(root.getTotalBitsExchangedDuringFastPhase()/2);
		if (!best.dominate(root, attributes)){
			//entonces nadie podra dominar a este hombre!
			tmp = new LinkedList<>();
		}
		else tmp = getNonDpominated(root, protocols, attributes);
		names = getProtocolNames(tmp);
		//if (tmp.size() > 0){
		if (names.size() == 1){
			System.out.println("One solution found, which is");
			History.printInfo(root, attributes);
			for (DBProtocol p : tmp) {
				//History.printInfo(p, attributes);
				System.out.print(p.getIdentifier()+", ");
			}
			return;
		}
		State clone = root.getState().clone();
		if (root.hasMafiaChildren()){
			root.getState().moveToMafiaChild();
			search(root, protocols, attributes);
			root.setState(clone);
		}
		if (root.hasDistanceChildren()){
			root.getState().moveToDistanceChild();
			search(root, protocols, attributes);
			root.setState(clone);
		}
		if (root.hasTerroristChildren()){
			root.getState().moveToTerroristChild();
			search(root, protocols, attributes);
			root.setState(clone);
		}
		/*if (root.hasBitsExchangedChildren()){
			root.getState().moveToBitsExchangedChild();
			search(root, protocols, attributes);
			root.setState(clone);
		}*/
		if (root.hasSizeOfMessageChildren()){
			root.getState().moveToSizeOfMessagesChild();
			search(root, protocols, attributes);
			root.setState(clone);
		}
		if (root.hasCryptoCallsChildren()){
			root.getState().moveToCryptoCallsChild();
			search(root, protocols, attributes);
			root.setState(clone);
		}
		if (root.hasMemoryChildren()){
			root.getState().moveToMemoryChild();
			search(root, protocols, attributes);
			root.setState(clone);
		}
		if (root.hasFinalSlowPhaseChildren()){
			root.getState().moveToFinalSlowPhaseChild();
			search(root, protocols, attributes);
			root.setState(clone);
		}
	}

	private static void searchByIteration(Node root, DBProtocol[] protocols, 
			Attribute[] attributes){
		int[] thresholds = new int[]{
			root.bitsExchanged.length-1,//0
			root.cryptoCalls.length-1,//1
			root.distance.length-1,//2
			root.finalSlowPhase.length-1,//3
			root.mafia.length-1,//4
			root.memory.length-1,//5
			root.sizeOfMessage.length-1,//6
			root.terrorist.length-1,//7
		};
		int maxLevel = Generator.maxThreshold(thresholds);
		System.out.println("Max thershold is "+ maxLevel);
		List<DBProtocol> tmp;
		Hashtable<String, String> names;
		State state;
		int[] values;
		BestProtocol best;
		List<int[]> blackList = new LinkedList<>();
		for (int level = 0; level <= maxLevel; level++) {
			Generator gen = new Generator(level, thresholds);
			System.out.println("Doing level "+level);
			while (gen.hasMoreElements()){
				values = gen.nextElement();
				boolean ok = true;
				for (int[] blackListed : blackList){
					ok = false;
					for (int i = 0; i < blackListed.length; i++) {
						if (values[i] < blackListed[i]){
							ok = true;
							break;
						}
					}
					if (!ok){
						break;
					}
				}
				if (!ok) continue;
				//state = new State(values[4], values[2], values[7], values[0], values[6], values[1], values[5], values[3]);
				state = new State(values[4], values[2], values[7], DBProtocol.MAX_N*2, values[6], values[1], values[5], values[3]);
				root.setState(state);
				best = new BestProtocol();
				best.setNumberOfRounds(root.getTotalBitsExchangedDuringFastPhase()/2);
				if (best.dominate(root, attributes)){
					//System.out.println(state.status());
					tmp = getNonDpominated(root, protocols, attributes);
					names = getProtocolNames(tmp);
					//if (tmp.size() > 0){
					if (names.size() == 1){
						System.out.println("One solution found, which is");
						History.printInfo(root, attributes);
						for (DBProtocol p : tmp) {
							//History.printInfo(p, attributes);
							System.out.println(p.getIdentifier()+", ");
						}
					}
					if (tmp.size() > 0){				
						System.out.println("Adding to the black list : "+ state.status()+", which contains "+names.size()+" protocols");
						List<int[]> newBlack = new LinkedList<>();
						boolean valueIn = true;
						for (int[] b : blackList) {
							if (lower(b, values)) {
								valueIn = false;
							}
							if (lower(values, b)){
								continue;
							}
							else {
								newBlack.add(b);
							}
						}
						if (valueIn) newBlack.add(values);
						//blackList.add(values);
						blackList = newBlack;
					}
				}
			}
		}
	}

	private static boolean lower(int[] a, int[] b) {
		boolean result = false;
		for (int i = 0; i < b.length; i++) {
			if (a[i] > b[i]) return false;
			if (a[i] < b[i]) result = true;
		}
		return result;
	}

	private static Hashtable<String, String> getProtocolNames(List<DBProtocol> protocols) {
		Hashtable<String, String> result = new Hashtable<String, String>();
		for (DBProtocol p : protocols){
			result.put(p.getAcronym(), p.getAcronym());
		}
		return result;
	}

	private static List<DBProtocol>  getNonDpominated(Node root,
			DBProtocol[] protocols, Attribute[] attributes) {
		List<DBProtocol> result = new LinkedList<>();
		Hashtable<String, String> names = new Hashtable<>();
		for (DBProtocol p : protocols){
			if (p.dominate(root, attributes)){
				result.add(p);
				names.put(p.getAcronym(), p.getAcronym());
			}
			if (names.size()>1){
				return result;
			}
		}
		return result;
	}

}
