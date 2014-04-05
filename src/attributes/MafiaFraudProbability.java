package attributes;

import attributes.scales.DoubleScale;

public class MafiaFraudProbability extends DoubleAttribute{


	public MafiaFraudProbability(double value){
		super(value);
	}
	
	public MafiaFraudProbability(double value, DoubleScale scale){
		super(value, scale);
	}

	@Override
	public MafiaFraudProbability getInstance() {
		return new MafiaFraudProbability(-1, scale);
	}
	
	@Override
	public String toString() {
		return "mafia = "+getValue();
	}
}
