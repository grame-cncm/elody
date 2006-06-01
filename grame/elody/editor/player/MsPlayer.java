package grame.elody.editor.player;

import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiException;
import grame.midishare.SmpteLoc;
import grame.midishare.midifile.MidiFileInfos;
import grame.midishare.midifile.MidiFileStream;
import grame.midishare.player.MidiPlayer;
import grame.midishare.player.PlayerPos;
import grame.midishare.player.PlayerState;

import java.io.File;
import java.net.URL;
import java.util.StringTokenizer;

public class MsPlayer extends MidiAppl {
	
	public MsPlayerInterface myUser = null;
		
	private MidiFileInfos  info = null;
	private PlayerState state = null;
	private PlayerPos pos  = null;
	
	private int tracknum = 1;
	
	public MsPlayer (){
		super();
	}

	public MsPlayer (MsPlayerInterface myUser){
		super();
		this.myUser = myUser;
	}

	
	public void Open (String name)throws MidiException{
	  if (refnum == -1) {
		 refnum = MidiPlayer.Open(name);
		 state = new PlayerState();
		 pos = new PlayerPos();
		 info = new MidiFileInfos();
		 if (refnum < 0) throw new MidiException ("Player Open Error ");
	  }
	}
		
	public void Close (){
		Stop();
		MidiPlayer.Close(refnum);
		refnum = -1;
  	}
	
	
	// Transport
	
	public void Stop(){
		MidiPlayer.Stop(refnum);
		MidiPlayer.Record(refnum, MidiPlayer.kNoTrack);
	}

	public void Cont(){
		MidiPlayer.Cont(refnum);
	}
	
	public void Start(){
		MidiPlayer.Start(refnum);
	}
	
	public void Pause(){
		MidiPlayer.Pause(refnum);
	}
	
	// Recording
  	
  	public void SetRecordMode(int mode){
 		MidiPlayer.SetRecordMode(refnum,mode);
  	}	
  	
 	public void Record(int num){
		tracknum = num;
		MidiPlayer.Record(refnum, tracknum);
	}

	public void  SetRecordFilter (int filter)
	{
		MidiPlayer.SetRecordFilter (refnum,filter);
	}
	
	
	// Position
	
	public void SetPosMs(int date){
		MidiPlayer.SetPosMs(refnum, date);
	}
	public void SetPosBBU(PlayerPos pos){
		MidiPlayer.SetPosBBU(refnum, pos);
	}
	
	public void SetPosBBU(String position){
		
		if (position != null) {
			StringTokenizer st = new StringTokenizer(position);
			pos.bar = (short)Integer.parseInt(st.nextToken(":"));
			pos.beat =(short)Integer.parseInt( st.nextToken(":"));
			pos.unit = (short)Integer.parseInt(st.nextToken());
	
	  		MidiPlayer.SetPosBBU(refnum, pos);
	  	}
	}

	// Loop state
	
	public void SetLoop(int state){
		MidiPlayer.SetLoop(refnum,state);
  	}	


	// LoopStart

	public int SetLoopStartMs(int date){
		return MidiPlayer.SetLoopStartMs(refnum, date);
	}

	public int SetLoopStartBBU(PlayerPos loopstart){
		return MidiPlayer.SetLoopStartBBU(refnum, loopstart);
	}

	public int SetLoopStartBBU(String loopstart){
		
		if (loopstart != null) {
			StringTokenizer st = new StringTokenizer(loopstart);
			
			pos.bar = (short)Integer.parseInt(st.nextToken(":"));
			pos.beat = (short)Integer.parseInt(st.nextToken(":"));
			pos.unit = (short)Integer.parseInt(st.nextToken());
			
			return MidiPlayer.SetLoopStartBBU(refnum, pos);
	 	}else
	  		return MidiPlayer.PLAYERerrSequencer;
	}
	
	// LoopEnd
	
	public int SetLoopEndMs(int date){
		return MidiPlayer.SetLoopEndMs(refnum, date);
	}

	
	public int SetLoopEndBBU(PlayerPos loopend){
		return MidiPlayer.SetLoopEndBBU(refnum, loopend);
	}

