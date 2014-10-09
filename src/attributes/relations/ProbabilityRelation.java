package attributes.relations;


public class ProbabilityRelation implements ApproximateEquality<Double>{

	private static final long serialVersionUID = 5017103889647867445L;

	@Override
	public boolean equal(Double x, Double y) {
		//return x/2 < y && y < 2*x;
		return x/1.5 < y && y < 1.5*x;
	}

}
