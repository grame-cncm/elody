package grame.elody.editor.player;

import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiException;
import grame.midishare.SmpteLoc;
import grame.midishare.midifile.MidiFileInfos;
import grame.midishare.midifile.MidiFileStream;
import grame.midishare.player.MidiPlayer1;
import grame.midishare.player.PlayerPos;
import grame.midishare.player.PlayerState;

import java.io.File;
import java.net.URL;
import java.util.StringTokenizer;

public final class MsPlayer1 extends MidiAppl {
	
	public MsPlayerInterface myUser = null;
		
	private MidiFileInfos  info = null;
	private PlayerState state = null;
	private PlayerPos pos  = null;
	
	private int tracknum = 1;
	
	public MsPlayer1 (){
		super();
	}

	public MsPlayer1 (MsPlayerInterface myUser){
		super();
		this.myUser = myUser;
	}

	
	public synchronized void Open (String name)throws MidiException{
	  if (refnum == -1) {
		 refnum = MidiPlayer1.Open(name);
		 state = new PlayerState();
		 pos = new PlayerPos();
		 info = new MidiFileInfos();
		 if (refnum < 0) throw new MidiException ("Player Open Error ");
	  }
	}
		
	public synchronized void Close (){
		Stop();
		MidiPlayer1.Close(refnum);
		refnum = -1;
  	}
	
	
	// Transport
	
	public synchronized void Stop(){
		MidiPlayer1.Stop(refnum);
		MidiPlayer1.Record(refnum, MidiPlayer1.kNoTrack);
	}

	public synchronized void Cont(){
		MidiPlayer1.Cont(refnum);
	}
	
	public synchronized void Start(){
		MidiPlayer1.Start(refnum);
	}
	
	public synchronized void Pause(){
		MidiPlayer1.Pause(refnum);
	}
	
	// Recording
  	
  	public synchronized void SetRecordMode(int mode){
 		MidiPlayer1.SetRecordMode(refnum,mode);
  	}	
  	
 	public synchronized void Record(int num){
		tracknum = num;
		MidiPlayer1.Record(refnum, tracknum);
	}

	public synchronized void SetRecordFilter (int filter)
	{
		MidiPlayer1.SetRecordFilter (refnum,filter);
	}
	
	
	// Position
	
	public synchronized void SetPosMs(int date){
		MidiPlayer1.SetPosMs(refnum, date);
	}
	public synchronized void SetPosBBU(PlayerPos pos){
		MidiPlayer1.SetPosBBU(refnum, pos);
	}
	
	public synchronized void SetPosBBU(String position){
		
		if (position != null) {
			StringTokenizer st = new StringTokenizer(position);
			pos.bar = (short)Integer.parseInt(st.nextToken(":"));
			pos.beat =(short)Integer.parseInt( st.nextToken(":"));
			pos.unit = (short)Integer.parseInt(st.nextToken());
	
	  		MidiPlayer1.SetPosBBU(refnum, pos);
	  	}
	}

	// Loop state
	
	public synchronized void SetLoop(int state){
		MidiPlayer1.SetLoop(refnum,state);
  	}	


	// LoopStart

	public synchronized int  SetLoopStartMs(int date){
		return MidiPlayer1.SetLoopStartMs(refnum, date);
	}

	public synchronized int  SetLoopStartBBU(PlayerPos loopstart){
		return MidiPlayer1.SetLoopStartBBU(refnum, loopstart);
	}

	public synchronized int  SetLoopStartBBU(String loopstart){
		
		if (loopstart != null) {
			StringTokenizer st = new StringTokenizer(loopstart);
			
			pos.bar = (short)Integer.parseInt(st.nextToken(":"));
			pos.beat = (short)Integer.parseInt(st.nextToken(":"));
			pos.unit = (short)Integer.parseInt(st.nextToken());
			
			return MidiPlayer1.SetLoopStartBBU(refnum, pos);
	 	}else
	  		return MidiPlayer1.PLAYERerrSequencer;
	}
	
	// LoopEnd
	
	public synchronized int  SetLoopEndMs(int date){
		return MidiPlayer1.SetLoopEndMs(refnum, date);
	}

	
	public synchronized int  SetLoopEndBBU(PlayerPos loopend){
		return MidiPlayer1.SetLoopEndBBU(refnum, loopend);
	}

	public synchronized int  SetLoopEndBBU(String loopend){
	
		if (loopend != null) {
			StringTokenizer st = new StringTokenizer(loopend);
			
			pos.bar = (short)Integer.parseInt(st.nextToken(":"));
			pos.beat = (short)Integer.parseInt(st.nextToken(":"));
			pos.unit = (short)Integer.parseInt(st.nextToken());
			
			return MidiPlayer1.SetLoopEndBBU(refnum, pos);
			
		 }else
	  	return MidiPlayer1.PLAYERerrSequencer;
	}
		
