package random_cayley;

public class IntegerSumModK implements IntegerGroupOperation {
	
	protected int k;

	public IntegerSumModK(int valueK) {
		k = valueK;
	}
	
	@Override
	public Integer compute(Integer oper1, Integer oper2) {
		return new Integer((oper1.intValue() + oper2.intValue()) % k);
	}

}
