package attributes;


import attributes.scales.Scale;

public class DistanceFraudProbability extends Attribute<Double>{

	public DistanceFraudProbability(double value, Scale<Double> scale){
		super(value, scale);
	}

	@Override
	public DistanceFraudProbability getInstance() {
		return new DistanceFraudProbability(value, scale);
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
