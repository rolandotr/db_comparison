package attributes;

import attributes.scales.LogScale;

public class DistanceFraudProbability extends DoubleAttribute{

	public DistanceFraudProbability(double value){
		super(value);
		setScale(new LogScale<DoubleAttribute>(2));
	}

	@Override
	public DistanceFraudProbability getInstance() {
		return new DistanceFraudProbability(-1);
	}
		
	@Override
	public String toString() {
		return "distance = "+getValue();
	}
}
