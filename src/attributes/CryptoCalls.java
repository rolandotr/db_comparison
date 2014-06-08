package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class CryptoCalls extends Attribute<Integer>{

	private static final long serialVersionUID = -3844913842878593165L;

	public CryptoCalls(int value, ApproximateEquality<Integer> equality, Scale<Integer> scale){
		super(value, equality, scale);
	}

	public CryptoCalls(ApproximateEquality<Integer> equality, Scale<Integer> scale){
		this(0, equality, scale);
	}

	@Override
	public CryptoCalls getInstance() {
		return new CryptoCalls(value, super.equality, scale);
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