	// Synchronisation
	
	public synchronized void SetSynchroIn(int sync){
		MidiPlayer1.SetSynchroIn(refnum, sync);
	}
	
	public synchronized void SetSynchroOut(int sync){
		MidiPlayer1.SetSynchroOut(refnum, sync);
	}
	
	public synchronized void SetSMPTEOffset(SmpteLoc smptepos){
		MidiPlayer1.SetSMPTEOffset(refnum, smptepos);
	}
	
	public synchronized void SetTempo(int tempo){
		MidiPlayer1.SetTempo(refnum, tempo);
	}


	// State management

	public synchronized PlayerState  GetState (){
		MidiPlayer1.GetState(refnum, state);
		return state;
 	}
 	
 	public synchronized PlayerState  GetEndScore (){
		MidiPlayer1.GetEndScore(refnum, state);
		return state;
 	}

	// Step playing
	
	public synchronized void ForwardStep (int state){
		MidiPlayer1.ForwardStep(refnum,state);
  	}	
  	
 	 public synchronized void BackwardStep (int state){
		MidiPlayer1.ForwardStep(refnum,state);
  	}	


	// Tracks management
	
	public synchronized int  GetAllTrack (){
		return MidiPlayer1.GetAllTrack(refnum);
  	}	
  	
 	public synchronized int  GetTrack (int tracknum){
		return MidiPlayer1.GetTrack(refnum,tracknum);
  	}	
	
	public synchronized int  SetTrack  (int tracknum, int s){
		return MidiPlayer1.SetTrack(refnum, tracknum, s);
	}
	public synchronized int  SetAllTrack  (int s, int ticks_per_quarter){
		return MidiPlayer1.SetAllTrack(refnum, s, ticks_per_quarter);
	}
	
	public synchronized int  InsertTrack  (int tracknum, int s){
		return MidiPlayer1.InsertTrack(refnum, tracknum, s);
	}
	public synchronized int  InsertAllTrack  (int s){
		return MidiPlayer1.InsertAllTrack(refnum, s);
	}
	
	public synchronized void SetParam  (int tracknum, int param , int value){
		
		 MidiPlayer1.SetParam(refnum,tracknum,  param ,  value);
	}
	public synchronized int   GetParam  (int tracknum, int  param){
		 return MidiPlayer1.GetParam(refnum,tracknum,  param );
	}
	

	// MidiFile management

	public synchronized void Load (URL filename) throws Exception{
		int seq = Midi.NewSeq();
			
		if (seq != 0) {	
			MidiFileStream mf = new MidiFileStream();	
			//System.out.println(filename);	
			mf.Load(filename,seq,info);
			//System.out.println("Loaded");	
			MidiPlayer1.SetAllTrack(refnum, seq, info.clicks);
		}else{
			throw new MidiException ("No more MidiShare event");
		}
	}
	
	public synchronized void Load (File filename) throws Exception{
		int seq = Midi.NewSeq();
			
		if (seq != 0) {	
			MidiFileStream mf = new MidiFileStream();	
			mf.Load(filename,seq,info);
			MidiPlayer1.SetAllTrack(refnum, seq, info.clicks);
		}else{
			throw new MidiException ("No more MidiShare event");
		}
	}
	
	public synchronized void Save (URL filename) throws MidiException{
		int seq = 0;
		
		try {
			MidiFileStream mf = new MidiFileStream();		
			info.clicks = 500;
			info.format = MidiFileInfos.midifile1;
			info.timedef = MidiFileInfos.TicksPerQuarterNote;
			seq = MidiPlayer1.GetAllTrack(refnum);
			mf.Save (filename,seq, info);
			Midi.FreeSeq(seq);
			
		}catch (Exception e){
			System.out.println(e);	
		}
	}
	
	public synchronized void Save (File filename) throws MidiException{
		int seq = 0;
		
		try {
			MidiFileStream mf = new MidiFileStream();		
			info.clicks = 500;
			info.format = MidiFileInfos.midifile1;
			info.timedef = MidiFileInfos.TicksPerQuarterNote;
			seq = MidiPlayer1.GetAllTrack(refnum);
			mf.Save (filename,seq, info);
			Midi.FreeSeq(seq);
			
		}catch (Exception e){
			Midi.FreeSeq(seq);
			System.out.println(e);	
		}
	}
}
