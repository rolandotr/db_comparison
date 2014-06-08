package protocols.specifications;

import java.security.SecureRandom;
import java.util.TreeMap;


/*Trujillo- Mar 11, 2014
 * This simulator is similar to an exhaustive search. However, it limits
 * the number of sequences kept in memory. Instead of having all of them, 
 * we just keep the ones that are repeated more often.*/
public class TreeBasedDistanceFraudSimulator{
	
	public static final int RUNS = 100000;
	public static final int DEFAULT_SIZE = 65536;

	private int size = DEFAULT_SIZE;
	
	private SecureRandom random;
			
	public TreeBasedDistanceFraudSimulator(){			
		random = new SecureRandom();
	}
	
	public double computeDistanceFraud(int n){
		double result = 0;
		for (int i = 0; i < RUNS; i++) {
			SequenceInTree mostFrequentSequence = runForMostFrequentSequence(n);
			//System.out.println(mostFrequentSequence.toString());
			result += (double)mostFrequentSequence.frequency/Math.pow(2, n);
		}
		return result/RUNS;
	}
	
	private SequenceInTree runForMostFrequentSequence(int n){
		TreeMap<SequenceInTree, SequenceInTree> sequences = new TreeMap<>();
		SequenceInTree first = new SequenceInTree("", 1);
		sequences.put(first, first);
		sequences = runForMostFrequentSequence(n, sequences);
		return sequences.firstKey();
	}
	
	/*Trujillo- Mar 11, 2014
	 * In this method, we have list of sequences and their number of appearances.
	 * Let "s" be a sequence and "a" its number of appearances. If n > 0, both
	 * "s0" and "s1" can be generated appearing "a0" and "a1" times respectively 
	 * where a0 + a1 = 2*a. Therefore, a0 is "randomly" generated and a1 is computed
	 * as the Equation before. If, for instance, a0 is already in the list, 
	 * we sum its appearances, otherwise we add it.
	 * 
	 * Once all the sequences have been added, the size of the list is decreased down
	 * to SIZE by keeping only those sequences with the higher number of
	 * appearances.
	 * */
		
	/*Trujillo- Mar 31, 2014
	 * This in old a not efficient implementation, yet it works.*/
	private TreeMap<SequenceInTree, SequenceInTree> runForMostFrequentSequence(int n, 
			TreeMap<SequenceInTree, SequenceInTree> sequences){
		if (n == 0) return sequences;
		sequences = runForMostFrequentSequence(n-1, sequences);
		TreeMap<SequenceInTree, SequenceInTree> result = new TreeMap<>();
		//we first develop all the current sequence a bit more
		for (SequenceInTree s : sequences.keySet()){
			int a = s.frequency;
			int a0 = 0;
			int a1 = 0;
			for (int i = 0; i < a; i++) {
				int randomness = random.nextInt(4);
				switch (randomness) {
					case 0:
						a0 += 2;
						break;
					case 1:
						a0 += 1;
						a1 += 1;
						break;
					case 2:
						a0 += 1;
						a1 += 1;
						break;
					case 3:
						a1 += 2;
						break;
				}
			}
			SequenceInTree s0 = new SequenceInTree(s.sequence+"0", a0);
			SequenceInTree s1 = new SequenceInTree(s.sequence+"1", a1);
			SequenceInTree s0In = result.get(s0);
			SequenceInTree s1In = result.get(s1);
			if (s0In == null) result.put(s0, s0);
			else {
				SequenceInTree tmp = new SequenceInTree(s0.sequence, 
						s0.frequency+s0In.frequency);
				SequenceInTree last = result.lastKey();
				if (result.size() > size){
					if (tmp.frequency > last.frequency){
						result.remove(last);
						result.put(tmp, tmp);
					}
				}
				else result.put(tmp, tmp);
			}
			if (s1In == null) result.put(s1, s1);
			else {
				SequenceInTree tmp = new SequenceInTree(s1.sequence, 
						s1.frequency+s1In.frequency);
				SequenceInTree last = result.lastKey();
				if (result.size() > size){
					if (tmp.frequency > last.frequency){
						result.remove(last);
						result.put(tmp, tmp);
					}
				}
				else result.put(tmp, tmp);
			}
		}
		//now we cut the size according to SIZE
		return result;
	}
	
	class SequenceInTree implements Comparable<SequenceInTree>{
		
		public String sequence;
		public int frequency;
		
		public SequenceInTree(String sequence, int frequency) {
			this.sequence = sequence;
			this.frequency = frequency;
		}

		/*Trujillo- Mar 11, 2014
		 * This is sorted according to the frequency. */
		@Override
		public int compareTo(SequenceInTree o) {
			if (sequence.equals(o.sequence)) return 0;
			//shortest sequence are always worst
			if (sequence.length() > o.sequence.length()) return -1;
			//If they are equals in terms of size, the the lower the frequency 
			//the worst is this sequence.
			//this might look counter intuitive, but it only means that the higher
			//the frequence the closer to the begginig in an ascendant sorted lost.
			if (frequency > o.frequency) return -1;
			return 1;
		}
		
		@Override
		public boolean equals(Object obj) {
			SequenceInTree tmp = (SequenceInTree)obj;
			return (tmp.sequence.equals(sequence));
		}
		
		@Override
		public String toString() {
			return sequence+" : "+frequency;
		}
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}

