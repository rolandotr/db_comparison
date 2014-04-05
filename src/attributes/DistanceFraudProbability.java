package attributes;


import attributes.scales.DoubleScale;

public class DistanceFraudProbability extends DoubleAttribute{

	public DistanceFraudProbability(double value, DoubleScale scale){
		super(value, scale);
	}

	@Override
	public DistanceFraudProbability getInstance() {
		return new DistanceFraudProbability(-1, scale);
	}
		
	@Override
	public String toString() {
		return "distance = "+getValue();
	}
}
