package protocols.automata_based_specifications;

public class HKAutomaton extends Automaton{
	
	private int n;
	private String edgeLabelSeq;
	private String vertexLabelSeq;
	
	public HKAutomaton(int n) {
		this.n = n;
		if (n < 1) throw new RuntimeException("n cannot be lower than 1");
		startingState = new State(0, "none");
		State stateLeft = new State(1, "none");
		State stateRight = new State(2, "none");
		startingState.setTransition("0", stateLeft);
		startingState.setTransition("1", stateRight);
		State tmpLeft = null;
		State tmpRight = null;
		for (int i = 2; i <= n; i++) {
			tmpLeft = new State(2*i-1, "none");
			tmpRight = new State(2*i, "none");
			stateLeft.setTransition("0", tmpLeft);
			stateLeft.setTransition("1", tmpRight);
			stateRight.setTransition("0", tmpLeft);
			stateRight.setTransition("1", tmpRight);
			stateLeft = tmpLeft;
			stateRight = tmpRight;
		}
	}

	public HKAutomaton(int n, String edgeLabelSeq, String vertexLabelSeq) {
		this.n = n;
		this.edgeLabelSeq = edgeLabelSeq;
		this.vertexLabelSeq = vertexLabelSeq;
		if (n < 1) throw new RuntimeException("n cannot be lower than 1");
		if (2*n-1 != edgeLabelSeq.length()) throw new RuntimeException("The sequence of edge labels is different to "+(2*n-1));
		if (2*n != vertexLabelSeq.length()) throw new RuntimeException("The sequence of vertex labels is different to "+(2*n));
		startingState = new State(-1, "none");
		char[] edgeLabels = edgeLabelSeq.toCharArray();
		char[] vertexLabels = vertexLabelSeq.toCharArray();
		
		State stateLeft = new State(0, vertexLabels[0]+"");
		State stateRight = new State(1, vertexLabels[1]+"");
		String leftLabel = edgeLabels[0]+"";
		String rightLabel = (leftLabel.equals("0"))?"1":"0";
		
		connectState(startingState, stateLeft, stateRight, leftLabel, rightLabel);

		State tmpLeft = null;
		State tmpRight = null;
		for (int i = 1; i < n; i++) {
			tmpLeft = new State(2*i, vertexLabels[2*i]+"");
			tmpRight = new State(2*i+1, vertexLabels[2*i+1]+"");
			leftLabel = edgeLabels[2*i-1]+"";
			rightLabel = (leftLabel.equals("0"))?"1":"0";
			
			connectState(stateLeft, tmpLeft, tmpRight, leftLabel, rightLabel);
			
			leftLabel = edgeLabels[2*i-1]+"";
			rightLabel = (leftLabel.equals("0"))?"1":"0";

			connectState(stateRight, tmpLeft, tmpRight, leftLabel, rightLabel);

			stateLeft = tmpLeft;
			stateRight = tmpRight;
		}
	}
	
	protected void connectState(State from, State left, State right, String leftLabel, String rightLabel) {
		from.setTransition(leftLabel, left);
		from.setTransition(rightLabel, right);
	}

	public String query(String input) {
		String output = "";
		char[] symbols = input.toCharArray();
		State currentState = startingState;
		for (int i = 0; i < symbols.length; i++) {
			currentState = currentState.next(symbols[i]+"");
			output += currentState.getLabel();
		}
		return output;
	}
	
	@Override
	public String toString() {
		return "[Edges: "+edgeLabelSeq+", Vertices: "+vertexLabelSeq+"]";		
	}
	
}
