package utils;

public abstract class ToStringUtility {

	public static String toStringBooleanArray(boolean[] b){
		String result = "";
		for (int i = 0; i < b.length; i++) {
			if (b[i]) result += "1";
			else result += "0";
		}
		return result;
	}
}
