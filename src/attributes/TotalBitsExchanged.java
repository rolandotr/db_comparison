package attributes;

public class TotalBitsExchanged extends DoubleAttribute{

	public TotalBitsExchanged(int value){
		super(value);
	}

	@Override
	public TotalBitsExchanged getInstance() {
		return new TotalBitsExchanged(-1);
	}

	@Override
	public String toString() {
		return "bitsExchanged = "+getValue();
	}
}
