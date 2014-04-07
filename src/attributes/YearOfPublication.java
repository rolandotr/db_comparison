package attributes;

import attributes.scales.Scale;


public class YearOfPublication extends Attribute<Integer>{

	public YearOfPublication(int value, Scale<Integer> scale){
		super(value, scale);
	}

	@Override
	public YearOfPublication getInstance() {
		return new YearOfPublication(value, scale);
	}
	
	@Override
	public String toString() {
		return "year = "+getValue();
	}
	
	@Override
	public String getName() {
		return "Year";
	}
	
}
