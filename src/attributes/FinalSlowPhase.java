package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class FinalSlowPhase extends Attribute<Boolean>{

	private static final long serialVersionUID = -226116297872306631L;

	public FinalSlowPhase(boolean value, ApproximateEquality<Boolean> equality, Scale<Boolean> scale){
		super(value, equality, scale);
	}
	
	public FinalSlowPhase(ApproximateEquality<Boolean> equality, Scale<Boolean> scale){
		this(false, equality, scale);
	}
	
	@Override
	public String toString() {
		return "finalSlowPhase = "+getValue();
	}
	
	@Override
	public String getName() {
		return "FinalPhase";
	}

	@Override
	public Attribute<Boolean> getInstance() {
		return new FinalSlowPhase(value, equality, scale);
	}
	
}
