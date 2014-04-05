package attributes.comparators;

import attributes.Attribute;
import attributes.BooleanAttribute;
import attributes.DoubleAttribute;

/*Trujillo- Mar 17, 2014
 * In this class we basically say that: i) in case of numerical attributes the 
 * lower the better (true for memory, attack success probability and number of crypto calls) 
 * ii) in case of a boolean attribute false is better than true (true for presence of
 * second slow phase, more than one bit exchange). 
 * Note that, this is very simplistic. Others kind of relationships might be defined, though.*/
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
