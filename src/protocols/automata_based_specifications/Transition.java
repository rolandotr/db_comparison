package protocols.automata_based_specifications;

public class Transition {

	
	private State from;
	private State to;
	private String label;
	
	
	public Transition(State from, String label, State to) {
		super();
		this.from = from;
		this.to = to;
		this.label = label;
	}
	
	
}
