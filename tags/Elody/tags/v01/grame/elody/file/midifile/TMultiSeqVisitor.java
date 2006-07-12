package grame.elody.file.midifile;

import grame.elody.lang.TEvaluator;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.valeurs.TApplVal;
import grame.elody.lang.texpression.valeurs.TClosure;
import grame.elody.lang.texpression.valeurs.TSequenceVal;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.MathUtils;
import grame.midishare.Midi;

/*******************************************************************************************
*
*	 TMultiSeqVisitor (classe) :visiteur de valeurs, convertit une valeur en séquence MidiShare
*    	
*    - les pistes sont constitués d'événement toujours consécutifs dans le temps, crée autant de pistes
*      qu'il faut.
*                          		
*******************************************************************************************/

public final class TMultiSeqVisitor extends TBValueVisitor {
	
	int seq;
	int count = 20000;
	int dur = 600000;
	int curChan = 0;
	int durTable[];
	static final int MaxTrack = 255;
	
	public TMultiSeqVisitor (){
		durTable = new int[MaxTrack];
		//for (int i = 0; i<MaxTrack; i++) {durTable[i] = 0;}
	}
	
	public TMultiSeqVisitor (int ev, int d) {
		durTable = new int[MaxTrack];
		count = ev; dur = d; 
	}
	
	// rend le numéro de la prochaine piste disponible pour écrire l'ev
	int getTracknum(int date,int dur) {
		for (int i = 0; i<MaxTrack; i++) {
			if (date >= durTable[i]) {
				durTable[i] = date + dur;
				//return i+1;
				return i;
			}
		}
		System.out.println ("No more available track when writing the MidiFile");
		return MaxTrack; // si pas de piste disponible, tout sur le dernière
	}
	
	public boolean contVisite(int date) { return (count > 0) && (date < dur);}
	
	public void Visite(TEvent ev,int date, Object arg) {
		int chan = (int)MathUtils.setRange(ev.getChan(),0f,32f);
		int dur = (int)(ev.getDur());
	
		if ((int)ev.getType() == TExp.SOUND) {
			int event = Midi.NewEv(Midi.typeNote);
 			if (event != 0) {
 				Midi.SetField(event, 0, (int)MathUtils.setRange(ev.getPitch(),0f,127f));
 				Midi.SetField(event, 1, (int)MathUtils.setRange(ev.getVel(),0f,127f));
 				Midi.SetField(event, 2, dur);
 				Midi.SetChan(event, chan%16);
 				Midi.SetPort(event, chan/16);
 				Midi.SetDate(event,date);
 				Midi.SetRefnum(event,getTracknum(date,dur));
 				Midi.AddSeq(seq, event);
 				count--;
 			}
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
	
	public void renderExp(TExp exp) {}

	public TMultiSeqResult fillSeq(TExp exp,int s) {
		seq = s;
		TEvaluator.gEvaluator.Render (exp,this, 0, null);
		
		//return s;
		return new TMultiSeqResult(seq, durTable);
	}
}
