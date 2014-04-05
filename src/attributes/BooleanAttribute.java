package attributes;

import attributes.scales.NoScale;

public class BooleanAttribute extends Attribute{

	private boolean value;
	
	public BooleanAttribute(boolean value){
		this.value = value;
	}
	
	public boolean getValue(){
		return value;
	}

	public boolean setValue(boolean value){
		return this.value = value;
	}
	
	@Override
	public BooleanAttribute getInstance() {
		return new BooleanAttribute(false);
	}
}
