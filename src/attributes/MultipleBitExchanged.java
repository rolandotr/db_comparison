package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class MultipleBitExchanged extends Attribute<Boolean>{

	private static final long serialVersionUID = 6588800431691002570L;

	public MultipleBitExchanged(boolean value, ApproximateEquality<Boolean> equality, Scale<Boolean> scale){
		super(value, equality, scale);
	}
	
	public MultipleBitExchanged(ApproximateEquality<Boolean> equality, Scale<Boolean> scale){
		this(false, equality, scale);
	}
	
	@Override
	public String toString() {
		return "multipleBitExchanged = "+getValue();
	}
	
	@Override
	public String getName() {
		return "multipleBitExchanged";
	}

	@Override
	public Attribute<Boolean> getInstance() {
		return new MultipleBitExchanged(value, equality, scale);
	}
	
}
