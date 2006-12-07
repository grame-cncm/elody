package grame.elody.editor.player;

import grame.elody.editor.misc.TGlobals;
import grame.elody.lang.TEvaluator;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.lang.texpression.valeurs.TApplVal;
import grame.elody.lang.texpression.valeurs.TClosure;
import grame.elody.lang.texpression.valeurs.TErrorVal;
import grame.elody.lang.texpression.valeurs.TMixVal;
import grame.elody.lang.texpression.valeurs.TNamedVal;
import grame.elody.lang.texpression.valeurs.TSequenceVal;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.lang.texpression.valeurs.TValueVisitor;
import grame.elody.util.Debug;
import grame.elody.util.MathUtils;
import grame.elody.util.MsgNotifier;
import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiTask;
import grame.midishare.player.MidiPlayer;
import grame.midishare.player.PlayerState;

import java.awt.Color;
import java.util.Observer;

/*******************************************************************************************
*
*	 TEvalPlayer (classe) : visiteur de valeur, evalue des tranches, envoie la séquence MidiShare
*    obtenue dans le sequenceur C++, schedule une tâche pour évaluer la tranche suivante
* 
*******************************************************************************************/

public class TEvalPlayer implements TValueVisitor, TPlayerInterface{

	public int SLICE = 200000;
	static final int FUNON = 21;
	static final int FUNOFF = 22;

	public static final int updateMsg 	= 5001;
	
	MsPlayer1 player;		// l'enrobage sur le Player C++
	TCont cont;				// continuation 
	
	int offset = 0;   		// différence entre temps réel et temps dans la séquence 
	int stopDate = 0;   	// date en temps réel du STOP (utilisée pour l'offset) 
	
	
	int endObjectDate = 0;  // date en temps musical limite pour le calcul d'une tranche
	int objectDate = 0 ;    // date en temps musical courant pour le calcul d'une tranche
	
	int seq = 0;			// sequence MidiShare 
	TSliceTask task = null; // tache de jeu de la tranche suivante
	int num = 0; 			// numero de génération des taches
	int numFun = 0;          // numero de génération des fonctions
	
	MsgNotifier  notifier;	// notifier
	
	public TEvalPlayer ( MsPlayer1 player) {
		this.player = player; 
		notifier = new MsgNotifier (updateMsg);
		cont = new TCont();
	}
	
	public void setSynchroPlayer(int synchro) {player.SetSynchroIn(synchro);}
	
	public void addObserver	(Observer o) { notifier.addObserver(o);}
	public void deleteObserver	(Observer o) { notifier.deleteObserver(o);}
  	
 	final boolean checkEventDur(float dur) {return (dur > 5);}  
 	
 	 public void Visite(TInput ev,int date, Object arg) {
 		int vel,pitch;
		int dur = (int)ev.getDur();
		
		if (dur<5) {
			cont.init(date + (int) (dur * ev.getExpand()));
			return;
		}
		
		int event1 = Midi.NewEv(FUNON);
		int event2 = Midi.NewEv(FUNOFF);
		int chan = (int)MathUtils.setRange(ev.getChan(),0f,32f);
		if (event1 != 0 && event2 != 0) {
		
			// Codage du numéro de génération
			Midi.SetField(event2, 0, numFun*65536);
			Midi.SetField(event1, 0, numFun*65536);
			
			vel = (int)ev.getVel() + 128; 
			pitch = (int)ev.getPitch() + 128; 
			Midi.SetField(event1, 1, pitch*65536 + vel);
			
			// Codage offset et coef
			Midi.SetField(event1, 2, date - (int)ev.getDelay()) ;   // offset 
			Midi.SetField(event1, 3, (int)(ev.getExpand()*1000));	// coef
			Midi.SetChan(event1, chan%16);
			Midi.SetPort(event1, chan/16 + TGlobals.context.getPort());
			
			// Codage des dates d'applications
			Midi.SetDate(event1,(int)ev.getDelay());     		// début date d'application 
			Midi.SetDate(event2, (int)(ev.getDelay()+dur));		// fin date d'application 
		
			Midi.AddSeq(seq, event1);
			Midi.AddSeq(seq, event2);
			numFun++;
		}else {
			System.out.println("No more MidiShare event");
		}
		cont.init(date + (int) (dur * ev.getExpand()));
	}
 	
