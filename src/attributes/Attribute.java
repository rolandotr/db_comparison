package attributes;

import attributes.scales.LogScale;
import attributes.scales.NoScale;
import attributes.scales.PolynomialScale;

public abstract class Attribute {
	
	
	public abstract Attribute getInstance();

	/*Trujillo- Apr 5, 2014
	 * Attributes with the our scales*/
	public static Attribute[] getEmptyAttributesWithScales(){
		return new Attribute[]{
				new DistanceFraudProbability(0, new LogScale(2)),
				new FinalSlowPhase(false),
				new MafiaFraudProbability(0, new LogScale(2)),
				new Memory(0, new PolynomialScale(64)),
				new TerroristFraudProbability(0, new LogScale(2)),
				//new TotalBitsExchanged(0),
				new YearOfPublication(0, new NoScale()),
		};		
	}


}
