package attributes.scales;


/*Trujillo- Mar 24, 2014
 * This actually a particularization of the Log scale. The only difference is the meaning
 * of the base. Here the base should be the number of rounds. While in the log scale
 * will be typically 2.*/
public abstract class LinearScale implements Scale<Long>{

	private int size;
	
	public LinearScale(int size){
		this.size = size;
	}
	
	@Override
	public Long scale(Long value) {
		return (long)Math.floor(((double)value)/size);
	}

	@Override
	public Long unScale(Long value) {
		return value*size;
	}

}
