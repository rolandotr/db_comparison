package utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class Generator implements Enumeration<int[]>{

	GeneratorFree left;
	GeneratorFree right;
	int[] lastLeft;
	int[] lastRight;
	int pos;
	int level;
	int[] thresholds;
	boolean emptyGeneratorFinished;
	
	public Generator(int level, int[] thresholds){
		this.level = level;
		this.thresholds = thresholds;
		int[] leftThreshold = new int[pos]; 
		int[] rightThreshold = new int[thresholds.length-pos-1];
		System.arraycopy(thresholds, 0, leftThreshold, 0, leftThreshold.length);
		System.arraycopy(thresholds, leftThreshold.length+1, rightThreshold, 0, rightThreshold.length);
		if (level == 0){
			emptyGeneratorFinished = false;
		}else{
			for (int i = 0; i < thresholds.length; i++) {
				if (thresholds[i] >= level){
					pos = i;
					break;
				}
			}
			left = new GeneratorFree(level-1, leftThreshold);
			lastLeft = left.nextElement();
			right = new GeneratorFree(level, rightThreshold);
		}
	}

	public static int maxThreshold(int[] leftThreshold) {
		int max = 0;
		for (int i = 0; i < leftThreshold.length; i++) {
			if (leftThreshold[i] > max){
				max = leftThreshold[i];
			}
		}
		return max;
	}

	private int[] copyAll(int[] left, int[] right){
		if (left.length+right.length + 1 != thresholds.length)
			throw new RuntimeException("left = "+left.length+" and right = "+right.length);
		int[] result = new int[left.length+1+right.length];
		System.arraycopy(left, 0, result, 0, left.length);
		result[left.length] = level;
		System.arraycopy(right, 0, result, left.length+1, right.length);
		return result;
	}
	
	@Override
	public boolean hasMoreElements() {
		if (level == 0) return !emptyGeneratorFinished;
		if (left.hasMoreElements() || right.hasMoreElements()) return true;
		int newPos = pos;
		for (int i = pos+1; i < thresholds.length; i++) {
			if (thresholds[i] >= level){
				newPos = i;
				break;
			}
		}
		if (pos >= newPos) return false;
		pos = newPos;
		int[] leftThreshold = new int[pos]; 
		int[] rightThreshold = new int[thresholds.length-pos-1];
		System.arraycopy(thresholds, 0, leftThreshold, 0, leftThreshold.length);
		System.arraycopy(thresholds, leftThreshold.length+1, rightThreshold, 0, rightThreshold.length);
		left = new GeneratorFree(level-1, leftThreshold);
		lastLeft = left.nextElement();
		right = new GeneratorFree(level, rightThreshold);
		return (right.hasMoreElements());
	}

	@Override
	public int[] nextElement() {
		if (level == 0){
			emptyGeneratorFinished = true;
			return new int[thresholds.length];
		}
		if (right.hasMoreElements()){
			return copyAll(lastLeft, right.nextElement());
		}
		else if (left.hasMoreElements()){
			lastLeft = left.nextElement();
			right = new GeneratorFree(level, right.thresholds);
			return copyAll(lastLeft, right.nextElement());
		}
		else{
			throw new NoSuchElementException();
		}
	}
	
	public static void main(String[] args) {
		Generator g = new Generator(2, new int[]{1,3,1,2});
		while (g.hasMoreElements()){
			int[] next = g.nextElement();
			for (int i = 0; i < next.length; i++){
				System.out.print(""+next[i]);
			}
			System.out.println();
		}
	}
	
}
