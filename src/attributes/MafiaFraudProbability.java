package attributes;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

public class MafiaFraudProbability extends Attribute<Double>{


	private static final long serialVersionUID = -3615554356375152832L;
	
	public MafiaFraudProbability(double value, ApproximateEquality<Double> equality, Scale<Double> scale){
		super(value, equality, scale);
	}

	public MafiaFraudProbability(ApproximateEquality<Double> equality, Scale<Double> scale){
		this(0, equality, scale);
	}

	@Override
	public MafiaFraudProbability getInstance() {
		return new MafiaFraudProbability(value, super.equality, scale);
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
