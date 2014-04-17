package methodology;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import attributes.Attribute;
import attributes.comparators.DefaultOrder;
import attributes.comparators.OrderRelationship;
import protocols.DBProtocol;

public class ParetoFrontier implements Serializable{
	
	private static final long serialVersionUID = -260614203005100896L;
	
	private DBProtocol[] protocols;
	private DBProtocol[] frontier;
	private TreeMap<Integer, List<Integer>> indexesToBeRemoved;
	private DominantRelationship relationship;
	



	public ParetoFrontier(DBProtocol[] protocols, DBProtocol[] frontier, TreeMap<Integer, 
			List<Integer>> indexesToBeRemoved, DominantRelationship relationship){
		this.protocols = protocols;
		this.frontier = frontier;
		this.indexesToBeRemoved = indexesToBeRemoved;
		this.relationship = relationship;
	}

	
	/*Trujillo- Apr 4, 2014
	 * Compute the pareto frontier according to some attributes and order*/
	public static ParetoFrontier computeParetoFrontier(DBProtocol[] protocols, OrderRelationship<Attribute> order, Attribute[] attributes, int n){
		DominantRelationship relationship = new DominantRelationship(order, protocols, attributes, n);
		TreeMap<Integer, List<Integer>> indexesToBeRemoved = new TreeMap<>();
		for (int i = 0; i < protocols.length; i++) {
			for (int j = 0; j < protocols.length; j++) {
				if (relationship.isDominating(protocols[i], protocols[j], n)){
					if (indexesToBeRemoved.containsKey(j)) 
						indexesToBeRemoved.get(j).add(i);
					else {
						List<Integer> tmp = new LinkedList<>();
						tmp.add(i);
						indexesToBeRemoved.put(j, tmp);
					}
				}
			}
		}
		DBProtocol[] result = new DBProtocol[protocols.length - indexesToBeRemoved.size()];
		int pos = 0;
		for (int i = 0; i < protocols.length; i++) {
			if (indexesToBeRemoved.containsKey(i)) {
				continue;
			}
			result[pos] = protocols[i];
			pos++;
		}
		return new ParetoFrontier(protocols, result, indexesToBeRemoved, relationship);
	}
	
	public static ParetoFrontier[] computeAllParetoFrontiers(DBProtocol[] protocols, OrderRelationship<Attribute> order, Attribute[] attributes, int maxN){
		ParetoFrontier[] result = new ParetoFrontier[maxN];
		for (int i = 1; i <= maxN; i++) {
			result[i-1] = computeParetoFrontier(protocols, order, attributes, i);
		}
		return result;
	}
	
	/*Trujillo- Apr 3, 2014
	 * Here the protocols are defined in the class DBprotocols, and the order is the default order*/
	public static ParetoFrontier computeParetoFrontier(int factor, Attribute[] attributes, int n){
		DBProtocol[] protocols = DBProtocol.loadProtocols(factor);
		OrderRelationship<Attribute> order = new DefaultOrder();
		return computeParetoFrontier(protocols, order, attributes, n);
	}
	
	public DBProtocol[] getProtocols() {
		return protocols;
	}


	public DBProtocol[] getFrontier() {
		return frontier;
	}


	public TreeMap<Integer, List<Integer>> getIndexesToBeRemoved() {
		return indexesToBeRemoved;
	}

	public DominantRelationship getRelationship() {
		return relationship;
	}
	public void setProtocols(DBProtocol[] protocols) {
		this.protocols = protocols;
	}


	public void setFrontier(DBProtocol[] frontier) {
		this.frontier = frontier;
	}


	public void setIndexesToBeRemoved(
			TreeMap<Integer, List<Integer>> indexesToBeRemoved) {
		this.indexesToBeRemoved = indexesToBeRemoved;
	}


	public void setRelationship(DominantRelationship relationship) {
		this.relationship = relationship;
	}

}
