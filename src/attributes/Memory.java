package attributes;

public class Memory extends DoubleAttribute{

	public Memory(long value){
		super(value);
	}

	@Override
	public Memory getInstance() {
		return new Memory(-1);
	}
	
	@Override
	public String toString() {
		return "memory = "+getValue();
	}
}
