package tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Test {
	
	public static void main(String[] args) throws IOException {
		main2();
		main1();
	}

	private static void main2()  throws IOException {
		int n = 5;
		List<String> comb;
		List<int[]> paths;
		//para i = 1
		comb = new LinkedList<String>();
		comb.add("00");
		comb.add("01");
		comb.add("10");
		comb.add("11");
		paths = new LinkedList<int[]>();
		paths.add(new int[]{0});
		paths.add(new int[]{1});
		int[] tmp;
		List<int[]> pathsTmp;
		List<String> combTmp;
		Hashtable<String, Integer> counter;
		BufferedWriter write = new BufferedWriter(new FileWriter("test.DAT"));
		for (int i = 2; i <= n; i++){
			pathsTmp = new LinkedList<int[]>();
			for (int[] is : paths) {
				tmp = new int[is.length+1];
				System.arraycopy(is, 0, tmp, 0, is.length);
				tmp[tmp.length-1] = (tmp[tmp.length-2]+i);
				pathsTmp.add(tmp);
				tmp = new int[is.length+1];
				System.arraycopy(is, 0, tmp, 0, is.length);
				tmp[tmp.length-1] = (tmp[tmp.length-2]+i+1);
				pathsTmp.add(tmp);
			}
			paths = pathsTmp;
			combTmp = new LinkedList<String>();
			List<String> suffixs = bin(i+1);
			for (String c : comb) {
				for (String suffix : suffixs)
					combTmp.add(c+suffix);
			}
			comb = combTmp;
			int cont =0;
			for (String c : comb) {
				//System.out.println(c);
				String aux = "";
				counter = new Hashtable<String, Integer>();
				for (int[] is : paths) {
					//for (int j = 0; j < is.length; j++) {
						//System.out.print(is[j]);
					//}
					//System.out.println();
					aux = "";
					for (int j = 0; j < is.length; j++) {
						aux += c.charAt(is[j]);
					}
					if (counter.containsKey(aux)) counter.put(aux, counter.get(aux)+1);
					else counter.put(aux, 1);
				}
				int max = 0;
				for (String key : counter.keySet()) {
					if (counter.get(key) > max) max = counter.get(key);
				}
				cont += max;
				//System.out.println("For the chain "+c+" the max = "+max);
			}
			//entonces en contador mas o menos tengo lo que em debe dar la probabilidad que es 
			// cont/2^i*comb.size()
			write.append(i+" "+((double)cont)/(Math.pow(2, i)*comb.size()));
			write.newLine();
			System.out.println("When i = "+i+"then cont "+(double)cont/(Math.pow(2, i)*comb.size()));
		}
		write.close();
	}

	
	private static List<String> bin(int n){
		List<String> result = new ArrayList<String>();
		result.add("0");
		result.add("1");
		if (n == 1) return result;
		List<String> tmp;
		for (int i = 1; i < n; i++){
			tmp = new ArrayList<String>();
			for (String s: result){
				tmp.add(s+"0");
				tmp.add(s+"1");
			}
			result = tmp;
		}
		return result;
	}
	
	
	private static void main1()  throws IOException {
		int n = 6;
		List<String> comb;
		List<int[]> paths;
		//para i = 1
		comb = new LinkedList<String>();
		comb.add("00");
		comb.add("01");
		comb.add("10");
		comb.add("11");
		paths = new LinkedList<int[]>();
		paths.add(new int[]{1});
		paths.add(new int[]{2});
		int[] tmp;
		List<int[]> pathsTmp;
		List<String> combTmp;
		Hashtable<String, Integer> counter;
		BufferedWriter write = new BufferedWriter(new FileWriter("test.DAT"));
		for (int i = 2; i <= n; i++){
			pathsTmp = new LinkedList<int[]>();
			for (int[] is : paths) {
				tmp = new int[is.length+1];
				System.arraycopy(is, 0, tmp, 0, is.length);
				tmp[tmp.length-1] = (tmp[tmp.length-2]+1)%(2*i);
				pathsTmp.add(tmp);
				tmp = new int[is.length+1];
				System.arraycopy(is, 0, tmp, 0, is.length);
				tmp[tmp.length-1] = (tmp[tmp.length-2]+2)%(2*i);
				pathsTmp.add(tmp);
			}
			paths = pathsTmp;
			combTmp = new LinkedList<String>();
			for (String c : comb) {
				combTmp.add(c+"00");
				combTmp.add(c+"01");
				combTmp.add(c+"10");
				combTmp.add(c+"11");
			}
			comb = combTmp;
			int cont =0;
			for (String c : comb) {
				//System.out.println(c);
				String aux = "";
				counter = new Hashtable<String, Integer>();
				for (int[] is : paths) {
					//for (int j = 0; j < is.length; j++) {
						//System.out.print(is[j]);
					//}
					//System.out.println();
					aux = "";
					for (int j = 0; j < is.length; j++) {
						aux += c.charAt(is[j]);
					}
					if (counter.containsKey(aux)) counter.put(aux, counter.get(aux)+1);
					else counter.put(aux, 1);
				}
				int max = 0;
				for (String key : counter.keySet()) {
					if (counter.get(key) > max) max = counter.get(key);
				}
				cont += max;
				//System.out.println("For the chain "+c+" the max = "+max);
			}
			//entonces en contador mas o menos tengo lo que em debe dar la probabilidad que es 
			// cont/2^i*comb.size()
			write.append(i+" "+((double)cont)/(Math.pow(2, i)*comb.size()));
			write.newLine();
			System.out.println("When i = "+i+" then cont "+(double)cont/(Math.pow(2, i)*comb.size()));
		}
		write.close();
	}
}
