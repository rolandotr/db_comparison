package protocols;

public class State {

	int mafiaIndex = 0;
	int distanceIndex = 0;
	int terroristIndex = 0;
	int bitsExchangedIndex = 0;
	int sizOfMessageIndex = 0;
	int cryptoCallsIndex = 0;
	int memoryIndex = 0;
	int finalSlowPhaseIndex = 0;
	
	public State(int mafiaIndex, int distanceIndex, int terroristIndex,
			int bitsExchangedIndex, 
			int sizOfMessageIndex,
			int cryptoCallsIndex, int memoryIndex, int finalSlowPhaseIndex) {
		super();
		this.mafiaIndex = mafiaIndex;
		this.distanceIndex = distanceIndex;
		this.terroristIndex = terroristIndex;
		this.bitsExchangedIndex = bitsExchangedIndex;
		this.sizOfMessageIndex = sizOfMessageIndex;
		this.cryptoCallsIndex = cryptoCallsIndex;
		this.memoryIndex = memoryIndex;
		this.finalSlowPhaseIndex = finalSlowPhaseIndex;
	}

	public void removeToMafiaChild() {
		mafiaIndex--;
	}

	public void removeToDistanceChild() {
		distanceIndex--;
	}

	public void removeToTerroristChild() {
		terroristIndex--;
	}

	public void removeToSizeOfMessagesChild() {
		sizOfMessageIndex--;
	}

	public void removeToCryptoCallsChild() {
		cryptoCallsIndex--;
	}

	public void removeToMemoryChild() {
		memoryIndex--;
	}

	public void removeToFinalSlowPhaseChild() {
		finalSlowPhaseIndex--;
	}

	public void moveToMafiaChild() {
		mafiaIndex++;
	}

	public void moveToDistanceChild() {
		distanceIndex++;
	}

	public void moveToTerroristChild() {
		terroristIndex++;
	}

	public void moveToBitsExchangedChild() {
		bitsExchangedIndex++;
	}

	public void moveToSizeOfMessagesChild() {
		sizOfMessageIndex++;
	}

	public void moveToCryptoCallsChild() {
		cryptoCallsIndex++;
	}

	public void moveToMemoryChild() {
		memoryIndex++;
	}

	public void moveToFinalSlowPhaseChild() {
		finalSlowPhaseIndex++;
	}

	public String status() {
		return "["+mafiaIndex+","+distanceIndex+","+terroristIndex+","+
				bitsExchangedIndex+
				","+sizOfMessageIndex
				+","+cryptoCallsIndex+","+memoryIndex+","+finalSlowPhaseIndex+"]";
	}

	public State clone(){
		return new State(mafiaIndex, distanceIndex, terroristIndex, 
				bitsExchangedIndex, 
				sizOfMessageIndex, cryptoCallsIndex, memoryIndex, finalSlowPhaseIndex);
	}
}