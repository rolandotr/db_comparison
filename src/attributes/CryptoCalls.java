package attributes;

import attributes.scales.Scale;

public class CryptoCalls extends Attribute<Integer>{

	public CryptoCalls(int value, Scale<Integer> scale){
		super(value, scale);
	}

	@Override
	public CryptoCalls getInstance() {
		return new CryptoCalls(value, super.scale);
	}

	@Override
	public String toString() {
		return "cryptoCalls = "+getValue();
	}

	@Override
	public String getName() {
		return "CryptoCalls";
	}
	
}
