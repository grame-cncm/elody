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
*	 TSeqVisitor (classe) : visiteur de valeurs, convertit une valeur en séquence MidiShare
*                           nombre d'event max et durée maximum
*                         
* 
*******************************************************************************************/

public final class TSeqVisitor extends TBValueVisitor {
	
	int seq;
	int count = 20000;
	int dur = 600000;
	
	public TSeqVisitor (){}
	
	public TSeqVisitor (int ev, int d) { count = ev; dur = d; }
	
	public boolean contVisite(int date) { return (count > 0) && (date < dur);}
	
	 public void Visite(TEvent ev,int date, Object arg) {
		int chan = (int)MathUtils.setRange(ev.getChan(),0f,32f);
	
		if ((int)ev.getType() == TExp.SOUND) {
			int keyOn = Midi.NewEv(Midi.typeKeyOn);
			
 			if (keyOn != 0) {
 				Midi.SetField(keyOn, 0, (int)MathUtils.setRange(ev.getPitch(),0f,127f));
 				Midi.SetField(keyOn, 1, (int)MathUtils.setRange(ev.getVel(),0f,127f));
 				Midi.SetChan(keyOn, chan%16);
 				Midi.SetPort(keyOn, chan/16);
 				Midi.SetDate(keyOn,date);
 				
 				int keyOff = Midi.CopyEv(keyOn);
 					if (keyOff != 0){
 						Midi.SetType(keyOff, Midi.typeKeyOff);
 						Midi.SetDate(keyOff, date + (int)(ev.getDur()));
 						Midi.AddSeq(seq, keyOn);
 						Midi.AddSeq(seq, keyOff);
 					}
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

	// Methode publique
	
	public int fillSeq(TExp exp,int s) {
		seq = s;
		TEvaluator.gEvaluator.Render (exp,this, 0, null);
		return s;
	}
}
