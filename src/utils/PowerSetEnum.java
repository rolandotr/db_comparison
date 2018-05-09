package utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class PowerSetEnum<T> implements Enumeration<Set<T>>{

	long currentPos;
	
	long lowerBound;
	long upperBound;
	
	long elementsConsumed;
	long maxElements;
	
	ArrayList<T> set;
	
	
	public PowerSetEnum(Set<T> originalSet, int lowerBound, int upperBound){
		this.set = new ArrayList<T>(originalSet.size());
		this.set.addAll(originalSet);
		this.currentPos = 0;
		this.lowerBound= lowerBound;
		this.upperBound= upperBound;
		this.maxElements = 0; //the empty set
		for (int i = lowerBound; i <= upperBound; i++) {
			this.maxElements += Combinatory.comb(originalSet.size(), i).longValue();
		}
		elementsConsumed = 0;
	}

	@Override
	public boolean hasMoreElements() {
		return (elementsConsumed < maxElements);
	}

	@Override
	public Set<T> nextElement() {
		Set<T> result;
		do {
			result = new HashSet<>();
			long tmpPos = currentPos;
			for (int i = 0; i < set.size(); i++) {
				if (tmpPos % 2 == 1) {
					result.add(set.get(i));
				}
				tmpPos = tmpPos/2;
			}
			currentPos++;
		}while (result.size() < lowerBound || result.size() > upperBound);
		elementsConsumed++;
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Set<Integer> mySet = new HashSet<Integer>();
		 mySet.add(1);
		 mySet.add(2);
		 mySet.add(3);
		 mySet.add(4);
		 mySet.add(5);
		 mySet.add(6);
		 mySet.add(7);
		 mySet.add(8);
		 mySet.add(9);
//		 mySet.add(10);
//		 mySet.add(11);
//		 mySet.add(12);
//		 mySet.add(13);
//		 mySet.add(14);
//		 mySet.add(15);
		 PowerSetEnum<Integer> g = new PowerSetEnum<>(mySet, 9, 9);
		 while (g.hasMoreElements()) {
		     System.out.println(g.nextElement().toString());
		 }
	}
	
}
