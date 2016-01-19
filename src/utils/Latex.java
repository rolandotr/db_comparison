package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import protocols.specifications.DBProtocol;

import attributes.Attribute;


public abstract class Latex {
	
	public static void appendTableHeader(FileWriter writer, Attribute[] attributes) throws IOException{
		String newLine = System.getProperty("line.separator");
		writer.append("\\begin{center}"+newLine);
		writer.append("\\begin{longtable}{ @{} l | c |");
		for (int i = 0; i < attributes.length; i++) {
			writer.append(" c");
		}
		writer.append("  @{}}"+newLine);
		writer.append("\\toprule"+newLine);
		writer.append("\\multirow{2}*{\\ $n$ \\ } &	\\multicolumn{1}{c}{Pareto}	" +
				" & \\multicolumn{"+attributes.length+"}{c}{Attributes}	\\\\"+newLine+" & Frontier ");
		for (int i = 0; i < attributes.length; i++) {
			writer.append(" & "+attributes[i].getName());
		}
		writer.append(" \\\\ \\cmidrule(l){1-"+(attributes.length+2)+"}"+newLine);
	}

	public static void appendClusters(List<List<DBProtocol>> clusters, Attribute[] attributes, int n, FileWriter writer) throws IOException {
		boolean goOn = false;
		for (List<DBProtocol> cluster : clusters) {
			if (!cluster.isEmpty()) goOn = true;
		}
		if (!goOn) return;
		String newLine = System.getProperty("line.separator");
		writer.append("\\multirow{"+clusters.size()+"}*{\\ $"+n+"$ \\ }"+newLine);
		for (List<DBProtocol> cluster : clusters) {
			appendCluster(cluster, attributes, n, writer);
		}
		writer.append(" \\\\ \\cmidrule(l){1-"+(attributes.length+2)+"}"+newLine);
	}

	private static void appendCluster(List<DBProtocol> cluster, Attribute[] attributes, 
			int n, FileWriter writer) throws IOException {
		String newLine = System.getProperty("line.separator");
		boolean first = true;
		writer.append(" & ");
		for (DBProtocol protocol : cluster) {
			if (first){
				writer.append(protocol.getIdentifier());
				first = false;
			}
			else writer.append(" "+protocol.getIdentifier()); 
		}
		//now we need to add the attributes values
		DBProtocol dominating = cluster.get(0);
		for (int i = 0; i < attributes.length; i++) {
			Attribute a = dominating.getAttribute(attributes[i]);
			writer.append(" & $"+a.getScale().scaleMeaning(a.getScaledValue())+"$ ");			
		}
		writer.append(" \\\\ "+newLine);
	}

	public static void appendTableFooter(FileWriter writer) throws IOException {		
		String newLine = System.getProperty("line.separator");
		writer.append("\\bottomrule"+newLine);
		writer.append("\\end{longtable}"+newLine);
		writer.append("\\end{center}"+newLine);
	}


}
