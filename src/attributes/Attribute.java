package attributes;

import java.io.Serializable;

import attributes.relations.ApproximateEquality;
import attributes.scales.Scale;

/*Trujillo- May 21, 2014
 * An attribute has a comparable value and an equality relation over the domain of
 * this value.*/
public abstract class Attribute<T extends Comparable<T>> implements Serializable{

	
	private static final long serialVersionUID = 502686781523859675L;
	
	protected T value;
	protected ApproximateEquality<T> equality;
	protected Scale<T> scale;
	
	public Scale<T> getScale() {
		return scale;
	}

	public void setScale(Scale<T> scale) {
		this.scale = scale;
	}

	public Attribute(ApproximateEquality<T> equality, Scale<T> scale){
		this(null, equality, scale);
	}
	
	public Attribute(T value, ApproximateEquality<T> equality, Scale<T> scale){
		this.equality = equality;
		this.value = value;
		this.scale = scale;
	}
	
	public ApproximateEquality<T> getEquality() {
		return equality;
	}

	public void setEquality(ApproximateEquality<T> equality) {
		this.equality = equality;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue(){
		return value;
	}
	
	public abstract Attribute<T> getInstance();
	public abstract String getName();
	
	public boolean dominate(T x){
		return !equality.equal(this.getValue(), x) 
				&& value.compareTo(x) < 0;
	}

	public boolean dominate(Attribute<T> x){
		return !equality.equal(this.getValue(), x.getValue()) 
				&& value.compareTo(x.getValue()) < 0;
	}

	public boolean isEqual(Attribute<T> x) {
		return equality.equal(this.getValue(), x.getValue()); 
	}

	public T getScaledValue() {
		return scale.scale(value);
	}

}
