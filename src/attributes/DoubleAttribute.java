package attributes;

public abstract class DoubleAttribute extends Attribute{

	private double value;
	
	public DoubleAttribute(double value){
		this.value = value;
	}
	
	public double getValue(){
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
}
