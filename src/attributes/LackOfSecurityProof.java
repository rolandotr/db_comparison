package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class LackOfSecurityProof extends Attribute<Boolean>{

	private static final long serialVersionUID = -1852042952999921339L;

	public LackOfSecurityProof(boolean value, ApproximateEquality<Boolean> equality, Scale<Boolean> scale){
		super(value, equality, scale);
	}
	
	public LackOfSecurityProof(ApproximateEquality<Boolean> equality, Scale<Boolean> scale){
		this(false, equality, scale);
	}
	
	@Override
	public String toString() {
		return "lackOfSecurityProof = "+getValue();
	}
	
	@Override
	public String getName() {
		return "LackOfSecurityProof";
	}

	@Override
	public Attribute<Boolean> getInstance() {
		return new LackOfSecurityProof(value, equality, scale);
	}
	
}
