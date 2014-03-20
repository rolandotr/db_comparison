package attributes;

public class MafiaFraudProbability extends DoubleAttribute{

	public MafiaFraudProbability(double value){
		super(value);
	}

	@Override
	public MafiaFraudProbability getInstance() {
		return new MafiaFraudProbability(-1);
	}
}
