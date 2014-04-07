package methodology;

import java.io.Serializable;
import java.util.Hashtable;

import attributes.Attribute;
import attributes.comparators.OrderRelationship;
import protocols.DBProtocol;

/*Trujillo- Apr 3, 2014
 * This class represents the dominating relationship between protocols.*/
public class DominatingRelation implements Serializable{

	
	
	private static final long serialVersionUID = -5064422592064211421L;
	
	private DBProtocol[] protocols;
	private Attribute[] attributes;
	private Hashtable<String, Integer> indexes;
	private boolean domination[][];
	OrderRelationship<Attribute> order;
	
	public DominatingRelation(OrderRelationship<Attribute> order, DBProtocol[] protocols, Attribute[] attributes, int n){
		this.attributes = attributes;
		this.protocols = protocols;
		this.order = order;
		indexes = new Hashtable<>();
		domination = new boolean[protocols.length][protocols.length];
		createIndexes();
		createTable(n);
	}

	/*Trujillo- Apr 3, 2014
	 * Index are here just for make easier to refer to domination of protocols instead of working with
	 * indexes.*/
	private void createIndexes() {
		int index = 0;
		for (int i = 0; i < protocols.length; i++) {
			if (indexes.containsKey(protocols[i].getIdentifier()))
				throw new RuntimeException("that should not have happened!. This means that two different protocols share the same identifier!");
			indexes.put(protocols[i].getIdentifier(), index);
			index++;
			
		}
	}

	private void createTable(int n) {
		for (int i = 0; i < protocols.length; i++) {
			for (int j = 0; j < protocols.length; j++) {
				if (isDominating(protocols[i], protocols[j], n))
					domination[indexes.get(protocols[i].getIdentifier())][indexes.get(protocols[j].getIdentifier())] 
							= isDominating(protocols[i], protocols[j], n);
			}
		}
	}

	public boolean isDominating(DBProtocol a, DBProtocol b, int n) {
		for (int i = 0; i < attributes.length; i++) {
			Attribute aValue = a.getAttribute(attributes[i], n); 
			Attribute bValue = b.getAttribute(attributes[i], n);
			if (order.compare(aValue, bValue) > 0) {
				return false;
			}
		}
		/*Trujillo- Apr 3, 2014
		 * Now, let us check whether all are equals*/
		for (int i = 0; i < attributes.length; i++) {
			Attribute aValue = a.getAttribute(attributes[i], n); 
			Attribute bValue = b.getAttribute(attributes[i], n);
			if (order.compare(aValue, bValue) < 0) {
				//printInfoOfDomination(a, b, n);
				return true;
			}
		}
		//System.out.println("Protocol: "+a.getIdentifier()+" is equal to "+b.getIdentifier());
		return false;
	}

	public void printInfoOfDomination(DBProtocol a, DBProtocol b, int n) {
		System.out.println("");
		System.out.println("Protocol: "+a.getIdentifier()+" dominates Protocol: "+b.getIdentifier()+", the reasons below");
		for (int i = 0; i < attributes.length; i++) {
			Attribute aValue = a.getAttribute(attributes[i], n); 
			Attribute bValue = b.getAttribute(attributes[i], n);
			if (order.compare(aValue, bValue) <= 0) {
				System.out.println("Attribute "+aValue.toString()+" is better or equal to attribute "+bValue.toString());
			}
		}
		System.out.println("");
	}
	
	public void printInfoOfNonDomination(DBProtocol a, DBProtocol b, int n) {
		System.out.println("");
		System.out.println("Protocol: "+a.getIdentifier()+" DOES NOT dominate Protocol: "+b.getIdentifier()+", the reasons below");
		for (int i = 0; i < attributes.length; i++) {
			Attribute aValue = a.getAttribute(attributes[i], n); 
			Attribute bValue = b.getAttribute(attributes[i], n);
			if (order.compare(aValue, bValue) > 0) {
				System.out.println("Attribute "+aValue.toString()+" is worst than "+bValue.toString());
			}
			else if (order.compare(aValue, bValue) == 0) {
				System.out.println("Attribute "+aValue.toString()+" is equal to "+bValue.toString());
			}
		}
		System.out.println("");
	}

	public boolean isEqual(DBProtocol a, DBProtocol b, int n) {
		for (int i = 0; i < attributes.length; i++) {
			Attribute aValue = a.getAttribute(attributes[i], n); 
			Attribute bValue = b.getAttribute(attributes[i], n);
			if (order.compare(aValue, bValue) != 0) {
				return false;
			}
		}
		return false;
	}
}
