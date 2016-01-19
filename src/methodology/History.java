package methodology;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import protocols.specifications.DBProtocol;

import utils.Latex;
import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.NumberOfRounds;
import attributes.SizeOfMessages;
import attributes.TerroristFraudProbability;
import attributes.YearOfPublication;
import attributes.relations.FinalSlowPhaseRelation;
import attributes.relations.IntegerRelation;
import attributes.relations.MemoryRelation;
import attributes.relations.ProbabilityRelation;
import attributes.relations.SizeOfMessagesRelation;
import attributes.scales.KbitsScale;
import attributes.scales.LogScale;
import attributes.scales.NoScale;
import methodology.ParetoFrontier;

/*Trujillo- Apr 16, 2014
 * Since this class considers the history of the DB protocols, it normally uses the attribute year.*/
public class History {


	public static void main(String[] args) throws IOException {
		
		System.setOut(new PrintStream("history.txt"));
		DBProtocol[][] protocols = DBProtocol.loadProtocolsFairly();
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new NumberOfRounds(new IntegerRelation(), new NoScale<Integer>()),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new Memory(new MemoryRelation(), new KbitsScale()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
				new YearOfPublication(new IntegerRelation(), new NoScale<Integer>()),
		};
		
		ParetoFrontier[] frontiers = ParetoFrontier.computeAllParetoFrontiers(protocols, attributes);
		
		saveInDiskTheFrontiers(frontiers, "history.obj");
		
		
		//printLatexTable(protocols, attributes, frontiers, 0, 8, "history_table.tex");
		//printHistory(protocols, attributes, frontiers);

		//TreeBasedProtocol tree = new TreeBasedProtocol();
		//System.out.println(tree.getMemory(63));
		//KimAndAvoineProtocol kim = new KimAndAvoineProtocol(0.25, 128);
		//DominatingRelation relation = new DominatingRelation(order, protocols, attributes, 64);
		//DBProtocol[] protocols = new DBProtocol[]{tree, kim};
		//relation.printInfoOfDomination(tree, kim, 63);
	}

