package attributes;

import attributes.scales.DoubleScale;

public abstract class DoubleAttribute extends Attribute{

	private double value;
	public DoubleScale scale;
	
	public DoubleAttribute(double value, DoubleScale scale){
		this.value = scale.scale(value);
		this.scale = scale;
	}
	
	public double getValue(){
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
}
