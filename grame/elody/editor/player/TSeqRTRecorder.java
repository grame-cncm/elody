package grame.elody.editor.player;

import grame.elody.file.midifile.TMIDIFile;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MsgNotifier;
import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiException;

import java.util.Observer;


public final class TSeqRTRecorder extends MidiAppl {
	static final int typeSlice = 25;
	static final int TRACKNUM = 1;
	int slice = 3000;
	int seq , offset = -1;
	int endSliceDate = 0;
	TMIDIFile midireader;
	MsgNotifier  notifier;
	int count = 0;
	
	public TSeqRTRecorder () 
	{ 
		midireader = new TMIDIFile();
		notifier = new MsgNotifier (2000);
	}
	
	public TSeqRTRecorder (int slice) 
	{ 
		midireader = new TMIDIFile();
		notifier = new MsgNotifier (2000);
		this.slice = slice;
	}
	
	public void addObserver	  	(Observer o) { notifier.addObserver(o); }
    public void deleteObserver	(Observer o) { notifier.deleteObserver(o); }

	public void Open (String name) throws MidiException  
	{	
		super.Open(name);
		seq = Midi.NewSeq();
		if (seq == 0) throw new MidiException();
		for (int i = 0 ; i<256; i++) { Midi.AcceptType(filter, i,0);}
		Midi.AcceptType(filter,Midi.typeKeyOn,1);
		Midi.AcceptType(filter,Midi.typeKeyOff,1);
		Midi.AcceptType(filter,Midi.typeNote,1);
		Midi.AcceptType(filter,typeSlice,1);		
	}
	
	public void Close ()
	{
		Midi.FreeSeq(seq);
		super.Close(); //02/02/07   TEMPORAIRE (faire planter...)
	}
	
	public synchronized void ReceiveAlarm (int ev) 
	//public void ReceiveAlarm (int ev) 
	{
		int type = Midi.GetType(ev);
		int curDate = Midi.GetDate(ev);
		Midi.SetRefnum(ev,TRACKNUM);
		 
		switch (type) {
		
			case typeSlice:
				handleSlice(ev,curDate);
				break;
				
			case Midi.typeKeyOn:
				if (Midi.GetField(ev,1) == 0)  // keyOn avec vel = 0
					handleKeyOff(ev,curDate);
				else
					handleKeyOn(ev,curDate);
				break;
			
			case Midi.typeKeyOff:
				handleKeyOff(ev,curDate);
				break;
				
			case Midi.typeNote:
				handleNote(ev,curDate);
				break;	
		}
	}
	
	final synchronized void sendSlice(int date) 
	//final void sendSlice(int date) 
	{
		int endSlice  = Midi.NewEv(typeSlice);
		endSliceDate = date;
					
		if (endSlice != 0) {
			Midi.SetDate(endSlice , date);
			Midi.Send(refnum, endSlice);
		}
	}
	
	final synchronized void handleKeyOff(int ev, int curDate) 
	//final void handleKeyOff(int ev, int curDate) 
	{
		if (count > 0) {
			if( --count == 0) sendSlice(curDate + slice);
			handleEv(ev,curDate);
		} else  {	//  sinon KeyOff tout seuls !!
			Midi.FreeEv(ev);
		}
	}
	
	final synchronized void handleKeyOn(int ev, int curDate) 
	//final void handleKeyOn(int ev, int curDate) 
	{
		count++;
		endSliceDate = curDate + slice;
		handleEv(ev,curDate);
	}
	
	final synchronized  void handleNote(int ev, int curDate) 
	//final  void handleNote(int ev, int curDate) 
	{
		sendSlice(curDate + slice);
		handleEv(ev,curDate);
	}
	
	final synchronized void handleEv(int ev , int curDate) 
	//final void handleEv(int ev , int curDate) 
	{
		if (offset < 0) offset = curDate;  // début d'une nouvelle tranche
		Midi.SetDate(ev, curDate - offset);
		Midi.AddSeq(seq, ev);
	}
	
	final synchronized void handleSlice(int ev, int curDate)
	//final void handleSlice(int ev, int curDate) 
	{
		if ((curDate >= endSliceDate) && !IsEmptySeq()) { 
			try {
				TExp res = midireader.readTrack(seq,TRACKNUM);
				notifier.notifyObservers (res);
				Midi.ClearSeq(seq);
				offset = -1;  // fin de tranche
			}catch (MidiException e) {
				System.out.println (e);
			}
		}
		Midi.FreeEv(ev);
	}
	
	final boolean IsEmptySeq() { return (Midi.GetFirstEv(seq) == 0);}
}
