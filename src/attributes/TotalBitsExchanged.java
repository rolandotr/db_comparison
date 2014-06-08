package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class TotalBitsExchanged extends Attribute<Integer>{

	
	private static final long serialVersionUID = 71520896895854288L;

	public TotalBitsExchanged(int value, ApproximateEquality<Integer> equality, Scale<Integer> scale){
		super(value, equality, scale);
	}

	public TotalBitsExchanged(ApproximateEquality<Integer> equality, Scale<Integer> scale){
		this(0, equality, scale);
	}

	@Override
	public TotalBitsExchanged getInstance() {
		return new TotalBitsExchanged(value, equality, scale);
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
