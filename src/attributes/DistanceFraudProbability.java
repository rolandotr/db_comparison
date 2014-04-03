package attributes;

public class DistanceFraudProbability extends DoubleAttribute{

	public DistanceFraudProbability(double value){
		super(value);
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
