package attributes;

import attributes.scales.LogScale;

public class MafiaFraudProbability extends DoubleAttribute{

	public MafiaFraudProbability(double value){
		super(value);
		setScale(new LogScale<DoubleAttribute>(2));
	}

	@Override
	public MafiaFraudProbability getInstance() {
		return new MafiaFraudProbability(-1);
	}
	@Override
	public String toString() {
		return "mafia = "+getValue();
	}
}
