package attributes.relations;

public class DoubleRelation implements ApproximateEquality<Double>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 852613572204650525L;

	@Override
	public boolean equal(Double x, Double y) {
		return x.equals(y);
	}

}
