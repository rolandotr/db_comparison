package protocols.specifications;

import java.math.BigDecimal;

import org.omg.CORBA.TCKind;

import attributes.Attribute;
import attributes.CryptoCalls;
import attributes.DistanceFraudProbability;
import attributes.FinalSlowPhase;
import attributes.MafiaFraudProbability;
import attributes.Memory;
import attributes.SizeOfMessages;
import attributes.TerroristFraudProbability;
import attributes.TotalBitsExchanged;
import attributes.relations.BitsExchangedRelation;
import attributes.relations.FinalSlowPhaseRelation;
import attributes.relations.IntegerRelation;
import attributes.relations.MemoryRelation;
import attributes.relations.ProbabilityRelation;
import attributes.relations.SizeOfMessagesRelation;
import attributes.scales.KbitsScale;
import attributes.scales.LogScale;
import attributes.scales.NoScale;

import methodology.History;

public class BrandsAndChaumProtocol extends DBProtocol{
	
	private static final long serialVersionUID = 4953569397230687732L;

	private int sizeOfCommit;
	
	public BrandsAndChaumProtocol(int sizeOfCommit) {
		this.sizeOfCommit = sizeOfCommit;
	}
	
	public BrandsAndChaumProtocol() {
		this(SIZE_OF_COMMIT);
	}
	
	@Override
	public String getAcronym() {
		return "BC";
	}

	@Override
	public BigDecimal getMafiaFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getDistanceFraudProbability() {
		return ONE_OVER_TWO.pow(n);
	}

	@Override
	public BigDecimal getTerroristFraudProbability() {
		return ONE;
	}

	@Override
	public boolean hasFinalSlowPhase() {
		return true;
	}

	@Override
	public int getCryptoCalls() {
		return 2;
	}


	@Override
	public DBProtocol[] getInstances() {
		DBProtocol[] result = new DBProtocol[MAX_N];
		for (int i = 0; i < MAX_N; i++){
			result[i] = new BrandsAndChaumProtocol();
			result[i].setNumberOfRounds(i+1);
		}
		return result;
	}

	@Override
	public String getIdentifier() {
		return "BC-{"+n+"}";
	}

	@Override
	public int getYearOfPublication() {
		return 1993;
	}

	@Override
	public boolean lackSecurityProof() {
		return true;
	}

	@Override
	public int getSizeOfTheChannel() {
		return 1;
	}

	@Override
	public DBProtocol getInstance() {
		return new BrandsAndChaumProtocol();
	}

	@Override
	public int getTotalMsgSizeReceived() {
		return n;
	}

	@Override
	public int getTotalOutputSizeOfCallsToFunctions() {
		return 3*sizeOfCommit;
	}

	@Override
	public int getNumberOfNoncesGenerated() {
		return 0;
	}

	@Override
	public int getBitsGenerated() {
		return n;
	}
	
	public static void main(String[] args) {
		/*BrandsAndChaumProtocol p1 = new BrandsAndChaumProtocol();
		p1.setNumberOfRounds(32);
		BrandsAndChaumProtocol p2 = new BrandsAndChaumProtocol();
		p2.setNumberOfRounds(31);*/
		BrandsAndChaumProtocol p1 = new BrandsAndChaumProtocol();
		p1.setNumberOfRounds(32);
		BussardAndBaggaProtocol p2 = new BussardAndBaggaProtocol();
		p2.setNumberOfRounds(32);
		Attribute[] attributes = new Attribute[]{
				new MafiaFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new DistanceFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				new TerroristFraudProbability(new ProbabilityRelation(), new LogScale(2)),
				//new TotalBitsExchanged(new BitsExchangedRelation(), new NoScale<Integer>()),
				new TotalBitsExchanged(new IntegerRelation(), new NoScale<Integer>()),
				new SizeOfMessages(new SizeOfMessagesRelation(), new NoScale<Integer>()),
				new CryptoCalls(new IntegerRelation(), new NoScale<Integer>()),
				new Memory(new MemoryRelation(), new KbitsScale()),
				new FinalSlowPhase(new FinalSlowPhaseRelation(), new NoScale<Boolean>()),
		};

		History.printInfoOfDomination(p1, p2, attributes);
	}

}
