package protocols.specifications;

import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.TreeMap;


/*Trujillo- Mar 11, 2014
 * This simulator is similar to an exhaustive search. However, it limits
 * the number of sequences kept in memory. Instead of having all of them, 
 * we just keep the ones that are repeated more often.*/
public class PoulidorDistanceFraudSimulator{
	
	public static final int RUNS = 1000000;
	public static final int DEFAULT_SIZE = 65536;
	
	private int size = DEFAULT_SIZE;

			
	public PoulidorDistanceFraudSimulator(){			
	}
	
	public double computeDistanceFraud(int n){
		double result = 0;
		for (int i = 0; i < RUNS; i++) {
			SequenceInGraph mostFrequentSequence = runForMostFrequentSequence(n);
			//System.out.println(mostFrequentSequence.toString());
			result += (double)mostFrequentSequence.getTotalFrequency()/Math.pow(2, n);
		}
		return result/RUNS;
	}
	
	private SequenceInGraph runForMostFrequentSequence(int n){
		TreeMap<SequenceInGraph, SequenceInGraph> sequences = new TreeMap<>();
		SequenceInGraph first = new SequenceInGraph("", -1, 1);
		sequences.put(first, first);
		boolean[] graph = generateRandomGraph(n);
		//boolean[] graph = new boolean[]{false, true, false, false};
		//System.out.println("Graph: "+ToStringUtility.toStringBooleanArray(graph));
		sequences = runForMostFrequentSequence(n, sequences, graph);
		//System.out.println("Frequency: "+sequences.firstKey().getTotalFrequency());
		return sequences.firstKey();
	}
	
	/*Trujillo- Mar 11, 2014
	 * In this method, we have a list of sequences and their number of appearances. 
	 * Different to the tree, in this method we keep the positions (the nodes) in the tree of
	 * the sequences. Together with the position, we put the number of walks that generate 
	 * this sequence until this position. 
	 * So, let "s" a sequence and p1, ..., pm and f1 ... fm the positions and sequences
	 * respectively for s. Each position pi generates two new positions pi+1 and pi+2 and
	 * also two new (potentially equal) sequences. The frequency of both sequences is fi
	 * regardless whether they are equal or not.
	 * Frequencies are merged if a sequence "s" at some position "p" already exists
	 * in the table. In this case both frequencies are summed. 
	 * 
	 * We always keep the table with a size lower than SIZE.
	 * */
	
	/*Trujillo- Mar 11, 2014
	 * TODO: Change the tree map by a sorted list using the class
	 * SequenceInTree.*/
	private TreeMap<SequenceInGraph, SequenceInGraph> runForMostFrequentSequence(int n, 
			TreeMap<SequenceInGraph, SequenceInGraph> sequences, boolean[] graph){
		if (n == 0) return sequences;
		sequences = runForMostFrequentSequence(n-1, sequences, graph);
		TreeMap<SequenceInGraph, SequenceInGraph> result = new TreeMap<>();
		//we first develop all the current sequence a bit more
		for (SequenceInGraph s : sequences.keySet()){
			for (int pos : s.frequencies.keySet()){
				long frequency = s.frequencies.get(pos);
				int pos0 = pos+1;
				int pos1 = pos+2;
				String s0 = (graph[pos0])?s.sequence+"1":s.sequence+"0";
				String s1 = (graph[pos1])?s.sequence+"1":s.sequence+"0";
				SequenceInGraph s0In = result.get(new SequenceInGraph(s0));
				if (s0In == null) {
					SequenceInGraph tmp = new SequenceInGraph(s0, pos0, frequency);
					if (result.size() > size){
						SequenceInGraph last = result.lastKey();
						if (tmp.getTotalFrequency() > last.getTotalFrequency()){
							result.remove(last);
							result.put(tmp, tmp);
						}
					}
					else result.put(tmp, tmp);
				}
				else {
					s0In.addFrequencyTo(pos1, frequency);
					result.put(s0In, s0In);
				}
				SequenceInGraph s1In = result.get(new SequenceInGraph(s1));
				if (s1In == null) {
					SequenceInGraph tmp = new SequenceInGraph(s1, pos1, frequency);
					if (result.size() > size){
						SequenceInGraph last = result.lastKey();
						if (tmp.getTotalFrequency() > last.getTotalFrequency()){
							result.remove(last);
							result.put(tmp, tmp);
						}
					}
					else result.put(tmp, tmp);
				}
				else {
					s1In.addFrequencyTo(pos1, frequency);
					result.put(s1In, s1In);
				}
			}
		}
		return result;
	}
	
	/*Trujillo- Mar 11, 2014
	 * This method creates a random Poulidor graph*/
	private boolean[] generateRandomGraph(int n) {
		SecureRandom random = new SecureRandom();
		boolean[] result = new boolean[2*n];
		for (int i = 0; i < result.length; i++) {
			if (random.nextBoolean()) result[i] = true;
			else result[i] = false;
		}
		return result;
	}


	class SequenceInGraph implements Comparable<SequenceInGraph>{
		
		public String sequence;
		private Hashtable<Integer, Long> frequencies;
		
		public SequenceInGraph(String sequence) {
			this.sequence = sequence;
		}
		public void addFrequencyTo(int pos, long frequency) {
			if (frequencies.containsKey(pos)){
				frequencies.put(pos, frequency+frequencies.get(pos));
			}
			else frequencies.put(pos, frequency);
		}
		
		public SequenceInGraph(String sequence, int position, long frequency) {
			this(sequence);
			frequencies = new Hashtable<>();
			frequencies.put(position, frequency);
		}

		/*Trujillo- Mar 11, 2014
		 * This is sorted according to the frequency. */
		@Override
		public int compareTo(SequenceInGraph o) {
			if (sequence.equals(o.sequence)) return 0;
			//shortest sequence are always worst
			if (sequence.length() > o.sequence.length()) return -1;
			//If they are equals in terms of size, the the lower the frequency 
			//the worst is this sequence.
			//this might look counter intuitive, but it only means that the higher
			//the frequence the closer to the begginig in an ascendant sorted lost.
			if (getTotalFrequency() > o.getTotalFrequency()) return -1;
			return 1;
		}
		
		private long getTotalFrequency() {
			if (frequencies == null) return 0;
			long result = 0;
			for (long value : frequencies.values()) {
				result += value;
			}
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			SequenceInGraph tmp = (SequenceInGraph)obj;
			return (tmp.sequence.equals(sequence));
		}
		
		@Override
		public String toString() {
			return sequence+" : "+frequencies.toString();
		}
	}


	public void setSize(int size) {
		this.size = size;
	}
	
}

