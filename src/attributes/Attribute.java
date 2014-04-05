package attributes;

import attributes.scales.Scale;

public abstract class Attribute {
	
	
	public abstract Attribute getInstance();

	public static Attribute[] getEmptyAttributes(){
		return new Attribute[]{
				new DistanceFraudProbability(0),
				new FinalSlowPhase(false),
				new MafiaFraudProbability(0),
				new Memory(0),
				new TerroristFraudProbability(0),
				//new TotalBitsExchanged(0),
				new YearOfPublication(0),
		};		
	}


}
