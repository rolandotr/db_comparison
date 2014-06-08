package attributes.relations;

public class IntegerRelation implements ApproximateEquality<Integer>{

	private static final long serialVersionUID = -9115481140156249134L;

	@Override
	public boolean equal(Integer x, Integer y) {
		return x.equals(y);
	}

}
