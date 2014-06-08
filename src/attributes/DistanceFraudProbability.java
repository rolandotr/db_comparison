package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class DistanceFraudProbability extends Attribute<Double>{

	private static final long serialVersionUID = -8977243624741302469L;

	public DistanceFraudProbability(double value, ApproximateEquality<Double> equality, Scale<Double> scale){
		super(value, equality, scale);
	}

	public DistanceFraudProbability(ApproximateEquality<Double> equality, Scale<Double> scale){
		this(0, equality, scale);
	}

	@Override
	public DistanceFraudProbability getInstance() {
		return new DistanceFraudProbability(value, equality, scale);
	}
		
	@Override
	public String toString() {
		return "distance = "+getValue();
	}

	@Override
	public String getName() {
		return "Distance";
	}
}