	public int SetLoopEndBBU(String loopend){
	
		if (loopend != null) {
			StringTokenizer st = new StringTokenizer(loopend);
			
			pos.bar = (short)Integer.parseInt(st.nextToken(":"));
			pos.beat = (short)Integer.parseInt(st.nextToken(":"));
			pos.unit = (short)Integer.parseInt(st.nextToken());
			
			return MidiPlayer.SetLoopEndBBU(refnum, pos);
			
		 }else
	  	return MidiPlayer.PLAYERerrSequencer;
	}
		
	// Synchronisation
	
	public void SetSynchroIn(int sync){
		MidiPlayer.SetSynchroIn(refnum, sync);
	}
	
	public void SetSynchroOut(int sync){
		MidiPlayer.SetSynchroOut(refnum, sync);
	}
	
	public void SetSMPTEOffset(SmpteLoc smptepos){
		MidiPlayer.SetSMPTEOffset(refnum, smptepos);
	}
	
	public void SetTempo(int tempo){
		MidiPlayer.SetTempo(refnum, tempo);
	}


	// State management

	public PlayerState GetState (){
		MidiPlayer.GetState(refnum, state);
		return state;
 	}
 	
 	public PlayerState GetEndScore (){
		MidiPlayer.GetEndScore(refnum, state);
		return state;
 	}

	// Step playing
	
	public void ForwardStep (int state){
		MidiPlayer.ForwardStep(refnum,state);
  	}	
  	
 	 public void BackwardStep (int state){
		MidiPlayer.ForwardStep(refnum,state);
  	}	


	// Tracks management
	
	public int GetAllTrack (){
		return MidiPlayer.GetAllTrack(refnum);
  	}	
  	
 	public int GetTrack (int tracknum){
		return MidiPlayer.GetTrack(refnum,tracknum);
  	}	
	
	public int SetTrack  (int tracknum, int s){
		return MidiPlayer.SetTrack(refnum, tracknum, s);
	}
	public int SetAllTrack  (int s, int ticks_per_quarter){
		return MidiPlayer.SetAllTrack(refnum, s, ticks_per_quarter);
	}
	
	public int InsertTrack  (int tracknum, int s){
		return MidiPlayer.InsertTrack(refnum, tracknum, s);
	}
	public int InsertAllTrack  (int s){
		return MidiPlayer.InsertAllTrack(refnum, s);
	}
	
	public void SetParam  (int tracknum, int param , int value){
		
		 MidiPlayer.SetParam(refnum,tracknum,  param ,  value);
	}
	public int  GetParam  (int tracknum, int  param){
		 return MidiPlayer.GetParam(refnum,tracknum,  param );
	}
	

	// MidiFile management

	public void Load (URL filename) throws Exception{
		int seq = Midi.NewSeq();
			
		if (seq != 0) {	
			MidiFileStream mf = new MidiFileStream();	
			//System.out.println(filename);	
			mf.Load(filename,seq,info);
			//System.out.println("Loaded");	
			MidiPlayer.SetAllTrack(refnum, seq, info.clicks);
		}else{
			throw new MidiException ("No more MidiShare event");
		}
	}
	
	public void Load (File filename) throws Exception{
		int seq = Midi.NewSeq();
			
		if (seq != 0) {	
			MidiFileStream mf = new MidiFileStream();	
			mf.Load(filename,seq,info);
			MidiPlayer.SetAllTrack(refnum, seq, info.clicks);
		}else{
			throw new MidiException ("No more MidiShare event");
		}
	}
	
	public void Save (URL filename) throws MidiException{
		int seq = 0;
		
		try {
			MidiFileStream mf = new MidiFileStream();		
			info.clicks = 500;
			info.format = MidiFileInfos.midifile1;
			info.timedef = MidiFileInfos.TicksPerQuarterNote;
			seq = MidiPlayer.GetAllTrack(refnum);
			mf.Save (filename,seq, info);
			Midi.FreeSeq(seq);
			
		}catch (Exception e){
			System.out.println(e);	
		}
	}
	
	public void Save (File filename) throws MidiException{
		int seq = 0;
		
		try {
			MidiFileStream mf = new MidiFileStream();		
			info.clicks = 500;
			info.format = MidiFileInfos.midifile1;
			info.timedef = MidiFileInfos.TicksPerQuarterNote;
			seq = MidiPlayer.GetAllTrack(refnum);
			mf.Save (filename,seq, info);
			Midi.FreeSeq(seq);
			
		}catch (Exception e){
			Midi.FreeSeq(seq);
			System.out.println(e);	
		}
	}
}
