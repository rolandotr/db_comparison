package attributes;

import attributes.scales.Scale;

public class MafiaFraudProbability extends Attribute<Double>{


	public MafiaFraudProbability(double value, Scale<Double> scale){
		super(value, scale);
	}

	@Override
	public MafiaFraudProbability getInstance() {
		return new MafiaFraudProbability(value, super.scale);
	}
	
	@Override
	public String toString() {
		return "mafia = "+getValue();
	}
	@Override
	public String getName() {
		return "Mafia";
	}
	
}
