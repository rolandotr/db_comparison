package utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class BinarySeqEnum implements Enumeration<String>{

	
	long maxElements;
	
	int size;
	long index; 
	
	public BinarySeqEnum(int size){
		this.size = size;
		this.index = 0;
		this.maxElements = (long)Math.pow(2, size); //the empty set
	}

	@Override
	public boolean hasMoreElements() {
		return (index < maxElements);
	}

	@Override
	public String nextElement() {
		String result = "";
		long tmpIndex = index;
		for (int i = 0; i < size; i++) {
			if (tmpIndex % 2 == 0) result += "0";
			else result += "1";
			tmpIndex = tmpIndex/2;
		}
		index++;
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BinarySeqEnum g = new BinarySeqEnum(3);
		 while (g.hasMoreElements()) {
		     System.out.println(g.nextElement().toString());
		 }
	}
	
}
