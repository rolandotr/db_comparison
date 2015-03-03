package methodology;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

import utils.Progress;

public class InternalState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1605741257517837188L;
	
	public int i;
	public Progress progress;
	public String identifier;
	public TreeMap<Integer, List<Integer>> indexesToBeRemoved;
	
	public InternalState(String identifier){
		this.identifier = identifier;
	}
	
	public void setIndexesToBeRemoved(TreeMap<Integer, List<Integer>> indexesToBeRemoved){
		this.indexesToBeRemoved = indexesToBeRemoved;
	}
	
	public void setCoordinates(int i){
		this.i = i;
	}
	
	public void setProgress(Progress progress){
		this.progress = progress;
	}
}
