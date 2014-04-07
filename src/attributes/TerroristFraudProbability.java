package attributes;

import attributes.scales.Scale;

public class TerroristFraudProbability extends Attribute<Double>{

	public TerroristFraudProbability(double value, Scale<Double> scale){
		super(value, scale);
	}


	@Override
	public TerroristFraudProbability getInstance() {
		return new TerroristFraudProbability(value, scale);
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
