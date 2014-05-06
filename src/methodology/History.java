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

import protocols.DBProtocol;
import utils.Latex;
import attributes.Attribute;
import attributes.comparators.DefaultNondominantRelation;
import attributes.comparators.NondominantRelationship;
import methodology.ProtocolNonDominantRelationship;
import methodology.ParetoFrontier;

/*Trujillo- Apr 16, 2014
 * Since this class considers the history of the DB protocols, it normally uses the attribute year.*/
public class History {

	public static final int MAX_N = 128;

	public static void main(String[] args) throws IOException {
		
		//System.setOut(new PrintStream("test_with_128.txt"));
		System.setOut(new PrintStream("history.txt"));
		DBProtocol[] protocols = DBProtocol.loadProtocols(MAX_N*2);
		//DBProtocol[] protocols = DBProtocol.loadProtocols(100);
		NondominantRelationship order = new DefaultNondominantRelation();
		Attribute[] attributes = Attribute.getEmptyAttributesWithScales();
		
		ParetoFrontier[] frontiers = ParetoFrontier.computeAllParetoFrontiers(protocols, order, attributes, MAX_N);
		
		saveInDiskTheFrontiers(frontiers, "history.obj");
		
		
		printLatexTable(protocols, order, attributes, frontiers, MAX_N, 0, 8, "history_table.tex");
		printHistory(protocols, order, attributes, frontiers, MAX_N);

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
	public static void printHistory(DBProtocol[] protocols, NondominantRelationship order, 
			Attribute[] attributes, ParetoFrontier[] frontiers, int maxN){
		
		ParetoFrontier before = new ParetoFrontier(protocols, protocols, null, frontiers[0].getRelationship());
		for (int i = 1; i <= maxN; i++) {
			System.out.println("Computing pareto frontier for n = "+i);
			System.out.println("");
			ParetoFrontier frontier = frontiers[i-1];
			System.out.println("");
			if (before != null){
				basicInfo(frontier.getFrontier(), before.getFrontier(), i);
				System.out.println("");
				provideReasons(frontier, before, i);			
				System.out.println("");
			}
			before = frontier;
		}
	}
	
	/*Trujillo- Apr 5, 2014
	 * Print the history in a latex table format*/
	public static void printLatexTable(DBProtocol[] protocols, NondominantRelationship order, 
			Attribute[] attributes, ParetoFrontier[] frontiers, int maxN, int remains, int module, String name) throws IOException{
		FileWriter writer = new FileWriter(name);
		//first, we print the header
		Latex.appendTableHeader(writer, attributes);		
		for (int i = 1; i <= maxN; i++) {
			if (i % module != remains && i != 1) continue;
			List<Integer> toIgnore = new ArrayList<>();
			//the frontier
			DBProtocol[] frontier = frontiers[i-1].getFrontier();
			List<List<DBProtocol>> clusters = new LinkedList<>();
			for (int j = 0; j < frontier.length; j++) {
				//protocols that already belong to a cluster
				if (toIgnore.contains(j)) continue;
				List<DBProtocol> cluster = new LinkedList<>();
				//the cluster starts with someone in the frontier
				cluster.add(frontier[j]);
				for (int k = j+1; k < frontier.length; k++) {
					if (frontiers[i-1].getRelationship().isEqual(frontier[j], frontier[k], i)){
						//equals protocols should be in the same cluster
						cluster.add(frontier[k]);
						toIgnore.add(k);
					}
				}
				clusters.add(cluster);
			}
			Latex.appendClusters(clusters, attributes, i, writer);
		}
		Latex.appendTableFooter(writer);
		writer.close();
	}

	/*Trujillo- Apr 4, 2014
	 * For each protocol entering or leaving the frontier, we show the causes. \
	 * A cause for a protocol P could be either the protocols that remove P previously and don't 
	 * do it now, or the protocols the didn't remove it and remove it now.*/
	private static void provideReasons(ParetoFrontier current,
			ParetoFrontier before, int i) {
		List<DBProtocol> entering = getProtocolsIn(before.getFrontier(), current.getFrontier());
		List<DBProtocol> leaving = getProtocolsOut(before.getFrontier(), current.getFrontier());
		//now we need to look for the indexes of this protocols.
		int[] indexesIn = getIndexesOfProtocols(current.getProtocols(), entering);
		int[] indexesOut = getIndexesOfProtocols(current.getProtocols(), leaving);
		provideReasonsForIn(indexesIn, current, before, i);
		provideReasonsForOut(indexesOut, current, before, i);
	}

	private static void provideReasonsForIn(int[] indexesIn,
			ParetoFrontier current, ParetoFrontier before, int n) {
		for (int i = 0; i < indexesIn.length; i++) {
			DBProtocol p = current.getProtocols()[indexesIn[i]];
			System.out.println("Analyzing why protocol: "+p+" is now in");
			List<Integer> causes = before.getIndexesToBeRemoved().get(indexesIn[i]);
			System.out.println("First, let see what happened when n = "+(n-1));
			ProtocolNonDominantRelationship previousRelation = before.getRelationship();
			for (Integer index : causes) {
				DBProtocol cause = current.getProtocols()[index];
				previousRelation.printInfoOfDomination(cause, p, n-1);
			}
			System.out.println("Now, let see what happened when n = "+(n));
			ProtocolNonDominantRelationship currentRelation = current.getRelationship();
			for (Integer index : causes) {
				DBProtocol cause = current.getProtocols()[index];
				currentRelation.printInfoOfNonDomination(cause, p, n);
			}
		}
	}

	private static void provideReasonsForOut(int[] indexesOut,
			ParetoFrontier current, ParetoFrontier before, int n) {
		for (int i = 0; i < indexesOut.length; i++) {
			DBProtocol p = current.getProtocols()[indexesOut[i]];
			System.out.println("Analyzing why protocol: "+p+" is now out");
			List<Integer> causes = current.getIndexesToBeRemoved().get(indexesOut[i]);
			System.out.println("First, let see what happened when n = "+(n-1));
			ProtocolNonDominantRelationship previousRelation = before.getRelationship();
			for (Integer index : causes) {
				DBProtocol cause = current.getProtocols()[index];
				previousRelation.printInfoOfNonDomination(cause, p, n-1);
			}
			System.out.println("Now, let see what happened when n = "+(n));
			ProtocolNonDominantRelationship currentRelation = current.getRelationship();
			for (Integer index : causes) {
				DBProtocol cause = current.getProtocols()[index];
				currentRelation.printInfoOfDomination(cause, p, n);
			}
		}
	}

	private static int[] getIndexesOfProtocols(DBProtocol[] protocols,
			List<DBProtocol> subset) {
		int[] result = new int[subset.size()];
		int pos = 0;
		for (DBProtocol p : subset) {
			for (int i = 0; i < protocols.length; i++) {
				if (p.equals(protocols[i])){
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
			basicInfo(frontier[i], before, i+1);
			before = frontier[i];
		}
		System.out.println(toPrint);
	}

	/*Trujillo- Apr 4, 2014
	 * This just provides basic info such as the protocol in the frontier and the protocols that left or enter the frontier*/
	public static void basicInfo(DBProtocol[] frontier, DBProtocol[] before, int n){
		String newLine = System.getProperty("line.separator");
		String toPrint = "";
		toPrint += "n = "+(n)+newLine;
		toPrint += "\t Protocols: "+getProtocolInList(frontier);			
		toPrint += newLine;
		toPrint += "\t In: "+getProtocolInList(getProtocolsIn(before, frontier));
		toPrint += newLine;
		toPrint += "\t Out: "+getProtocolInList(getProtocolsOut(before, frontier));
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
			if (p.equals(list[i])) return true;
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
