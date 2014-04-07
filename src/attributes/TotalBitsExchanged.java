package attributes;

import attributes.scales.Scale;

public class TotalBitsExchanged extends Attribute<Integer>{

	
	public TotalBitsExchanged(int value, Scale<Integer> scale){
		super(value, scale);
	}

	@Override
	public TotalBitsExchanged getInstance() {
		return new TotalBitsExchanged(value, scale);
	}

	@Override
	public String toString() {
		return "bitsExchanged = "+getValue();
	}

	@Override
	public String getName() {
		return "BitsExchanged";
	}
	
}
