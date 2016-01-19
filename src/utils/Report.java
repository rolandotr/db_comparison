package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.NumberOfRounds;
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

import protocols.specifications.*;

public class Report {

	public static void main(String[] args) throws IOException {
		File f = new File("evolution-1.txt");
		int numberOfSolutions = getNumberOfSolutions(f);
		
		Attribute[] attributes = new Attribute[]{
				new NumberOfRounds(new IntegerRelation(), new NoScale<Integer>()),
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				//new MafiaFraudProbability(new DoubleRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				//new DistanceFraudProbability(new DoubleRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				//new TerroristFraudProbability(new DoubleRelation(), new LogScale(2)),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				//new SizeOfMessages(new IntegerRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new Memory(new MemoryRelation(), new KbitsScale()),
				//new Memory(new LongRelation(), new KbitsScale()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
		};
		String row = "";
		List<DBProtocol> bc = getBransAndChaum(f);
		row += appendProtocol(bc, attributes);
		List<DBProtocol> bb = getBussardAndBaggat(f);
		row += appendProtocol(bb, attributes);
		List<DBProtocol> hk = getHanckeAndKuhnProtocol(f);
		row += appendProtocol(hk, attributes);
		List<DBProtocol> kim = getKimAndAvoineProtocol(f);
		row += appendProtocol(kim, attributes);
		List<DBProtocol> mad = getMADProtocol(f);
		row += appendProtocol(mad, attributes);
		List<DBProtocol> mp = getMunillaAndPeinadoProtocol(f);
		row += appendProtocol(mp, attributes);
		List<DBProtocol> poulidor = getPoulidorProtocol(f);
		row += appendProtocol(poulidor, attributes);
		List<DBProtocol> rc = getRasmussenAndCapckunProtocol(f);
		row += appendProtocol(rc, attributes);
		List<DBProtocol> ski = getSKIProtocol(f);
		row += appendProtocol(ski, attributes);
		List<DBProtocol> swiss = getSwissKnifeProtocol(f);
		row += appendProtocol(swiss, attributes);
		List<DBProtocol> tma = getTMAProtocol(f);
		row += appendProtocol(tma, attributes);
		List<DBProtocol> tree = getTreeBasedProtocol(f);
		row += appendProtocol(tree, attributes);
		List<DBProtocol> ykhl = getYKHLProtocol(f);
		row += appendProtocol(ykhl, attributes);
		System.out.println("Number of solutions = "+numberOfSolutions);
		int total = bc.size()+bb.size()+hk.size()+kim.size()+mad.size()+mp.size()+poulidor.size()+rc.size()+
				ski.size()+swiss.size()+tma.size()+tree.size()+ykhl.size();
		if (numberOfSolutions == total)	System.out.println("OK");
		else System.out.println("Not Ok, "+total);
		System.out.println(row);
	}
	
	private static String appendProtocol(List<DBProtocol> protocols, Attribute[] attributes){
		String result = "";
		if (protocols.isEmpty()) return "";
		double mafiaMax = 0; 
		DBProtocol selected = null;
		for (DBProtocol p : protocols){
			if (p.getMafiaFraudProbability().doubleValue() > mafiaMax){
				mafiaMax = p.getMafiaFraudProbability().doubleValue();
				selected = p;
			}
		}
		String name = selected.getIdentifier().replace("{", "\\{");
		name = name.replace("}", "\\}");
		result += " & "+name;
		for (int i = 0; i < attributes.length; i++) {
			Attribute a = selected.getAttribute(attributes[i]);
			String meaning = a.getScale().scaleMeaning(a.getScaledValue());
			if (a.getScale() instanceof LogScale){
				meaning = meaning.replace(".0", "");
			}
			if (a instanceof SizeOfMessages){
				if (meaning.equals("1")) meaning = "$\\texttt{false}$";
				else meaning = "$\\texttt{true}$";
			}
			if (a instanceof FinalSlowPhase){
				if (meaning.equals("true")) meaning = "$\\texttt{true}$";
				else meaning = "$\\texttt{false}$";
			}
			result += " & $"+meaning+"$ ";			
		}		
		result += " & $"+protocols.size() + "$\\\\ \n";
		return result;
	}
	
