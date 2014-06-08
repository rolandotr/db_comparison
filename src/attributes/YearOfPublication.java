package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;


public class YearOfPublication extends Attribute<Integer>{

	private static final long serialVersionUID = 3600307032927227628L;

	public YearOfPublication(int value, ApproximateEquality<Integer> equality, Scale<Integer> scale){
		super(value, equality, scale);
	}

	public YearOfPublication(ApproximateEquality<Integer> equality, Scale<Integer> scale){
		this(0, equality, scale);
	}

	@Override
	public YearOfPublication getInstance() {
		return new YearOfPublication(value, equality, scale);
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
