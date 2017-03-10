package graph_mafia_calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

public abstract class FastGraphDBMafiaCalculator extends GraphDBMafiaCalculator {

	private HashMap<Integer, HashMap<Integer, Long>[]> reachableMap = new HashMap<Integer, HashMap<Integer, Long>[]>();

	private void addInMap(HashMap<Integer, Long> map, int v, long c) {
		map.put(v, c + (map.containsKey(v) ? map.get(v) : 0));
	}

	private HashMap<Integer, Long> reachableOfSize(int size, int vertex) {

		if (!reachableMap.containsKey(vertex) || reachableMap.get(vertex).length < size + 1) {

			int len = reachableMap.containsKey(vertex) ? reachableMap.get(vertex).length : 0;

			HashMap<Integer, Long> reachable[] = new HashMap[size + 1];

			for (int i = 0; i < len; i++)
				reachable[i] = reachableMap.get(vertex)[i];

			HashMap<Integer, Long> tmpMap;

			if (len == 0)
				addInMap(tmpMap = new HashMap<Integer, Long>(), vertex, 1);
			else
				tmpMap = reachableMap.get(vertex)[--len];

			for (int i = len; i < size; i++) {

				reachable[i] = tmpMap;

				tmpMap = new HashMap<Integer, Long>();

				Set<Entry<Integer, Long>> entrySet = reachable[i].entrySet();
				for (Entry<Integer, Long> e : entrySet) {

					addInMap(tmpMap, getNeighbor(e.getKey(), 0), e.getValue());
					addInMap(tmpMap, getNeighbor(e.getKey(), 1), e.getValue());

				}
			}

			reachable[size] = tmpMap;
			reachableMap.put(vertex, reachable);
		}

		return reachableMap.get(vertex)[size];
	}

	@Override
	protected BigDecimal probabilityOfMeeting(int x, int c, int lx, int lc) {

		int t = 0;
		while ((x & 1) == (c & 1) && t < lx && t < lc) {
			x >>= 1;
			c >>= 1;
			t++;
		}

		HashMap<Integer, Long> joint = reachableOfSize(t, getInitialVertex());
		Set<Entry<Integer, Long>> jointEntrySet = joint.entrySet();
		BigInteger total = BigInteger.ZERO;

		for (Entry<Integer, Long> je : jointEntrySet) {

			BigInteger sum = BigInteger.ZERO;

			int v = je.getKey();

			for (int b = (lx == t && lc == t ? 0 : 1); b >= 0; b--) {

				BigInteger num = numberOfPairOfSeqWithCommonVertices(getNeighborIfApplicable(v, b, lc - t),
						getNeighborIfApplicable(v, 1 - b, lx - t), lc == t ? 0 : lc - t - 1, lx == t ? 0 : lx - t - 1);

				sum = sum.add(num);

			}

			sum = sum.multiply(BigInteger.valueOf(je.getValue()));

			total = total.add(sum);

		}

		return new BigDecimal(total).multiply(HALF.pow((lx == t || lc == t) ? (lx == t ? lc : lx) : lx + lc - t - 1));
	}

	private BigInteger numberOfPairOfSeqWithCommonVertices(int vertex1, int vertex2, int len1, int len2) {

		BigInteger num = BigInteger.ZERO;

		HashMap<Integer, Long> map1 = reachableOfSize(len1, vertex1);
		HashMap<Integer, Long> map2 = reachableOfSize(len2, vertex2);

		Set<Entry<Integer, Long>> entrySet = map1.entrySet();

		for (Entry<Integer, Long> e : entrySet)
			num = num.add(BigInteger.valueOf(e.getValue())
					.multiply(BigInteger.valueOf(map2.containsKey(e.getKey()) ? map2.get(e.getKey()) : 0)));

		return num;
	}

}
