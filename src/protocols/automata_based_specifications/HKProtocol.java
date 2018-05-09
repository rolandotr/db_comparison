package protocols.automata_based_specifications;

import java.util.LinkedList;
import java.util.List;

import utils.BinarySeqEnum;

public class HKProtocol {

	List<HKAutomaton> automata; 
	
	private int n;
	
	public HKProtocol(int n, boolean labelClosure) {
		if (labelClosure) HKProtocolClosure(n);
		else HKProtocolNONClosure(n);
	}
	
	private void HKProtocolClosure(int n) {
		this.n = n;
		automata = new LinkedList<HKAutomaton>();
		BinarySeqEnum edgeLabels = new BinarySeqEnum(2*n-1);
		while (edgeLabels.hasMoreElements()) {
			String edgeLabelSeq = edgeLabels.nextElement();
			BinarySeqEnum vertexLabels = new BinarySeqEnum(2*n);
			while (vertexLabels.hasMoreElements()) {
				String vertexLabelSeq = vertexLabels.nextElement();
				automata.add(new HKAutomaton(n, edgeLabelSeq, vertexLabelSeq));
			}			
		}
	}
	
	private void HKProtocolNONClosure(int n) {
		this.n = n;
		automata = new LinkedList<HKAutomaton>();

		char[] edgeLabelSeqTmp = new char[2*n-1];
		for (int i = 0; i < edgeLabelSeqTmp.length; i++) {
			edgeLabelSeqTmp[i] = '0';
		}

		String edgeLabelSeq = new String(edgeLabelSeqTmp);
		BinarySeqEnum vertexLabels = new BinarySeqEnum(2*n);
		while (vertexLabels.hasMoreElements()) {
			String vertexLabelSeq = vertexLabels.nextElement();
			automata.add(new HKAutomaton(n, edgeLabelSeq, vertexLabelSeq));
		}			
	}
	
	public double mafiaFraudProb() {
		
		long positive = 0;
		long negative = 0;
		
		char[] advChallengeTmp = new char[n];
		for (int i = 0; i < advChallengeTmp.length; i++) {
			advChallengeTmp[i] = '0';
		}
		String advChallenge = new String(advChallengeTmp);
		for (HKAutomaton chosenAutomaton: automata) {
			
			System.out.println("Chosen automaton");
			System.out.println(chosenAutomaton.toString());
			
			String advInput = chosenAutomaton.query(advChallenge);
			List<HKAutomaton> advSolutionSpace = computeAdvSolutionSpace(advChallenge, advInput);

			for (HKAutomaton advAutomaton: advSolutionSpace) {
				
				System.out.println("Adversary automaton");
				System.out.println(advAutomaton.toString());
				long positiveRecord = positive;
				long negativeRecord = negative;
				
				BinarySeqEnum verifierChallenges = new BinarySeqEnum(n);
				while (verifierChallenges.hasMoreElements()) {
					String challenge = verifierChallenges.nextElement();
					String expectedReply = chosenAutomaton.query(challenge);
					String advReply = advAutomaton.query(challenge);
					if (expectedReply.equals(advReply)) positive++;
					else negative++;
				}
				System.out.println("Positive for this pair is: "+(positive-positiveRecord)+", negative = "+(negative-negativeRecord));
			}
			System.out.println("");
			System.out.println("");
		}
		return ((double)positive/(positive+negative));
	}


	private List<HKAutomaton> computeAdvSolutionSpace(String advChallenge, String advInput) {
		List<HKAutomaton> result = new LinkedList<>();
		for (HKAutomaton candidate : automata) {
			if (candidate.query(advChallenge).equals(advInput)) {
				result.add(candidate);
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		HKProtocol hk = new HKProtocol(3, true);
		HKAutomaton chosenAutomaton = new HKAutomaton(1, "0", "11");
		String advInput = chosenAutomaton.query("0");
		List<HKAutomaton> advSolutionSpace = hk.computeAdvSolutionSpace("0", advInput);

		System.out.println(hk.mafiaFraudProb());
	}
	
}
