package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import protocols.specifications.DBProtocol;

import attributes.Attribute;
import attributes.DistanceFraudProbability;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.TerroristFraudProbability;
import attributes.relations.MemoryRelation;
import attributes.relations.ProbabilityRelation;
import attributes.scales.KbitsScale;
import attributes.scales.LogScale;

public class Data {
	
	/*Trujillo- Jun 3, 2014
	 * Create a data file for each protocol*/
	public static void printData(DBProtocol[] protocols, Attribute[] attributes, int maxN, int increment) throws IOException{
		String suffix = "";
		String newLine = System.getProperty("line.separator");
		for (Attribute attribute : attributes) {
			suffix += "-"+attribute.getName();
		}
		for (DBProtocol p : protocols) {
			FileWriter writer = new FileWriter(new File(p.getIdentifier()+suffix+"-increment-"+increment+".DAT"));
			writer.write("# n");
			for (Attribute attribute : attributes) {
				writer.append("\t "+attribute.getName());
			}
			writer.append(newLine);
			for (int i = 1; i <= maxN; i+=increment) {
				writer.append(""+i);
				for (Attribute attribute : attributes) {
					writer.append("\t "+p.getAttribute(attribute).getValue().toString());
				}
				writer.append(newLine);
			}
			writer.close();
		}
	}

	public static void main(String[] args) throws IOException {
		//main1(args);
		printDataAllProtocols(256, 1);
	}
	
	public static void main1(String[] args) throws IOException {
		DBProtocol[][] protocols = DBProtocol.loadProtocolsFairly();
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
		};
		printDataOfBestInstances(protocols, attributes[0], 256, 32);
		printDataOfBestInstances(protocols, attributes[1], 256, 32);
		printDataOfBestInstances(protocols, attributes[2], 256, 32);
	}
	
	/*Trujillo- Jun 3, 2014
	 * Create a data file for each protocol*/
	public static void printDataOfBestInstances(DBProtocol[][] clusters, Attribute attribute, int maxE, int increment) throws IOException{
		String newLine = System.getProperty("line.separator");
		for (DBProtocol[] protocols : clusters) {
			if (protocols[0].getTotalBitsExchangedDuringFastPhase() % increment != 0) continue;
			if (protocols[0].getTotalBitsExchangedDuringFastPhase() > maxE) continue;
			Hashtable<String, Attribute> bestValues = new Hashtable<>();
			for (DBProtocol p : protocols) {
				Attribute toCompareWith = p.getAttribute(attribute);
				if (bestValues.containsKey(p.getAcronym())){
					Attribute best = bestValues.get(p.getAcronym());
					if (toCompareWith.dominate(best)) 
						bestValues.put(p.getAcronym(), toCompareWith);
				}
				else{
					bestValues.put(p.getAcronym(), toCompareWith);					
				}
			}
			for (String pName : bestValues.keySet()) {
				FileWriter writer = new FileWriter(new File(pName+"-"+attribute.getName()+"-increment-"+increment+".DAT"), true);
				writer.append(protocols[0].getTotalBitsExchangedDuringFastPhase()+" \t ");
				writer.append(bestValues.get(pName).getValue().toString());
				writer.append(newLine);
				writer.close();
			}
		}
	}

	public static void printDataAllProtocols(int maxN, int increment) throws IOException{
		DBProtocol[] protocols = DBProtocol.loadProtocols();
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new Memory(new MemoryRelation(), new KbitsScale()),
		};
		String suffix = "";
		String newLine = System.getProperty("line.separator");
		for (Attribute attribute : attributes) {
			suffix += "-"+attribute.getName();
		}
		FileWriter writer = new FileWriter(new File("Data"+suffix+"-increment-"+increment+".DAT"), false);
		writer.write("# n");
		writer.append(","+"Protocol");
		for (Attribute attribute : attributes) {
			writer.append(","+attribute.getName());
		}
		writer.append(newLine);
		for (DBProtocol p : protocols) {
			
			writer.append(""+p.getNumberOfRounds());
			writer.append(","+p.getAcronym());
			for (Attribute attribute : attributes) {
				writer.append(","+p.getAttribute(attribute).getValue().toString());
			}
			writer.append(newLine);
		}
		writer.close();
	}
}
