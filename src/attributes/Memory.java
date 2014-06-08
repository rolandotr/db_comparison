package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class Memory extends Attribute<Long>{

	private static final long serialVersionUID = 5269827552824204035L;
	
	public Memory(long value, ApproximateEquality<Long> equality, Scale<Long> scale){
		super(value, equality, scale);
	}

	public Memory(ApproximateEquality<Long> equality, Scale<Long> scale){
		this(0, equality, scale);
	}

	@Override
	public Memory getInstance() {
		return new Memory(value, equality, scale);
	}
	
	@Override
	public String toString() {
		return "memory = "+getValue();
	}
	@Override
	public String getName() {
		return "Memory";
	}
	
}
