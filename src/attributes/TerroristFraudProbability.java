package attributes;

import attributes.scales.DoubleScale;

public class TerroristFraudProbability extends DoubleAttribute{

	public TerroristFraudProbability(double value, DoubleScale scale){
		super(value, scale);
	}


	@Override
	public TerroristFraudProbability getInstance() {
		return new TerroristFraudProbability(-1, scale);
	}
	@Override
	public String toString() {
		return "terrorist = "+getValue();
	}
}
