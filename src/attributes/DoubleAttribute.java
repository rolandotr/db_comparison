package attributes;

import attributes.scales.Scale;

public abstract class DoubleAttribute extends Attribute{

	protected Scale<DoubleAttribute> scale;

	private double value;
	
	public DoubleAttribute(double value){
		this.value = value;
	}
	
	public double getValue(){
		return scale.scale(this);
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public Scale<DoubleAttribute> getScale() {
		return scale;
	}

	public void setScale(Scale<DoubleAttribute> scale) {
		this.scale = scale;
	}
	
}
