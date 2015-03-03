package attributes.scales;

/*Trujillo- Apr 6, 2014
 * Just an scale from bits to Kbits
 * */
public class KbitsScale extends LinearScale{

	
	public KbitsScale(){
		super(1024);
	}

	@Override
	public String scaleMeaning(Long value) {
		return value+"\\texttt{Kb}";
	}
	
}
