package attributes;

public class FinalSlowPhase extends BooleanAttribute{

	public FinalSlowPhase(boolean value){
		super(value);
	}
	
	@Override
	public String toString() {
		return "finalSlowPhase = "+getValue();
	}
}
