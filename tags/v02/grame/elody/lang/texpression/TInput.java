package grame.elody.lang.texpression;

import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TExpVisitor;
import grame.elody.lang.texpression.expressions.TIdent;
import grame.elody.lang.texpression.expressions.TModifyExp;
import grame.elody.lang.texpression.operateurs.TAdd;
import grame.elody.lang.texpression.operateurs.TMult;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.lang.texpression.suspensions.TEvalSusp;
import grame.elody.lang.texpression.valeurs.TApplVal;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.lang.texpression.valeurs.TValueVisitor;
import grame.elody.util.Debug;

import java.awt.Color;


/***********************************/
//TInput
/***********************************/

public class TInput extends TExp implements TValue, Cloneable{

	private float val [];

	public TInput() {
		val = new float[7];
		//val[DURATION] = Float.POSITIVE_INFINITY;
		val[DURATION] = 600000;
		val[EXPAND] = 1;
	}

	public TInput(float pitch, float vel, float chan, float dur, float delay, float expand) {
		val = new float[7];
 		val[PITCH] = pitch;
 		val[VEL] = vel;
 		val[CHAN] = chan;
 		val[DURATION] = dur;
 		val[DELAY] = delay;
 		val[EXPAND] = expand;
	}
	
	public TValue 	Eval (TEnv env) { 
 		Debug.Trace( "Eval", this);
 		return this;
 	}
 	public TValue 	Suspend( TEnv env){ 
 		Debug.Trace(  "Suspend", this); 
 		return new TEvalSusp (this,env);
 	} 
	
	final public  TValue Begin(float n) {  
		TInput ev = (TInput)clone(); 
		//ev.val[DURATION] =  Math.min (n,ev.val[DURATION]);
		ev.val[DURATION] =  Math.min (n/ev.val[EXPAND],	ev.val[DURATION]);
		return ev;
	}
	final public  TValue Rest(float n) { 
		TInput ev = (TInput)clone(); 
		n = n/ev.val[EXPAND];
		ev.val[DELAY]+=n;
		
		if((ev.val[DURATION]-n) < 0) {
			//System.out.println("Dur < 0" + ev.val[DURATION] + " " + n);
			ev.val[DURATION] = 0;
		}else {
			ev.val[DURATION]-=n;
		}
		
		return ev;
		
	}
	final public  TValue Apply(TValue susp) { return new TApplVal(this, susp);}
	
	public  float 	Duration () {return DurationAux(MaxDur,MaxRecur);}
	
	public  float 	DurationAux (float limit,int n) { 
		if (n <= 0) return 0f;
		float dur = val[DURATION];
		return (dur >= limit) ? Float.POSITIVE_INFINITY :  dur * getExpand();
	}
	
	public void 	Accept(TValueVisitor v,int date, Object arg) { v.Visite(this, date, arg); }
	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	final public TValue 	Modify(int index, float value, TOperator op) { 
		Debug.Trace( "Modify", this);
		
		if (index == TExp.XPND)  // modification de duree
			return setField (6,op.Execute(getField(6),value));
		else
			return setField (index,op.Execute(getField(index),value));
	}
	
	final public float getPitch() { return val[PITCH];}
	final public float getVel() { return val[VEL];}
	final public float getChan() { return val[CHAN];}
	final public float getDur() { return val[DURATION];}
	final public float getDelay() { return val[DELAY];}
	final public float getExpand() { return val[EXPAND];}

	public float getField(int index) { return val[index];}
	
	final public  TValue Mute() { return new TEvent(TEvent.SILENCE,Color.black,0f,0f,0f, Duration());}
	final public  TValue getValArg1() {System.out.println("Error getArg1 of TEvent"); return null ;}
	final public  TValue getValArg2() {System.out.println("Error getArg2 of TEvent"); return null ;}
	
	public TExp Reify(float dur){ return this;}
	
	
	final public TInput setField(int index, float val) { 
		TInput ev = (TInput)clone();
		ev.val[index] = val;
		return ev;
	}
	
	public TExp Rebuild(TExp e, TIdent id) { 
 		Debug.Trace( "Rebuild", this);
 		return this;
 	}
  	
 	public TExp Replace(TExp f, TIdent id) { 
		Debug.Trace( "Replace", this);
		
		
		if (f instanceof TInput) {
 			TExp res = id;
			TInput e = (TInput)f;
			float v1,v2;
		
			if ((v1 = e.getDur()) != (v2 = getDur()))
				res = new TModifyExp(res,XPND,v2/v1,TMult.op);
			if ((v1 = e.getPitch()) != (v2 = getPitch()))
			 	res = new TModifyExp(res,TRSP,v2- v1 ,TAdd.op);
			if ((v1 = e.getVel()) != (v2 = getVel()))
			 	res = new TModifyExp(res,ATTN,v2-v1 ,TAdd.op);
			if ((v1 = e.getChan()) != (v2 = getChan()))
			 	res = new TModifyExp(res,INST,v2- v1 ,TAdd.op);
			if ((v1 = e.getDur()) != (v2 = getDur()))
				res = new TModifyExp(res,XPND,v2/v1,TMult.op);
			 	 	
			absTable.put(this,res); 		// insère un élément dans la table	
			return res;
	
		}else {
			return this;
		}
	}
	
	public  Object clone () {
		try {
		   	TInput ev = (TInput)super.clone();
		   	ev.val = new float[7];
	   	   	System.arraycopy(val, 0, ev.val, 0, 7);
	      	return ev;
		} catch (CloneNotSupportedException e) { 
	    	throw new InternalError();
		}
    }
   
    public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TInput) 
			&& val.equals(((TInput)obj).val));
	}
	
	public int hashCode() {
	 	if (hashcode == 0)  hashcode = (int)(getPitch() + getVel() + getChan() + getDur());
	 	return hashcode;
	}
}