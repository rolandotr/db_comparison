package tests;

import java.io.IOException;
import java.util.Hashtable;

import protocols.DBProtocol;
import dataset.DatasetCreation;

public class DatasetCreationTesting {

	public static void main(String[] args) throws IOException {
		createDatabase();
		testIdentifiers();
	}

	private static void createDatabase() throws IOException {
		DatasetCreation.createDataset(20);
	}

	private static void testIdentifiers() {
		DBProtocol[] protocols = DBProtocol.loadProtocols(16);
		Hashtable<String, Boolean> identifiers = new Hashtable<>();
		for (int i = 0; i < protocols.length; i++) {
			System.out.println("Protocol: "+protocols[i].getIdentifier());
			if (identifiers.containsKey(protocols[i].getIdentifier())){
				throw new RuntimeException("the protocol "+protocols[i]+" was already in the list");
			}
		}
		System.out.println("No repetition! Good Work!");
	}
	
}
