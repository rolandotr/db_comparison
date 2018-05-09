package protocols.automata_based_specifications;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class State {

	private int identifier;
	
	private String label; 
	
	Map<String, State> transition;
	
	public State(int identifier, String label) {
		this.identifier = identifier;
		this.label = label;
		transition = new TreeMap<String, State>();
	}
	
	
	public void setTransition(String label, State to) {
		if (transition.containsKey(label)) throw new RuntimeException("Transition with label "+label+" has been already set");
		transition.put(label, to);
	}

	public State next(String label) {
		if (!transition.containsKey(label)) throw new RuntimeException("Transition with label "+label+" has not been found");
		return transition.get(label);
	}


	public String getLabel() {
		return label;
	}
	
}
