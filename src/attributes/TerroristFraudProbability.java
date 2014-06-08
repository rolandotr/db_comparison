package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class TerroristFraudProbability extends Attribute<Double>{

	private static final long serialVersionUID = -2135759305373485027L;

	public TerroristFraudProbability(double value, ApproximateEquality<Double> equality, Scale<Double> scale){
		super(value, equality, scale);
	}

	public TerroristFraudProbability(ApproximateEquality<Double> equality, Scale<Double> scale){
		this(0, equality, scale);
	}


	@Override
	public TerroristFraudProbability getInstance() {
		return new TerroristFraudProbability(value, equality, scale);
	}
	@Override
	public String toString() {
		return "terrorist = "+getValue();
	}
	
	@Override
	public String getName() {
		return "Terrorist";
	}
	
}
