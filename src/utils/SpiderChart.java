package utils;

import java.io.FileWriter;
import java.io.IOException;

import protocols.specifications.BestProtocol;
import protocols.specifications.BrandsAndChaumProtocol;
import protocols.specifications.DBProtocol;
import protocols.specifications.MunillaAndPeinadoProtocol;
import protocols.specifications.PoulidorProtocol;
import protocols.specifications.SKIProtocol;
import protocols.specifications.SwissKnifeProtocol;
import protocols.specifications.TMAProtocol;
import protocols.specifications.TreeBasedProtocol;


import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.SizeOfMessages;
import attributes.TerroristFraudProbability;
import attributes.relations.FinalSlowPhaseRelation;
import attributes.relations.IntegerRelation;
import attributes.relations.MemoryRelation;
import attributes.relations.ProbabilityRelation;
import attributes.relations.SizeOfMessagesRelation;
import attributes.scales.KbitsScale;
import attributes.scales.LogScale;
import attributes.scales.NoScale;

public class SpiderChart {

public static String newLine = System.getProperty("line.separator");

	public static void main(String[] args) throws IOException {
		main1(args);
	}
	
	public static void main1(String[] args) throws IOException {
		DBProtocol best = new BestProtocol();
		best.setNumberOfRounds(128);
		DBProtocol swiss = new SwissKnifeProtocol();
		swiss.setNumberOfRounds(128);
		DBProtocol ski = new SKIProtocol(2, 2, DBProtocol.SIZE_OF_NONCES);
		ski.setNumberOfRounds(64);
		DBProtocol[] firstSet = new DBProtocol[]{
				best,swiss,ski,
		};
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
		};

