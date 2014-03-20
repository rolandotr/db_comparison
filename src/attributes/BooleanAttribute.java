package attributes;

public class BooleanAttribute extends Attribute{

	private boolean value;
	
	public BooleanAttribute(boolean value){
		this.value = value;
	}
	
	public boolean getValue(){
		return value;
	}

	@Override
	public BooleanAttribute getInstance() {
		return new BooleanAttribute(false);
	}
}
