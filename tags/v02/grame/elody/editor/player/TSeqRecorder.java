package grame.elody.editor.player;

import grame.elody.file.midifile.TMIDIFile;
import grame.elody.lang.texpression.expressions.TExp;
import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiException;

public final class TSeqRecorder extends MidiAppl {
	int offset,seq = 0;
	TMIDIFile midireader;
	boolean recflag = false;
	
	public TSeqRecorder () { midireader = new TMIDIFile();}
	
	public void Open (String name) throws MidiException  {
		super.Open(name);
		seq = Midi.NewSeq();
		if (seq == 0) throw new MidiException();
		for (int i = 0 ; i<256; i++) { Midi.AcceptType(filter, i,0);}
		Midi.AcceptType(filter,Midi.typeKeyOn,1);
		Midi.AcceptType(filter,Midi.typeKeyOff,1);
		Midi.AcceptType(filter,Midi.typeNote,1);
	}
	
	public void Close (){
		Midi.FreeSeq(seq);
		super.Close();
	}
	
	public synchronized void StartRecord() {
		Midi.ClearSeq(seq);
		offset = Midi.GetTime();
		recflag = true;
	}
	
	public synchronized TExp StopRecord() {
		try {
			recflag = false;
			TExp res = midireader.readExp(seq);
			Midi.ClearSeq(seq);
			return res;
		}catch (MidiException e) {
			System.out.println (e);
			return null;
		}
	}

	public synchronized void ReceiveAlarm (int ev) {
		if (recflag){
			Midi.SetDate(ev, Midi.GetDate(ev) - offset);
			Midi.AddSeq(seq,ev);
		}else
			Midi.FreeEv(ev);
	}
}
