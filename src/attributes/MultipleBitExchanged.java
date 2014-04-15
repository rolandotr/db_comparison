package attributes;

import attributes.scales.NoScale;

public class MultipleBitExchanged extends Attribute<Boolean>{

	public MultipleBitExchanged(boolean value){
		super(value, new NoScale<Boolean>());
	}
	
	@Override
	public String toString() {
		return "singleBitExchanged = "+getValue();
	}
	
	@Override
	public String getName() {
		return "SingleBitExchanged";
	}

	@Override
	public Attribute<Boolean> getInstance() {
		return new MultipleBitExchanged(value);
	}
	
}
