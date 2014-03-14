package utils;

import java.math.BigInteger;

public abstract class Combinatory {

	public static BigInteger comb(int n, int k){
		if (n < k || n < 1) throw new IllegalArgumentException("n en k invalid n<k or n < 0; n = "+n+" and k = "+k); 
		if (n == k || k == 0) return new BigInteger("1");
		if (k < 0) return new BigInteger("0");
		BigInteger num;
		BigInteger den;
		if (k < n - k) {
			num = fact(n, n-k+1);
			den = fact(k);
		}
		else{
			num = fact(n, k+1);
			den = fact(n-k);
		}
		return num.divide(den); 
	}
	
	public static BigInteger fact(int n){
		return fact(n,1);
	}
	
	public static BigInteger fact(int n, int u){
		BigInteger result =  new BigInteger(""+u);
		for (int i = u; i < n; i++) {
			result = result.multiply(new BigInteger(""+(i+1)));
		}
		return result;
	}
}
