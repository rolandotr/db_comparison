package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class SizeOfMessages extends Attribute<Integer>{

	private static final long serialVersionUID = -4151106695260927485L;
	
	public SizeOfMessages(int value, ApproximateEquality<Integer> equality, Scale<Integer> scale) {
		super(value, equality, scale);
	}
	
	public SizeOfMessages(ApproximateEquality<Integer> equality, Scale<Integer> scale) {
		this(0, equality, scale);
	}
	
	@Override
	public SizeOfMessages getInstance() {
		return new SizeOfMessages(value, equality, scale);
	}
	
	@Override
	public String toString() {
		return "t = "+getValue();
	}
	@Override
	public String getName() {
		return "SizeOfMessages";
	}
	
}