	public static void saveInDiskTheFrontiers(ParetoFrontier[] frontiers,
			String file) throws FileNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		out.writeObject(frontiers);
		out.close();
	}

	/*Trujillo- Apr 4, 2014
	 * This method takes MAX_N and builds the history from n = 1 to n = MAX_N. This history is basically how the 
	 * pareto frontier evolves with n.*/
	public static void printHistory(DBProtocol[][] protocols,  
			Attribute[] attributes, ParetoFrontier[] frontiers){
		long total = 0; 
		for (int i = 0; i < frontiers.length; i++) {
			total += frontiers[i].getProtocols().length;
		}
		System.out.println("There are "+total+" nondominated protocol instances");
		ParetoFrontier before = null;
		for (int i = 0; i < protocols.length; i++) {
			//System.out.println("Computing pareto frontier for e = "+i);
			System.out.println("");
			ParetoFrontier frontier = frontiers[i];
			System.out.println("");
			
			if (before != null){
				basicInfo(frontier.getFrontier(), before.getFrontier());
				System.out.println("");
				provideReasons(frontier, before, attributes);			
				System.out.println("");
			}else 
				basicInfo(frontier.getFrontier()); 
			before = frontier;
		}
	}
	
	/*Trujillo- Apr 5, 2014
	 * Print the history in a latex table format*/
	public static void printLatexTable(ParetoFrontier[] frontiers, 
			int remains, int module, String name) throws IOException{
		if (frontiers.length == 0) return;
		FileWriter writer = new FileWriter(name);
		//first, we print the header
		Attribute[] attributes = frontiers[0].getAttributes();
		Latex.appendTableHeader(writer, attributes);		
		for (int i = 0; i < frontiers.length; i++) {
			DBProtocol[] pList = frontiers[i].getProtocols();
			int e = pList[0].getTotalBitsExchangedDuringFastPhase();
			if (e % module != remains && e != 2) continue;
			List<Integer> toIgnore = new ArrayList<>();
			//the frontier
			DBProtocol[] frontier = frontiers[i].getFrontier();
			//if (frontier.length == 0) continue;
			List<List<DBProtocol>> clusters = new LinkedList<>();
			for (int j = 0; j < frontier.length; j++) {
				//protocols that already belong to a cluster
				if (toIgnore.contains(j)) continue;
				List<DBProtocol> cluster = new LinkedList<>();
				//the cluster starts with someone in the frontier
				cluster.add(frontier[j]);
				for (int k = j+1; k < frontier.length; k++) {
					if (frontier[j].isEqual(frontier[k], attributes)){
						//equals protocols should be in the same cluster
						cluster.add(frontier[k]);
						toIgnore.add(k);
					}
				}
				clusters.add(cluster);
			}
			//if (clusters.isEmpty()) continue;
			Latex.appendClusters(clusters, attributes, e, writer);
		}
		Latex.appendTableFooter(writer);
		writer.close();
	}

	/*Trujillo- Apr 4, 2014
	 * For each protocol entering or leaving the frontier, we show the causes. \
	 * A cause for a protocol P could be either the protocols that remove P previously and don't 
	 * do it now, or the protocols the didn't remove it and remove it now.*/
	private static void provideReasons(ParetoFrontier current,
			ParetoFrontier before, Attribute[] attributes) {
		List<DBProtocol> entering = getProtocolsIn(before.getFrontier(), current.getFrontier());
		List<DBProtocol> leaving = getProtocolsOut(before.getFrontier(), current.getFrontier());
		//now we need to look for the indexes of this protocols.
		int[] indexesIn = getIndexesOfProtocols(current.getProtocols(), entering);
		int[] indexesOut = getIndexesOfProtocols(before.getProtocols(), leaving);
		provideReasonsForIn(indexesIn, current, before, attributes);
		//provideReasonsForOut(indexesOut, before, attributes);
	}

	private static void provideReasonsForIn(int[] indexesIn,
			ParetoFrontier current, ParetoFrontier before, Attribute[] attributes) {
		int ePrevious = before.getProtocols()[0].getTotalBitsExchangedDuringFastPhase();
		int eCurrent = current.getProtocols()[0].getTotalBitsExchangedDuringFastPhase();
		for (int i = 0; i < indexesIn.length; i++) {
			DBProtocol p = current.getProtocols()[indexesIn[i]];
			System.out.println("Analyzing why protocol: "+p+" is now in");
			//we need to find what was the version of this protocol previously.
			int indexBefore = findIndexOfSimilarProtocol(p, before);
			if (indexBefore == -1){
				System.out.println(p+" is just a new protocol that was not considered before");
				System.out.println("However, let see its relation with other protocols because" +
						" it should not be dominated by anyone");
				for (DBProtocol toCompareWith : current.getFrontier()){
					printInfoOfNonDomination(toCompareWith, p, attributes);
				}
			}
			else{
				List<Integer> causes = before.getIndexesToBeRemoved().get(indexBefore);
				System.out.println("First, let see what happened when e = "+(ePrevious));
				for (Integer index : causes) {
					DBProtocol cause = before.getProtocols()[index];
					printInfoOfDomination(cause, before.getProtocols()[indexBefore], attributes);
				}
				System.out.println("Now, let see what happened when e = "+(eCurrent));
				for (Integer index : causes) {
					DBProtocol cause = before.getProtocols()[index];
					int indexCurrent = findIndexOfSimilarProtocol(cause, current);
					if (indexCurrent == -1) {
						System.out.println("The protocol "+cause+" was dominating before, however, it is no longer considered");
					}
					else printInfoOfNonDomination(current.getProtocols()[indexCurrent], p, attributes);
				}
			}
		}
	}

	private static int findIndexOfSimilarProtocol(DBProtocol p,
			ParetoFrontier before) {
		for (int i = 0; i < before.getProtocols().length; i++) {
			if (p.equals(before.getProtocols()[i]))
				throw new RuntimeException("should not happen");
			if (p.isSameInstanceRegardlessRounds(before.getProtocols()[i])){
				return i;
			}
		}
		return -1;
	}

	public static void printInfoOfDomination(DBProtocol a, DBProtocol b, Attribute[] attributes) {
		System.out.println("");
		System.out.println("Protocol: "+a.getIdentifier()+" dominates Protocol: "+b.getIdentifier()+", the reasons below");
		for (int i = 0; i < attributes.length; i++) {
			Attribute aValue = a.getAttribute(attributes[i]); 
			Attribute bValue = b.getAttribute(attributes[i]);
			if (aValue.dominate(bValue)) {
				System.out.println("Attribute "+aValue.toString()+" dominates attribute "+bValue.toString());
			}
			else if (aValue.isEqual(bValue)){
				System.out.println("Attribute "+aValue.toString()+" is equal to attribute "+bValue.toString());
			}
			else{ 
				if (bValue.dominate(aValue)){
					throw new RuntimeException("Attribute "+bValue.toString()+" dominates attribute "+aValue.toString());
				}
				throw new RuntimeException("there is no domination here. " +
						"Attribute "+bValue.toString()+" in "+b.getIdentifier()+" " +
								"dominates attribute "+aValue.toString()+" in "+a.getIdentifier());
			}
		}
		System.out.println("");
	}
	
	public static void printInfoOfNonDomination(DBProtocol a, DBProtocol b, Attribute[] attributes) {
		System.out.println("");
		System.out.println("Protocol: "+a.getIdentifier()+" DOES NOT dominate Protocol: "+b.getIdentifier()+", the reasons below");
		for (int i = 0; i < attributes.length; i++) {
			Attribute aValue = a.getAttribute(attributes[i]); 
			Attribute bValue = b.getAttribute(attributes[i]);
			if (bValue.dominate(aValue)) {
				System.out.println("Attribute "+aValue.toString()+" is dominated by "+bValue.toString());
			}
			if (aValue.isEqual(bValue)) {
				System.out.println("Attribute "+aValue.toString()+" is equal to "+bValue.toString());
			}
		}
		System.out.println("");
	}

	public static void printInfo(DBProtocol p, Attribute[] attributes) {
		System.out.println("Protocol: "+p.getIdentifier());
		for (int i = 0; i < attributes.length; i++) {
			Attribute aValue = p.getAttribute(attributes[i]); 
			System.out.println("Attribute is equal to "+aValue.toString());
		}
		System.out.println("");
	}

	private static void provideReasonsForOut(int[] indexesOut,
			ParetoFrontier before, Attribute[] attributes) {
		int ePrevious = before.getProtocols()[0].getTotalBitsExchangedDuringFastPhase();
		for (int i = 0; i < indexesOut.length; i++) {
			DBProtocol p = before.getProtocols()[indexesOut[i]];
			System.out.println("Analyzing why protocol: "+p+" is now out");
			List<Integer> causes = before.getIndexesToBeRemoved().get(indexesOut[i]);
			System.out.println("Let see what happened when e = "+(ePrevious));
			for (Integer index : causes) {
				DBProtocol cause = before.getProtocols()[index];
				printInfoOfNonDomination(cause, p, attributes);
			}
			/*System.out.println("Now, let see what happened when e = "+(eCurrent));
			for (Integer index : causes) {
				DBProtocol cause = current.getProtocols()[index];
				printInfoOfDomination(cause, p, attributes);
			}*/
		}
	}

	private static int[] getIndexesOfProtocols(DBProtocol[] protocols,
			List<DBProtocol> subset) {
		int[] result = new int[subset.size()];
		int pos = 0;
		//System.out.println("Computing indexes ");
		for (DBProtocol p : subset) {
			for (int i = 0; i < protocols.length; i++) {
				if (p.equals(protocols[i])){
					//System.out.println("Protocol "+p.getIdentifier()+" equal to "+protocols[i].getIdentifier());
					result[pos] = i;
					pos++;
				}
			}
		}
		return result;
	}

	public static void history(DBProtocol[][] frontier){
		DBProtocol[] before = new DBProtocol[]{};
		String toPrint = "";
		for (int i = 0; i < frontier.length; i++) {
			basicInfo(frontier[i], before);
			before = frontier[i];
		}
		System.out.println(toPrint);
	}

	/*Trujillo- Apr 4, 2014
	 * This just provides basic info such as the protocol in the frontier and the protocols that left or enter the frontier*/
	public static void basicInfo(DBProtocol[] frontier, DBProtocol[] before){
		int e = frontier[0].getTotalBitsExchangedDuringFastPhase();
		String newLine = System.getProperty("line.separator");
		String toPrint = "";
		toPrint += "e = "+(e)+newLine;
		toPrint += frontier.length+" protocols in total";			
		toPrint += newLine;
		toPrint += "\t Protocols: "+getProtocolInList(frontier);			
		toPrint += newLine;
		toPrint += "\t In: "+getProtocolInList(getProtocolsIn(before, frontier));
		toPrint += newLine;
		toPrint += "\t Out: "+getProtocolInList(getProtocolsOut(before, frontier));
		toPrint += newLine;
		System.out.println(toPrint);
	}

	public static void basicInfo(DBProtocol[] frontier){
		int e = frontier[0].getTotalBitsExchangedDuringFastPhase();
		String newLine = System.getProperty("line.separator");
		String toPrint = "";
		toPrint += "e = "+(e)+newLine;
		toPrint += frontier.length+" protocols in total";			
		toPrint += newLine;
		toPrint += "\t Protocols: "+getProtocolInList(frontier);			
		toPrint += newLine;
		System.out.println(toPrint);
	}

	private static List<DBProtocol> getProtocolsIn(DBProtocol[] before,
			DBProtocol[] current) {
		List<DBProtocol> result = new LinkedList<>();
		for (int i = 0; i < current.length; i++) {
			if (!isIn(current[i], before)) result.add(current[i]);
		}
		
		return result;
	}

	private static List<DBProtocol> getProtocolsOut(DBProtocol[] before,
			DBProtocol[] current) {
		List<DBProtocol> result = new LinkedList<>();
		for (int i = 0; i < before.length; i++) {
			if (!isIn(before[i], current)) result.add(before[i]);
		}
		return result;
	}

	private static boolean isIn(DBProtocol p, DBProtocol[] list) {
		for (int i = 0; i < list.length; i++) {
			//if (p.equals(list[i])) return true;
			if (p.isSameInstanceRegardlessRounds(list[i])) return true;
		}
		return false;
	}

	private static String getProtocolInList(List<DBProtocol> protocols) {
		String result = "";
		for (DBProtocol p : protocols) {
			result += p.getIdentifier() + ", ";				
		}
		return result;
	}
	
	private static String getProtocolInList(DBProtocol[] protocols) {
		String result = "";
		for (DBProtocol p : protocols) {
			result += p.getIdentifier() + ", ";				
		}
		return result;
	}
}
