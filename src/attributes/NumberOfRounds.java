package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class NumberOfRounds extends Attribute<Integer>{

	private static final long serialVersionUID = -4151106695260927485L;
	
	public NumberOfRounds(int value, ApproximateEquality<Integer> equality, Scale<Integer> scale) {
		super(value, equality, scale);
	}
	
	public NumberOfRounds(ApproximateEquality<Integer> equality, Scale<Integer> scale) {
		this(0, equality, scale);
	}
	
	@Override
	public NumberOfRounds getInstance() {
		return new NumberOfRounds(value, equality, scale);
	}
	
	@Override
	public String toString() {
		return "rounds = "+getValue();
	}
	@Override
	public String getName() {
		return "NumberOfRounds";
	}
	
}