	public void Visite(TEvent ev,int date, Object arg) {
		int dur = (int)ev.getDur();
		
		if ((int)ev.getType() == TExp.SOUND && checkEventDur(dur)) {
			int event = Midi.NewEv(Midi.typeNote);
			int chan = (int)MathUtils.setRange(ev.getChan(),0f,32f);
			int durInt = dur - 2;
 			if (event != 0) {
 				Midi.SetField(event, 0, (int)MathUtils.setRange(ev.getPitch(),0f,127f));
 				Midi.SetField(event, 1, (int)MathUtils.setRange(ev.getVel(),0f,127f));
 				Midi.SetField(event, 2, durInt);
 				Midi.SetChan(event, chan%16);
 				Midi.SetPort(event, chan/16 + TGlobals.context.getPort());
 				Midi.SetDate(event,date);
 				if (durInt > 30000){ // durInt > taille d'un short signé
 					int keyOff = Midi.CopyEv(event);
 					if (keyOff != 0)  {
 						Midi.SetType(event,Midi.typeKeyOn);
 						Midi.SetType(keyOff,Midi.typeKeyOff);
 						Midi.SetDate(keyOff,date + durInt);
 						Midi.AddSeq(seq, event);
 						Midi.AddSeq(seq, keyOff);
 					}else {
 						Midi.FreeEv(event);
 						System.out.println("No more MidiShare event");
 					}
 				}else {
 					Midi.AddSeq(seq, event);
 				}
 			}else {
 				System.out.println("No more MidiShare event");
 			}
 		}
 		cont.init(date + dur);
	}
  			
	public void Visite(TSequenceVal val ,int date, Object arg) {
 		Debug.Trace( "TSeqVal", this);
 			
		val.getValArg1().Accept(this, date, arg);
		int afterDate  =  cont.getDate();  	// date de la continuation  
		
		if (cont.isNull()) {
			if (afterDate >= endObjectDate) {
				cont.init(val.getValArg2(), afterDate);
			}else{
				val.getValArg2().Accept(this, afterDate,arg);
			}
			
		}else{
			cont.init (new TSequenceVal(cont.getVal(),val.getValArg2()),afterDate);
		}
	}		
 		
	public void Visite(TMixVal val,int date, Object arg) {
		Debug.Trace( "TMixVal", this);
		
		val.getValArg1().Accept(this, date, arg);
		int date1 = cont.getDate();  // date de la continuation  
		TValue contval1 =  cont.getVal();
					
		val.getValArg2().Accept(this, date,arg);
		int date2 = cont.getDate();  // date de la continuation 
		TValue contval2 =  cont.getVal();
			
		if (contval1 == null) {
			if (contval2 == null) cont.init(Math.max (date1,date2));
		}else if (contval2 == null) {
			cont.init(contval1,date1); // remise a jour de la cont
		}else if (date1 < date2) {
			TValue val1 = new TSequenceVal(makeSil( date2 - date1) , contval2);
			cont.init(new TMixVal(contval1,val1), Math.min (date1,date2));
		}else if (date2 < date1) {
			TValue val1 = new TSequenceVal(makeSil(date1 - date2) , contval1);
			cont.init(new TMixVal(val1, contval2), Math.min (date1,date2));
		}else {
			cont.init(new TMixVal(contval1,contval2), date1); // dates égales
		}
	}
			
	public void Visite(TClosure val,int date, Object arg) {
		TValue body = val.getValArg2();
		float bdur = body.Duration();
		float coef = Float.isInfinite(bdur) ? 1 : val.Duration() / bdur;
	
		TValue val1 = TExpMaker.gExpMaker.expandVal(body,coef);
		val1.Accept(this, date,arg);	
	}
	
	public void Visite(TApplVal val,int date, Object arg) {
		new TSequenceVal (val.getValArg1(), val.getValArg2()).Accept(this, date,arg);
	}
	
	public void Visite(TErrorVal val,int date,Object arg){ System.out.println("Visite TErrorVal ");}
	public void Visite(TNullExp val,int date,Object arg){}
	public void Visite(TNamedVal val,int date,Object arg){ val.Accept(this, date,arg);}
	
	final TValue makeSil (int dur) {
		 return (TValue)TExpMaker.gExpMaker.createSilence(Color.black,0f,0f,0f,(float)dur);
	}
	
	final boolean isPlaying() {
		PlayerState state = player.GetState(); 
		return ((state.state == MidiPlayer.kPlaying) || (state.state == MidiPlayer.kWaiting));
	}
	
	final boolean isIdle() {
		PlayerState state = player.GetState(); 
		return (state.state == MidiPlayer.kIdle); 
	}
	
	final void nextGeneration() {num++;}
	
	final  void playFirstSlice() {
		endObjectDate = objectDate + SLICE/2;
		evalSlice();
	}
	
	final void playSlices() {
		nextGeneration();
		endObjectDate = objectDate + SLICE;		
		evalSlice();
	}	
	
