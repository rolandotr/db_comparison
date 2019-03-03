package protocols.specifications;

//import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

import methodology.ParetoFrontier;

import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.SizeOfMessages;
import attributes.TerroristFraudProbability;
import attributes.TotalBitsExchanged;
import attributes.relations.FinalSlowPhaseRelation;
import attributes.relations.IntegerRelation;
import attributes.relations.MemoryRelation;
import attributes.relations.ProbabilityRelation;
import attributes.relations.SizeOfMessagesRelation;
import attributes.scales.KbitsScale;
import attributes.scales.LogScale;
import attributes.scales.NoScale;

public class ModularProtocol extends DBProtocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7990599544513576342L;

	private final static int DEFAULT_H = 30;
	private static final int MAX_H = 256;
	private static final BigDecimal TWO = BigDecimal.valueOf(2);
	public static final BigDecimal SQRT_DIG = new BigDecimal(150);
	public static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

	private int h;
	private int sizeOfNonces;

	private static class FastModularCalculator {

		private BigDecimal table[][], R[][], tableDistance[][];
		private BigInteger W[][], p2[];

		public FastModularCalculator() {
			super();

			W = new BigInteger[MAX_H + 1][MAX_N + 1];
			p2 = new BigInteger[MAX_N + 1];

			table = new BigDecimal[MAX_H + 1][MAX_N + 1];
			tableDistance = new BigDecimal[MAX_H + 1][MAX_N + 1];
			R = new BigDecimal[MAX_H + 1][MAX_N + 1];

			p2[0] = BigInteger.ONE;
			BigInteger two = BigInteger.valueOf(2);
			for (int n = 1; n <= MAX_N; n++)
				p2[n] = p2[n - 1].multiply(two);
		}

		/**
		 * A fast <b>O(MAX_N^2)</b> dynamic solution. For a given <b>h</b>, it performs
		 * the computation of mafia(<b>h</b>,n) for every n <= MAX_N Further queries
		 * with the same <b>h</b> are simply done by retrieving the value from the table
		 * 
		 * @param h
		 *            is the size
		 * @param n
		 *            is the number of rounds
		 * @return the probability of success of a mafia-fraud attack against the
		 *         h-modular protocol
		 */

		public BigDecimal mafia(int h, int n) {
			if (h > MAX_H || n > MAX_N)
				System.err.println("Something is wrong. Check MAX_N and MAX_H in " + this.getClass().getName());

			if (table[h][n] == null) { // it means that this is the first time the
										// function is called with this h

				for (int i = 1; i <= MAX_N; i++) {

					BigInteger v = BigInteger.valueOf(h).min(p2[i]);
					BigInteger a = p2[i].divide(v);
					BigInteger r = p2[i].remainder(v);

					W[h][i] = (v.multiply(a).multiply(a)).add(r.add(BigInteger.valueOf(2).multiply(a).multiply(r)));
				}

				R[h][0] = BigDecimal.ONE;
				for (int i = 1; i <= MAX_N; i++) {
					R[h][i] = BigDecimal.ZERO;
					for (int j = 0; j < i; j++)
						R[h][i] = R[h][i].add(R[h][j].multiply(new BigDecimal(W[h][i - j])));
				}

				table[h][0] = BigDecimal.ONE;
				for (int i = 1; i <= MAX_N; i++)
					table[h][i] = ONE_OVER_TWO.multiply(table[h][i - 1])
							.add(R[h][i].multiply(BigDecimal.valueOf(.125).pow(i)));

			}

			return table[h][n];

		}

		public BigDecimal distanceUpperBound(int h, int n) {

			if (tableDistance[h][n] == null) {

				BigDecimal a = BigDecimal.ONE.subtract(TWO.pow(n + 2)).add(mafia(h, n).multiply(TWO.pow(2 * n + 2)));

				if (a.compareTo(BigDecimal.ZERO) < 0) {
					tableDistance[h][n] = BigDecimal.ONE;
				} else {
					tableDistance[h][n] = (ONE_OVER_TWO.pow(n + 1)).multiply(BigDecimal.ONE.add(bigSqrt(a)));
				}
			}
			return tableDistance[h][n];

		}

		/**
		 * Square root of a BigDecimal by using Newton Raphson method.
		 * 
		 * @author Luciano Culacciatti
		 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
		 */

		private BigDecimal sqrtNewtonRaphson(BigDecimal c, BigDecimal xn, BigDecimal precision) {
			BigDecimal fx = xn.pow(2).add(c.negate());
			BigDecimal fpx = xn.multiply(new BigDecimal(2));
			BigDecimal xn1 = fx.divide(fpx, 2 * SQRT_DIG.intValue(), RoundingMode.HALF_DOWN);
			xn1 = xn.add(xn1.negate());
			BigDecimal currentSquare = xn1.pow(2);
			BigDecimal currentPrecision = currentSquare.subtract(c);
			currentPrecision = currentPrecision.abs();
			if (currentPrecision.compareTo(precision) <= -1) {
				return xn1;
			}
			return sqrtNewtonRaphson(c, xn1, precision);
		}

		public BigDecimal bigSqrt(BigDecimal c) {
			return sqrtNewtonRaphson(c, new BigDecimal(1), new BigDecimal(1).divide(SQRT_PRE));
		}

		public void test() {
			// testing robustness....
			for (int h = 2; h <= MAX_H; h++)
				for (int n = 1; n <= MAX_N; n++)
					try {
						System.out.println("Mafia(" + h + "," + n + ")" + fast.mafia(h, n));
						System.out.println("Dist(" + h + "," + n + ")" + fast.distanceUpperBound(h, n));

					} catch (Exception e) {
						System.out.println(n + "," + h);
						e.printStackTrace();
					}
		}

	}

	private static final FastModularCalculator fast = new FastModularCalculator();

	public ModularProtocol(int sizeOfNonces, int h) {
		this.sizeOfNonces = sizeOfNonces;
		this.h = h;
	}

	public ModularProtocol(int h) {
		this(SIZE_OF_NONCES, h);
	}

	public ModularProtocol() {
		this(SIZE_OF_NONCES, DEFAULT_H);
	}

	public void setSize(int h) {
		this.h = h;
	}

	@Override
	public DBProtocol getInstance() {
		// TODO Auto-generated method stub
		return new ModularProtocol();
	}

	@Override
	public String getAcronym() {
		// TODO Auto-generated method stub
		return "Modular";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		// TODO Auto-generated method stub
		return fast.mafia(h, n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		// TODO Auto-generated method stub
		return fast.distanceUpperBound(h, n);
	}

	@Override
	public BigDecimal getTerroristFraudProbability() {
		// TODO Auto-generated method stub
		return BigDecimal.ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSizeOfTheChannel() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean lackSecurityProof() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getYearOfPublication() {
		// TODO Auto-generated method stub
		return 2016;
	}

	@Override
	public int getTotalMsgSizeReceived() {
		// TODO Auto-generated method stub
		return sizeOfNonces + n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		// TODO Auto-generated method stub
		return h * (2 * n - 1);
	}

	@Override
	public int getNumberOfNoncesGenerated() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getBitsGenerated() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DBProtocol[] getInstances() {
		
		int hs[] = { 16, 32, 64, 128, 256 }, len = hs.length;

		DBProtocol[] result = new DBProtocol[MAX_N * len];
		for (int i = 0; i < len; i++)
			for (int j = 0; j < MAX_N; j++) {
				result[i * MAX_N + j] = new ModularProtocol(hs[i]);
				result[i * MAX_N + j].setNumberOfRounds(j + 1);
			}

		return result;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return getAcronym() + "-{" + n + ", " + h + "}";
	}

	@Override
	public int getCryptoCalls() {
		// TODO Auto-generated method stub
		return 1;
	}

	public static void main(String[] args) {

		ArrayList<DBProtocol> list = new ArrayList<DBProtocol>();

		ModularProtocol modular = new ModularProtocol(16);
		modular.setNumberOfRounds(41);

		// ModularProtocol modular2 = new ModularProtocol(64);
		// modular2.setNumberOfRounds(38);

		PoulidorProtocol poulidor = new PoulidorProtocol();
		poulidor.setNumberOfRounds(42);

		// KimAndAvoineProtocol ka = new KimAndAvoineProtocol(.85);
		// ka.setNumberOfRounds(37);
		//
		// BrandsAndChaumProtocol bc = new BrandsAndChaumProtocol();
		// bc.setNumberOfRounds(32);
		//
		// TreeBasedProtocol tree = new TreeBasedProtocol(6);
		// tree.setNumberOfRounds(48);
		//
		// TMAProtocol tma = new TMAProtocol();
		// tma.setNumberOfRounds(53);
		//
		// SwissKnifeProtocol swiss = new SwissKnifeProtocol();
		// swiss.setNumberOfRounds(32);
		//
		// SKIProtocol ski = new SKIProtocol(2);
		// ski.setNumberOfRounds(78);

		list.add(modular);
		// list.add(modular2);
		list.add(poulidor);
		// list.add(ka);
		// list.add(bc);
		// list.add(tree);
		// list.add(tma);
		// list.add(swiss);
		// list.add(ski);

		DBProtocol arr[] = new DBProtocol[list.size()];
		for (int i = 0; i < arr.length; i++)
			arr[i] = list.get(i);

		Attribute[] attributes = new Attribute[] {
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TotalBitsExchanged(new IntegerRelation(), new NoScale<Integer>()),
				// new TotalBitsExchanged(new BitsExchangedRelation(), new NoScale<Integer>()),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new Memory(new MemoryRelation(), new KbitsScale()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()), };

		ParetoFrontier frontiers = null;
		try {
			// frontiers = ParetoFrontier.computeParetoFrontier(new DBProtocol[]{ modular,
			// poulidor, modular2}, attributes);
			frontiers = ParetoFrontier.computeParetoFrontier(arr, attributes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBProtocol[] frontier = frontiers.getFrontier();
		System.out.println();
		System.out.println("Pareto Frontier: " + frontier.length + " instances");
		for (DBProtocol protocol : frontier)
			System.out.println(protocol.getIdentifier());

		// System.out.println(poulidor.getMafiaFraudProbability());
		// System.out.println(BigDecimal.valueOf(.5).pow(32));

	}
}
