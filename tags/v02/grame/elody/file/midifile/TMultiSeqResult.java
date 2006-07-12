package grame.elody.file.midifile;

public final class TMultiSeqResult {
	int seq;
	int trackTable[];
	
	public TMultiSeqResult(int seq, int table[]) { this.seq = seq; this.trackTable = table;}
	
	public boolean isVoice(int i) { return (trackTable[i] > 0);}
	public int getSeq() {return seq;}
}