	final void evalSlice() {
		seq = Midi.NewSeq();
		if (seq != 0) {
		
			int lastDate = (int)cont.getVal().Duration();	
			cont.getVal().Accept (this, objectDate, null); 	// évalue une tranche
			objectDate = cont.getDate();  					// recupere la date effective de fin 
				
			if (Midi.GetFirstEv(seq) != 0) { 				// seq non vide
			
				int fin = Midi.NewEv(Midi.typeText);		// ajoute en ev de fin 
				
				if (fin != 0) {
					Midi.SetText(fin,"End");
					Midi.SetDate(fin,lastDate+10);
					Midi.AddSeq(seq,fin);
				}
		
				if (Midi.FreeSpace() < 1000) Midi.GrowSpace(10000);
				int res = player.InsertAllTrack(seq); 		// ins?re et consomme la tranche calculée
				if (res == MidiPlayer.PLAYERerrSequencer){
					System.out.println("Insert Error " + res);
					Midi.FreeSeq(seq);
				}
			}
			nextSlice();
		}else {
 			System.out.println("No more MidiShare event");
 		}
	}	
	
	final  void nextSlice() {
		if (cont.isNull()) { 	
			task = null;
			notifier.notifyObservers (new Integer(cont.getDate())); // renvoie la date
		}else {
			task =  new TSliceTask(this,num);
			TGlobals.midiappl.ScheduleTask(task, Math.max(Midi.GetTime(), offset + objectDate - SLICE));
		}	
	}
	
	void startPlayerInt(TExp exp) {
		int emptySeq = Midi.NewSeq();
		if (emptySeq != 0) {
			stopPlayer ();
			int res = player.SetAllTrack(emptySeq, 500);  // vide le player
			if (res != MidiPlayer.PLAYERnoErr) {
				System.out.println("StartPlayerInt error");
				return ;
			}
			cont.init(TEvaluator.gEvaluator.Eval(exp));
			offset = Midi.GetTime();
			stopDate = 0;
			endObjectDate = 0;
			objectDate = 0;
			playFirstSlice();
			player.Start();
		}else{
			System.out.println("No more MidiShare events");
		}
	}
		
	public void  setBufferPlayer (int val) {SLICE = Math.max (val*1000, 2000);}
		
	// Methodes publiques
	
	public  void startPlayer (TExp exp) {
		nextGeneration();
		if (!(exp instanceof TNullExp)){
			startPlayerInt (exp);
		}else {
			stopPlayer ();
		}
	}
	
	public  void  stopPlayer () {
		nextGeneration();
		if (isPlaying()) {
			player.Stop();
			stopDate = Midi.GetTime();
		}
	}
	
	public  void contPlayer() {
		nextGeneration();
		if (isIdle()) {
			offset += Midi.GetTime() - stopDate;
			player.Cont();
			nextSlice();
		}
	}
	
	public  void insertPlayer (TExp exp) {
		nextGeneration();
		cont.init(TEvaluator.gEvaluator.Eval(exp));
		playSlices();
	}
	
	public  void freePlayer () {
		nextGeneration();
		player.Stop();
	}
	
	public  void setPosMsPlayer (int date_ms) {
		player.SetPosMs(date_ms);
	}

	public void renderExp (TExp exp) {}
}

/*******************************************************************************************
*
*	 TCont (classe) : continuation, utilisé par le visiteur de valeur
* 
*******************************************************************************************/

final class TCont {
	TValue val = null;
	int date = 0;
	
	public TCont (){}
		
	public TCont(TValue val) {this.val =  val;}
	public TCont(TValue val,int date) {this.val =  val; this.date = date;}
	public TCont(int date) {this.date = date;}
	
	public final TCont init (TValue val, int date) {this.val =  val; this.date =  date; return this;}
	public final TCont init (TValue val) {this.val =  val; this.date =  0; return this;}
	public final TCont init (int date) {this.val =  null; this.date =  date; return this;}
	
	int  getDate() 	{return date;}
	TValue 	getVal() {return val;}
	
	boolean isNull() {return (val == null);}
}

/*******************************************************************************************
*
*	 TSliceTask (classe) : tache d'évaluation des tranches de la valeur.
* 	 - La tache s'execute seulement si le numéro de génération n'a pas changé
* 
*******************************************************************************************/

class TSliceTask extends MidiTask {
	
	TEvalPlayer player; 
	int num = 0;
	
	public TSliceTask (TEvalPlayer player, int num) { this.player = player; this.num = num; }
	public void Execute (MidiAppl appl, int date) {  {if (num == player.num) player.playSlices();}}
}
