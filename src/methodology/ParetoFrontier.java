package methodology;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import protocols.specifications.DBProtocol;
import utils.Progress;

import attributes.Attribute;

public class ParetoFrontier implements Serializable{
	
	private static final long serialVersionUID = -260614203005100896L;
	
	private DBProtocol[] protocols;
	private DBProtocol[] frontier;
	private TreeMap<Integer, List<Integer>> indexesToBeRemoved;
	private Attribute[] attributes;

	public Attribute[] getAttributes() {
		return attributes;
	}


	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}


	public ParetoFrontier(DBProtocol[] protocols, DBProtocol[] frontier, Attribute[] attributes, TreeMap<Integer, 
			List<Integer>> indexesToBeRemoved){
		this.protocols = protocols;
		this.frontier = frontier;
		this.indexesToBeRemoved = indexesToBeRemoved;
		this.attributes = attributes;
	}

	
	/*Trujillo- Apr 4, 2014
	 * Compute the pareto frontier according to some attributes and order*/
	public static ParetoFrontier computeParetoFrontier(DBProtocol[] protocols, Attribute[] attributes, InternalState state) throws FileNotFoundException, IOException{
		TreeMap<Integer, List<Integer>> indexesToBeRemoved = state.indexesToBeRemoved;
		Progress progress = state.progress;
		for (int i = 0; i < protocols.length; i++) {
			if (i <= state.i) continue;
			for (int j = 0; j < protocols.length; j++) {
				progress.addProgress();
				if (!indexesToBeRemoved.containsKey(j)){ 
					List<Integer> tmp = new LinkedList<>();
					indexesToBeRemoved.put(j, tmp);
				}
				if (protocols[i].dominate(protocols[j], attributes)){
					/*
					System.out.println(protocols[i].getIdentifier()+" dominates "+protocols[j].getIdentifier());
					History.printInfoOfDomination(protocols[i], protocols[j], attributes);
					*/
					indexesToBeRemoved.get(j).add(i);
				}
			}
			progress.printProgress();
			state.setCoordinates(i);
			state.setProgress(progress);
			state.setIndexesToBeRemoved(indexesToBeRemoved);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(state.identifier));
			out.writeObject(state);
			out.close();
			out = new ObjectOutputStream(new FileOutputStream(state.identifier+"Backup"));
			out.writeObject(state);
			out.close();
			//if (i > 20) break;  
		}
		List<DBProtocol> result = new LinkedList<>();
		for (int i = 0; i < protocols.length; i++) {
			if (!(indexesToBeRemoved.get(i).isEmpty())) {
				continue;
			}
			result.add(protocols[i]);
		}
		DBProtocol[] tmp = new DBProtocol[result.size()];
		return new ParetoFrontier(protocols, result.toArray(tmp), attributes, indexesToBeRemoved);
	}
	
	public static ParetoFrontier computeParetoFrontier(DBProtocol[] protocols, Attribute[] attributes) throws FileNotFoundException, IOException{
		TreeMap<Integer, List<Integer>> indexesToBeRemoved = new TreeMap<>();
		Progress progress = new Progress(protocols.length*protocols.length);
		for (int i = 0; i < protocols.length; i++) {
			for (int j = 0; j < protocols.length; j++) {
				progress.addProgress();
				if (!indexesToBeRemoved.containsKey(j)){ 
					List<Integer> tmp = new LinkedList<>();
					indexesToBeRemoved.put(j, tmp);
				}
				if (protocols[i].dominate(protocols[j], attributes)){
					/*
					System.out.println(protocols[i].getIdentifier()+" dominates "+protocols[j].getIdentifier());
					History.printInfoOfDomination(protocols[i], protocols[j], attributes);
					*/
					indexesToBeRemoved.get(j).add(i);
				}
			}
			progress.printProgress();
			//if (i > 20) break;  
		}
		List<DBProtocol> result = new LinkedList<>();
		for (int i = 0; i < protocols.length; i++) {
			if (!(indexesToBeRemoved.get(i).isEmpty())) {
				continue;
			}
			result.add(protocols[i]);
		}
		DBProtocol[] tmp = new DBProtocol[result.size()];
		return new ParetoFrontier(protocols, result.toArray(tmp), attributes, indexesToBeRemoved);
	}
	
	public static ParetoFrontier[] computeAllParetoFrontiers(DBProtocol[][] protocols, 
			Attribute[] attributes, InternalState state) throws FileNotFoundException, IOException{
		ParetoFrontier[] result = new ParetoFrontier[protocols.length];
		long total = 0;
		for (int i = 0; i < protocols.length; i++) {
			for (int j = 0; j < protocols[i].length; j++) {
				total++;
			}
		}
		state.progress.setTotal(total*total);
		for (int i = 0; i < protocols.length; i++) {
			result[i] = computeParetoFrontier(protocols[i], attributes, state);
		}
		return result;
	}
	public static ParetoFrontier[] computeAllParetoFrontiers(DBProtocol[][] protocols, 
			Attribute[] attributes) throws FileNotFoundException, IOException{
		ParetoFrontier[] result = new ParetoFrontier[protocols.length];
		long total = 0;
		for (int i = 0; i < protocols.length; i++) {
			for (int j = 0; j < protocols[i].length; j++) {
				total++;
			}
		}
		for (int i = 0; i < protocols.length; i++) {
			result[i] = computeParetoFrontier(protocols[i], attributes);
		}
		return result;
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


}
