/*
Marion Paulo A. Dagang
December 14, 2016
*/
class SimpleTCP {
	public int seqNum;
	public int ackNum;
	public int ackBit;
	public int synBit;
	public int finBit;
	public int winSize;
	public String data = "";

	public SimpleTCP(int seqNum, int ackNum, int ackBit, int synBit, int finBit, int winSize){
		this.seqNum = seqNum;
		this.ackNum = ackNum;
		this.ackBit = ackBit;
		this.synBit = synBit;
		this.finBit = finBit;
		this.winSize = winSize;
	}

	public SimpleTCP(int seqNum, int ackNum, int ackBit, int synBit, int finBit, int winSize, String data){
		this.seqNum = seqNum;
		this.ackNum = ackNum;
		this.ackBit = ackBit;
		this.synBit = synBit;
		this.finBit = finBit;
		this.winSize = winSize;
		this.data = data;
	}

	public String stringify(){
		if(data.equals(""))
			return "SEQN: "+seqNum+", ACKN: "+ackNum+", ACKB: "+ackBit+", SYNB: "+synBit+", FINB: "+finBit+", WS: "+winSize;
		return "SEQN: "+seqNum+", ACKN: "+ackNum+", ACKB: "+ackBit+", SYNB: "+synBit+", WS: "+winSize+", DATA: "+data;
	}

	public SimpleTCP decode(String s){
		String parts[] = s.split(",");
		if(parts.length == 6){
			return new SimpleTCP(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
		}
		return new SimpleTCP(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), parts[6]);
	}
}