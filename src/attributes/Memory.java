package attributes;

import attributes.scales.DoubleScale;

public class Memory extends DoubleAttribute{


	public Memory(long value){
		super(value);
	}
	
	public Memory(long value, DoubleScale scale){
		super(value, scale);
	}

	@Override
	public Memory getInstance() {
		return new Memory(-1, scale);
	}
	
	@Override
	public String toString() {
		return "memory = "+getValue();
	}
}
