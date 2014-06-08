package tests;

import protocols.specifications.PoulidorDistanceFraudSimulator;
import protocols.specifications.PoulidorProtocol;


public class PoulidorDistanceFraudSimulatorTest {

	public static void main(String[] args) {
		test2();
		test3();
		test1();
	}
	
	
	/*Trujillo- Mar 11, 2014
	 * We believe that mafia fraud upper bound distance fraud
	 * for this protocol. Therefore, this test aims to verify that*/
	private static void test2(){
		PoulidorProtocol pro = new PoulidorProtocol(0);
		int n = 128;
		for (int i = 1; i <= n; i++) {
			pro.setNumberOfRounds(i);
			double mafia = pro.getMafiaFraudProbability().doubleValue();
			double distance = pro.getDistanceFraudProbability().doubleValue();
			System.out.println("n = "+i+" : mafia = "+mafia+" : distance = "+distance);
		}
	}

	/*Trujillo- Mar 11, 2014
	 * This test consists simply on calling the method for some values
	 * of n.*/
	private static void test1(){
		PoulidorDistanceFraudSimulator sim = new PoulidorDistanceFraudSimulator();
		int n = 128;
		for (int i = 2; i <= n; i++){
			double value = sim.computeDistanceFraud(i);
			System.out.println("n = "+i+" df = "+value);
		}
	}
	
	/*Trujillo- Mar 12, 2014
	 * In this test, we will change the size of the "table" during the simulation in order to check
	 * how reducing the size affects the result*/
	private static void test3(){
		PoulidorDistanceFraudSimulator sim = new PoulidorDistanceFraudSimulator();
		int n = 6;
		int maxSize = 16;
		for (int size = 1; size < maxSize; size *= 2) {
			for (int i = 1; i <= n; i++){
				sim.setSize(size);
				double value = sim.computeDistanceFraud(i);
				System.out.println("n = "+i+", size = "+size+" df = "+value);
			}
		}
	}
}