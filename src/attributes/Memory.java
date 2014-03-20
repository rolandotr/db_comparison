package attributes;

public class Memory extends DoubleAttribute{

	public Memory(int value){
		super(value);
	}

	@Override
	public Memory getInstance() {
		return new Memory(-1);
	}
	
}
