package attributes;

import attributes.scales.Scale;

public class Memory extends Attribute<Long>{

	public Memory(long value, Scale<Long> scale){
		super(value, scale);
	}

	@Override
	public Memory getInstance() {
		return new Memory(value, scale);
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
