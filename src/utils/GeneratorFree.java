package utils;

import java.util.Enumeration;

public class GeneratorFree implements Enumeration<int[]>{

	int level;
	int[] current;
	int[] thresholds;
	
	public GeneratorFree(int level, int[] thresholds){
		this.level = level;
		this.thresholds = thresholds;
		current = new int[thresholds.length];
	}

	@Override
	public boolean hasMoreElements() {
		return current != null;
	}

	@Override
	public int[] nextElement() {
		int[] result = new int[current.length];
		System.arraycopy(current, 0, result, 0, current.length);
		int pos = current.length-1;
		while (pos >= 0 && (current[pos] == level || current[pos] == thresholds[pos])){
			current[pos] = 0; 
			pos--;
		}
		if (pos < 0) current = null;
		else current[pos] = current[pos]+1;
		return result;
	}
	
	public static void main(String[] args) {
		GeneratorFree g = new GeneratorFree(2, new int[]{1,3,1});
		while (g.hasMoreElements()){
			int[] next = g.nextElement();
			for (int i = 0; i < next.length; i++){
				System.out.print(""+next[i]);
			}
			System.out.println();
		}
	}
	
}
