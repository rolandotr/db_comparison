package attributes.relations;

public class FinalSlowPhaseRelation implements ApproximateEquality<Boolean>{

	private static final long serialVersionUID = 2362784700878649000L;

	@Override
	public boolean equal(Boolean x, Boolean y) {
		return x.equals(y);
	}

}