	private static List<DBProtocol> getYKHLProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "YKHL-\\{(\\d+), (\\d\\.\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		System.out.println("Looking for: "+regex);
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				YKHLProtocol p = new YKHLProtocol(Double.parseDouble(m.group(2)));
				p.setNumberOfRounds(Integer.parseInt(m.group(1)));
				
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}


	private static List<DBProtocol> getTreeBasedProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "Tree-\\{(\\d+), (\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				TreeBasedProtocol p = new TreeBasedProtocol(Integer.parseInt(m.group(2)), DBProtocol.SIZE_OF_NONCES);
				p.setNumberOfRounds(Integer.parseInt(m.group(1)));
				
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	private static List<DBProtocol> getTMAProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "TMA-\\{(\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				retrievedText = m.group(1);
				TMAProtocol p = new TMAProtocol();
				p.setNumberOfRounds(Integer.parseInt(retrievedText));
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}
	private static List<DBProtocol> getSwissKnifeProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "SwissKnife-\\{(\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				retrievedText = m.group(1);
				SwissKnifeProtocol p = new SwissKnifeProtocol();
				p.setNumberOfRounds(Integer.parseInt(retrievedText));
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	private static List<DBProtocol> getSKIProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "SKI-\\{(\\d+), (\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				SKIProtocol p = new SKIProtocol(Integer.parseInt(m.group(2)), DBProtocol.SIZE_OF_NONCES);
				p.setNumberOfRounds(Integer.parseInt(m.group(1)));
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	
	private static List<DBProtocol> getRasmussenAndCapckunProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "RC-\\{(\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				retrievedText = m.group(1);
				RasmussenAndCapckunProtocol p = new RasmussenAndCapckunProtocol();
				p.setNumberOfRounds(Integer.parseInt(retrievedText));
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	private static List<DBProtocol> getPoulidorProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "Poulidor-\\{(\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				retrievedText = m.group(1);
				PoulidorProtocol p = new PoulidorProtocol();
				p.setNumberOfRounds(Integer.parseInt(retrievedText));
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	private static List<DBProtocol> getMunillaAndPeinadoProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "MP-\\{(\\d+), (\\d\\.\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				MunillaAndPeinadoProtocol p = new MunillaAndPeinadoProtocol(Double.parseDouble(m.group(2)));
				p.setNumberOfRounds(Integer.parseInt(m.group(1)));
				
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	
	private static List<DBProtocol> getMADProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "MAD-\\{(\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				retrievedText = m.group(1);
				MADProtocol p = new MADProtocol();
				p.setNumberOfRounds(Integer.parseInt(retrievedText));
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	private static List<DBProtocol> getKimAndAvoineProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "KA-\\{(\\d+), (\\d\\.\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				KimAndAvoineProtocol p = new KimAndAvoineProtocol(Double.parseDouble(m.group(2)));
				p.setNumberOfRounds(Integer.parseInt(m.group(1)));
				
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	private static List<DBProtocol> getHanckeAndKuhnProtocol(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "HK-\\{(\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				retrievedText = m.group(1);
				HanckeAndKuhnProtocol p = new HanckeAndKuhnProtocol();
				p.setNumberOfRounds(Integer.parseInt(retrievedText));
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	private static List<DBProtocol> getBussardAndBaggat(File f) throws NumberFormatException, IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "BB-\\{(\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				retrievedText = m.group(1);
				BussardAndBaggaProtocol p = new BussardAndBaggaProtocol();
				p.setNumberOfRounds(Integer.parseInt(retrievedText));
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	private static List<DBProtocol> getBransAndChaum(File f) throws IOException {
		List<DBProtocol> protocols = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "BC-\\{(\\d+)\\}";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			while (m.find()) {
				retrievedText = m.group(1);
				BrandsAndChaumProtocol p = new BrandsAndChaumProtocol();
				p.setNumberOfRounds(Integer.parseInt(retrievedText));
				protocols.add(p);
			}
		}
		reader.close();
		return protocols;
	}

	private static int getNumberOfSolutions(File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String regex = "(\\d+)"+" protocols in total";
		Pattern pattern = Pattern.compile(regex);
		String retrievedText = null;
		for (String line = reader.readLine(); line  != null; line = reader.readLine()){
			Matcher m = pattern.matcher(line);
			if (m.find()) {
				retrievedText = m.group(1);
			}
		}
		reader.close();
		return Integer.parseInt(retrievedText);
	}
	
}
