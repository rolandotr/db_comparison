package graph_mafia_calculator;

import java.math.BigDecimal;

public abstract class GraphDBMafiaCalculator {

	static final BigDecimal HALF = BigDecimal.valueOf(.5);

	protected abstract int getInitialVertex();

	/**
	 * To define the edges of a graph instance (which may be any). All you need
	 * to ensure is that <i>getNeighbor(vertex, 0)</i> and
	 * <i>getNeighbor(vertex, 1)</i> are the two connected vertices to
	 * <i>vertex</i>, it does not matter the order.
	 * 
	 * @param vertex
	 *            the edge source vertex
	 * @param edgeLabel
	 *            the label of the edge
	 * @return
	 */

	protected abstract int getNeighbor(int vertex, int edgeLabel);

	public BigDecimal mafia(int n) {

		int chLimit = 1 << n;

		int verticesVisitedByC[] = new int[n];
		int verticesVisitedByX[] = new int[n];
		int verticesSelectedForResponses[] = new int[n];

		BigDecimal sum = BigDecimal.ZERO;
		

		for (int x = 0; x < chLimit; x++) {

			fillListOfVisitedVertices(x, n, verticesVisitedByX);
			
			for (int c = 0; c < chLimit; c++) {

				fillListOfVisitedVertices(c, n, verticesVisitedByC);
				
				for (int lc = 1; lc <= n; lc++) {

					BigDecimal max = BigDecimal.valueOf(-1), tmp = null;
					int bestLx = 0;
					
					for (int lx = 1; lx <= n; lx++) {

						tmp = probabilityOfMeeting(x, c, lx, lc);
						if (tmp.compareTo(max) > 0) {
							max = tmp;
							bestLx = lx;
						}
						if (tmp.compareTo(BigDecimal.ONE) == 0)
							break;

					}

					verticesSelectedForResponses[lc - 1] = verticesVisitedByX[bestLx - 1];
				}
				
				sum = sum.add(BigDecimal.valueOf(2).pow(countEquals(verticesSelectedForResponses, verticesVisitedByC, n)));
				
			}

		}

		return sum.multiply(BigDecimal.valueOf(.125).pow(n));

	}
	
	private int getBit(int value, int idx) {
		return (value & (1 << idx)) > 0 ? 1 : 0;
	}

	private int countEquals(int a[], int b[], int n) {
		int count = 0;
		for (int i = 0; i < n; i++)
			count += (a[i] == b[i] ? 1 : 0);
		return count;
	}

	private void fillListOfVisitedVertices(int ch, int n, int vv[]) {
		int vertex = getInitialVertex();
		for (int i = 0; i < n; i++)
			vv[i] = (vertex = getNeighbor(vertex, getBit(ch, i)));
	}

	private int decrease(int l) {
		return l > 0 ? l - 1 : 0;
	}

	protected int getNeighborIfApplicable(int vertex, int edgeLabel, int l) {
		return l > 0 ? getNeighbor(vertex, edgeLabel) : vertex;
	}

	protected BigDecimal probabilityOfMeeting(int x, int c, int lx, int lc) {
		return probabilityOfMeeting(getInitialVertex(), getInitialVertex(), x, c, lx, lc);
	}

	private BigDecimal probabilityOfMeeting(int vertex1, int vertex2, int ch1, int ch2, int l1, int l2) {

		if (l1 == 0 && l2 == 0)
			return vertex1 == vertex2 ? BigDecimal.ONE : BigDecimal.ZERO;

		int bit1 = 0, bit2 = (ch1 & 1) == (ch2 & 1) ? bit1 : 1 - bit1;

		int neighbor1 = getNeighborIfApplicable(vertex1, bit1, l1);
		int neighbor2 = getNeighborIfApplicable(vertex2, bit2, l2);

		int neighbor3 = getNeighborIfApplicable(vertex1, 1 - bit1, l1);
		int neighbor4 = getNeighborIfApplicable(vertex2, 1 - bit2, l2);

		BigDecimal a = probabilityOfMeeting(neighbor1, neighbor2, ch1 >> 1, ch2 >> 1, decrease(l1), decrease(l2));
		BigDecimal b = probabilityOfMeeting(neighbor3, neighbor4, ch1 >> 1, ch2 >> 1, decrease(l1), decrease(l2));

		BigDecimal res = HALF.multiply(a.add(b));

		if (vertex1 != vertex2) {

			bit2 = 1 - bit2;

			neighbor1 = getNeighborIfApplicable(vertex1, bit1, l1);
			neighbor2 = getNeighborIfApplicable(vertex2, bit2, l2);

			neighbor3 = getNeighborIfApplicable(vertex1, 1 - bit1, l1);
			neighbor4 = getNeighborIfApplicable(vertex2, 1 - bit2, l2);

			a = probabilityOfMeeting(neighbor1, neighbor2, ch1 >> 1, ch2 >> 1, decrease(l1), decrease(l2));
			b = probabilityOfMeeting(neighbor3, neighbor4, ch1 >> 1, ch2 >> 1, decrease(l1), decrease(l2));

			BigDecimal res2 = HALF.multiply(a.add(b));

			res = HALF.multiply(res.add(res2));
		}

		return res;
	}
}
