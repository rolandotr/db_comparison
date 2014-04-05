package attributes;

import attributes.scales.DoubleScale;

public class TotalBitsExchanged extends DoubleAttribute{

	
	public TotalBitsExchanged(int value, DoubleScale scale){
		super(value, scale);
	}

	@Override
	public TotalBitsExchanged getInstance() {
		return new TotalBitsExchanged(-1, scale);
	}

	@Override
	public String toString() {
		return "bitsExchanged = "+getValue();
	}
}
