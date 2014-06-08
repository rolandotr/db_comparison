package attributes.scales;

import protocols.specifications.SKIProtocol;
import attributes.Attribute;
import attributes.MafiaFraudProbability;
import attributes.relations.ProbabilityRelation;


/*Trujillo- Mar 17, 2014
 * A logaritimic scale in base 2. It works with double attributes only*/
public class LogScale implements Scale<Double>{
	
	protected int base;
	
	public LogScale(int base){
		this.base = base;
	}
	
	/*Trujillo- Mar 24, 2014
	 * We return the largest integer that is closer to the logarithmic of the value*/
	public Double scale(Double value) {
		double num = Math.log(value);
		double den = Math.log(base);
		return Math.ceil(num/den);
	}
	
	public static void main(String[] args) {
		SKIProtocol p = new SKIProtocol(3, 128);
		Attribute a = p.getAttribute(new MafiaFraudProbability(0, new ProbabilityRelation(), new LogScale(2)));
		System.out.println(a.getValue());
	}


	@Override
	public Double unScale(Double value) {
		return Math.pow(value, base);
	}

	@Override
	public String scaleMeaning(Double value) {
		return base+"^{"+value+"}";
	}
}
