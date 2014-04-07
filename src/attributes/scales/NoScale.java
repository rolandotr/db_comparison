package attributes.scales;


public class NoScale<T> implements Scale<T>{

	@Override
	public T scale(T value) {
		return value;
	}
	
	@Override
	public T unScale(T value) {
		return value;
	}

	@Override
	public String scaleMeaning(T value) {
		return ""+value;
	}
	

}
