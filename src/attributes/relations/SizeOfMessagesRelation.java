package attributes.relations;

public class SizeOfMessagesRelation implements ApproximateEquality<Integer>{

	private static final long serialVersionUID = -7060648351488285845L;

	@Override
	public boolean equal(Integer x, Integer y) {
		return (x.intValue() == 1 && y.intValue() == 1) || (x > 1 && y > 1); 
	}

}
