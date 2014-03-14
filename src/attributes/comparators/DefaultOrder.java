package attributes.comparators;

import attributes.Attribute;
import attributes.BooleanAttribute;
import attributes.DoubleAttribute;

public class DefaultOrder<T> implements OrderRelationship<Attribute>{

	@Override
	public int compare(Attribute o1, Attribute o2) {
		if (o1 instanceof DoubleAttribute){
			DoubleAttribute tmp1 = (DoubleAttribute)o1;
			DoubleAttribute tmp2 = (DoubleAttribute)o2;
			if (tmp1.getValue() < tmp2.getValue()) return -1;
			else if (tmp1.getValue() > tmp2.getValue()) return 1;
			else return 0;
		}
		else if (o1 instanceof BooleanAttribute){
			BooleanAttribute tmp1 = (BooleanAttribute)o1;
			BooleanAttribute tmp2 = (BooleanAttribute)o2;
			if (tmp1.getValue() && !tmp2.getValue()) return 1;
			else if (!tmp1.getValue() && tmp2.getValue()) return -1;
			else return 0;
		}
		throw new RuntimeException("Uncomparable objects "+o1+" vs "+o2);
	}
	
}