		printSpiderChart(firstSet, attributes, "swiss_ski.tex");
	}

	public static void main3(String[] args) throws IOException {
		DBProtocol best = new BestProtocol();
		best.setNumberOfRounds(16);
		DBProtocol tree = new TreeBasedProtocol(8);
		tree.setNumberOfRounds(16);
		DBProtocol hk = new MunillaAndPeinadoProtocol(0.75);
		hk.setNumberOfRounds(16);
		DBProtocol bc = new BrandsAndChaumProtocol();
		bc.setNumberOfRounds(16);
		DBProtocol[] firstSet = new DBProtocol[]{
				best,tree,hk, bc,
		};
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new Memory(new MemoryRelation(), new KbitsScale()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
		};

		printSpiderChart(firstSet, attributes, "tree_hk_bc.tex");
	}

	public static void main4(String[] args) throws IOException {
		DBProtocol best = new BestProtocol();
		best.setNumberOfRounds(32);
		DBProtocol poulidor = new PoulidorProtocol();
		poulidor.setNumberOfRounds(32);
		DBProtocol[] firstSet = new DBProtocol[]{
				best,poulidor,
		};
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new Memory(new MemoryRelation(), new KbitsScale()),
		};

		printSpiderChart(firstSet, attributes, "uniform_against_others.tex");
	}

	public static void main2(String[] args) throws IOException {
		DBProtocol best = new BestProtocol();
		best.setNumberOfRounds(128);
		DBProtocol tree = new TreeBasedProtocol(7, DBProtocol.SIZE_OF_NONCES);
		tree.setNumberOfRounds(128);
		DBProtocol poulidor = new PoulidorProtocol();
		poulidor.setNumberOfRounds(128);
		DBProtocol tma = new TMAProtocol();
		tma.setNumberOfRounds(128);
		DBProtocol[] firstSet = new DBProtocol[]{
				best,tree,poulidor,tma
		};
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new Memory(new MemoryRelation(), new KbitsScale()),
		};

		printSpiderChart(firstSet, attributes, "tree_poulidor_tma.tex");
	}
	
	public static void printSpiderChart(DBProtocol[] protocols, Attribute[] attributes, 
			String fileName) throws IOException{
		FileWriter writer = new FileWriter(fileName);
		//first, we print the header
		writer.append("\\begin{tikzpicture}[label distance=.15cm,rotate=30,scale=0.5]"+newLine);
		writer.append("\\tkzKiviatDiagram{");
		addAttributes(attributes, writer);
		writer.append("}"+newLine);
		double[][] normalizedAttributes = normalizeAttributes(protocols, attributes);		
		for (int i = 0; i < protocols.length; i++) {
			addProtocol(i, attributes, normalizedAttributes, writer);
		}
	    writer.append("\\end{tikzpicture}");
		writer.close();	
	}

	private static double[][] normalizeAttributes(DBProtocol[] protocols, 
			Attribute[] allAttributes) {
		double[][] result = new double[allAttributes.length][protocols.length];
		for (int i = 0; i < allAttributes.length; i++) {
			result[i] = normalizeAttributes(protocols, 
					allAttributes[i]);
		}
		return result;
	}

	private static double[] normalizeAttributes(DBProtocol[] protocols,
			Attribute attribute) {
		if (attribute.getValue() instanceof Double){
			return normalizeDoubleAttributes(protocols,attribute);
		}
		else if (attribute.getValue() instanceof Integer){
			return normalizeIntegerAttributes(protocols,attribute);
		}
		else if (attribute.getValue() instanceof Long){
			return normalizeLongAttributes(protocols,attribute);
		}
		else if (attribute.getValue() instanceof Boolean){
			return normalizeBooleanAttributes(protocols,attribute);
		}
		else{
			throw new RuntimeException();
		}
	}

	private static double[] normalizeDoubleAttributes(DBProtocol[] protocols,
			Attribute attribute) {
		double max = 0;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < protocols.length; i++) {
			double tmp = (Double)protocols[i].getAttribute(attribute).getScaledValue();
			if (tmp > max) max = tmp;
			if (tmp < min) min = tmp;
		}
		double[] result = new double[protocols.length];
		for (int i = 0; i < result.length; i++) {
			double tmp = (Double)protocols[i].getAttribute(attribute).getScaledValue();
			if (max == min) result[i] = 1;
			else result[i] = 1-(tmp-min)/(max-min);
		}
		return result;
	}

	private static double[] normalizeIntegerAttributes(DBProtocol[] protocols,
			Attribute attribute) {
		double max = 0;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < protocols.length; i++) {
			double tmp = (Integer)protocols[i].getAttribute(attribute).getScaledValue();
			if (tmp > max) max = tmp;
			if (tmp < min) min = tmp;
		}
		double[] result = new double[protocols.length];
		for (int i = 0; i < result.length; i++) {
			double tmp = (Integer)protocols[i].getAttribute(attribute).getScaledValue();
			if (max == min) result[i] = 1;
			else result[i] = 1-(tmp-min)/(max-min);
		}
		return result;
	}
	private static double[] normalizeLongAttributes(DBProtocol[] protocols,
			Attribute attribute) {
		double max = 0;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < protocols.length; i++) {
			double tmp = (Long)protocols[i].getAttribute(attribute).getScaledValue();
			if (tmp > max) max = tmp;
			if (tmp < min) min = tmp;
		}
		double[] result = new double[protocols.length];
		for (int i = 0; i < result.length; i++) {
			double tmp = (Long)protocols[i].getAttribute(attribute).getScaledValue();
			if (max == min) result[i] = 1;
			else result[i] = 1-(tmp-min)/(max-min);
		}
		return result;
	}

	private static double[] normalizeBooleanAttributes(DBProtocol[] protocols,
			Attribute attribute) {
		boolean best = false;
		for (int i = 0; i < protocols.length; i++) {
			boolean tmp = (Boolean)protocols[i].getAttribute(attribute).getScaledValue();
			if (attribute.dominate(best)) best = tmp;
		}
		double[] result = new double[protocols.length];
		for (int i = 0; i < result.length; i++) {
			boolean tmp = (Boolean)protocols[i].getAttribute(attribute).getScaledValue();
			if (tmp == best) result[i] = 1;
			else result[i] = 0;
		}
		return result;
	}


	
	private static Attribute[][] getAttributes(DBProtocol[] protocols,
			Attribute[] attributes) {
		Attribute[][] allAttributes = new Attribute[attributes.length][protocols.length];
		for (int i = 0; i < protocols.length; i++) {
			for (int j = 0; j < attributes.length; j++) {
				allAttributes[j][i] = protocols[i].getAttribute(attributes[j]);
			}
		}
		return allAttributes;
	}

	private static void addAttributes(Attribute[] attributes, FileWriter writer) throws IOException {
		for (int i = 0; i < attributes.length-1; i++) {
			writer.append(attributes[i].getName()+",");
		}
		writer.append(attributes[attributes.length-1].getName());
	}
	
	private static void addProtocol(int protocolPos, Attribute[] attributes, double[][] normalizedAttributes, FileWriter writer) throws IOException {
		writer.append("\\tkzKiviatLine[thick, color = red, mark = ball, " +
				"ball color = red, mark size  = 4pt, opacity = .2,"+newLine);
	    writer.append("fill=red!20](");
        writer.append(""+((int)(normalizedAttributes[0][protocolPos]*10)));
	    for (int j = 1; j < attributes.length; j++) {
	        writer.append(","+((int)(normalizedAttributes[j][protocolPos]*10)));
		}
	    writer.append(")"+newLine);
	}


}
