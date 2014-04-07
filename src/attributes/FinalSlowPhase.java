package attributes;

import attributes.scales.NoScale;

public class FinalSlowPhase extends Attribute<Boolean>{

	public FinalSlowPhase(boolean value){
		super(value, new NoScale<Boolean>());
	}
	
	@Override
	public String toString() {
		return "finalSlowPhase = "+getValue();
	}
	
	@Override
	public String getName() {
		return "FinalPhase";
	}

	@Override
	public Attribute<Boolean> getInstance() {
		return new FinalSlowPhase(value);
	}
	
}
