package utils;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Iterator;

import methodology.ParetoFrontier;
import protocols.specifications.DBProtocol;

public abstract class Gnuplot {

	
	public static void main(String[] args) {
		
	}
	
	public static void createDatafile(DBProtocol[] protocols,
			DBProtocol[] frontiers, String preffix) throws IOException {
		String newLine = System.getProperty("line.separator");
		File points = new File("points_"+preffix+".DAT");
		FileWriter writer = new FileWriter(points);
		for (DBProtocol p : protocols) {
			writer.write(p.getTotalBitsExchangedDuringFastPhase()+"");
			writer.write("\t");
			writer.write(p.getMafiaFraudProbability().toString());
			writer.write(newLine);
		}
		writer.close();
		Hashtable<Class, FileWriter> classes = new Hashtable<>();
		for (DBProtocol p : frontiers) {
			Class classP = p.getClass();
			if (!classes.containsKey(classP)) {
				classes.put(classP, new FileWriter(new File(classP.getName()+"_"+preffix+".DAT")));
			}			
			//writer.write(classes.get(p.getClass()));
			//writer.write("\t");
			writer = classes.get(classP);
			writer.write(p.getTotalBitsExchangedDuringFastPhase()+"");
			writer.write("\t");
			writer.write(p.getMafiaFraudProbability().toString());
			writer.write(newLine);
		}
		for (FileWriter w  : classes.values()){
			w.close();
		}
	}
	
	
}
