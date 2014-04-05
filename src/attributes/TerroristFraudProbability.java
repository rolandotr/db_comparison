package attributes;

import attributes.scales.LogScale;

public class TerroristFraudProbability extends DoubleAttribute{

	public TerroristFraudProbability(double value){
		super(value);
		setScale(new LogScale<DoubleAttribute>(2));
	}


	@Override
	public TerroristFraudProbability getInstance() {
		return new TerroristFraudProbability(-1);
	}
	@Override
	public String toString() {
		return "terrorist = "+getValue();
	}
}
