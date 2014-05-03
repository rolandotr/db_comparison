package attributes.scales;

import java.io.Serializable;


public interface Scale<T> extends Serializable{
	
	

	public T scale(T value);

	public T unScale(T value);

	/*Trujillo- Apr 6, 2014
	 * Given a value already scaled, this string provides a kind of meaning.
	 * For instance, whether it is exponatiantion, etc.*/
	public String scaleMeaning(T value);
}
