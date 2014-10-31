package attributes.relations;

public class LongRelation implements ApproximateEquality<Long>{



	/**
	 * 
	 */
	private static final long serialVersionUID = 7809571158942723770L;

	@Override
	public boolean equal(Long x, Long y) {
		return x.equals(y);
	}

}
