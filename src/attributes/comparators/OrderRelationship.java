package attributes.comparators;

import java.util.Comparator;

import attributes.Attribute;

/*Trujillo- Mar 17, 2014
 * This interface must be implemented by those classes aimed at defining an 
 * order relationship between attributes. See for example the class 
 * DefaultOrder.*/
public interface OrderRelationship<T extends Attribute> extends Comparator<T>{
	
}
