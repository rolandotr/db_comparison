package attributes;

import java.io.Serializable;

import attributes.scales.KbitsScale;
import attributes.scales.LogScale;
import attributes.scales.NoScale;
import attributes.scales.LinearScale;
import attributes.scales.Scale;

public abstract class Attribute<T> implements Serializable{

	
	private static final long serialVersionUID = 502686781523859675L;
	
	protected Scale<T> scale;
	protected T value;
	
	public Attribute(T value, Scale<T> scale){
		this.scale = scale;
		this.value = scale.scale(value);
	}
	
	public T getValue(){
		return value;
	}
	
	public Scale<T> getScale() {
		return scale;
	}

	public abstract Attribute<T> getInstance();

	/*Trujillo- Apr 5, 2014
	 * Attributes with the our scales*/
	public static Attribute[] getEmptyAttributesWithScales(){
		return new Attribute[]{
				new MafiaFraudProbability(0, new LogScale(2)),
				new DistanceFraudProbability(0, new LogScale(2)),
				new TerroristFraudProbability(0, new LogScale(2)),
				new Memory(0l, new KbitsScale()),
				new CryptoCalls(0, new NoScale<Integer>()),
				new FinalSlowPhase(false),
				new YearOfPublication(0, new NoScale<Integer>()),
		};		
	}

	public abstract String getName();

}
