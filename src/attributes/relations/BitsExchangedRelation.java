package attributes.relations;

public class BitsExchangedRelation implements ApproximateEquality<Integer>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 820031181111906255L;

	@Override
	public boolean equal(Integer x, Integer y) {
		return Math.abs(x-y)<=2;
	}

}
