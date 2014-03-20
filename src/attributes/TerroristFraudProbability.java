package attributes;

public class TerroristFraudProbability extends DoubleAttribute{

	public TerroristFraudProbability(double value){
		super(value);
	}


	@Override
	public TerroristFraudProbability getInstance() {
		return new TerroristFraudProbability(-1);
	}
}
