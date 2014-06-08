package attributes.relations;

public class MemoryRelation implements ApproximateEquality<Long>{

	private static final long serialVersionUID = 681969845475187247L;

	@Override
	public boolean equal(Long x, Long y) {
		return Math.abs(x-y)<1024;
	}

}
