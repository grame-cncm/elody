package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.operateurs.TAdd;
import grame.elody.lang.texpression.operateurs.TMult;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.lang.texpression.suspensions.TEvalSusp;
import grame.elody.lang.texpression.valeurs.TApplVal;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.lang.texpression.valeurs.TValueVisitor;
import grame.elody.util.Debug;

import java.awt.Color;


public final class TEvent extends TExp implements TValue, Cloneable{
	private float val [];
	private Color color;
	
 	public TEvent () { val = new float[5];}
  	
  	public TEvent (int type , Color color) { 
  		val = new float[5];
  		val[TYPE] = (float)type; 
  		this.color = color;
  	}
  	 	
 	public TEvent (int type,Color color,  float pitch, float vel,float chan, float dur) { 
 		val = new float[5];
 		val[TYPE] = (float)type;
 		val[PITCH] = pitch;
 		val[VEL] = vel;
 		val[CHAN] = chan;
 		val[DURATION] = dur;
 		this.color = color;
 	}
 		
 	public TValue 	Eval (TEnv env) { 
 		Debug.Trace( "Eval", this);
 		return this;
 	}
 	public TValue 	Suspend( TEnv env){ Debug.Trace(  "Suspend", this); 
 		//return this;
 		return new TEvalSusp (this,env);
 	} 
   		
	public TValue 	Modify(int index, float value, TOperator op) { 
		Debug.Trace( "Modify", this);
		return setField (index,op.Execute(getField(index),value));
	}
	
	public TValue 	Begin(float n) {
		Debug.Trace( "Begin", this);
		if (n < getDur()) {
			return setDur(n);
		}else {
			return this;
		}
	}
		
	public TValue 	Rest(float n) {
		Debug.Trace( "Rest", this);
		if ( n < getDur()){
			return setDur(getDur() - n);
		}else{
			return new TEvent (SILENCE,Color.black);
		}
	}

	public TValue 	Apply(TValue susp){
		Debug.Trace( "Apply", this);
		return new TApplVal(this, susp);
	}
	
	
	public TExp Rebuild(TExp e, TIdent id) { 
 		Debug.Trace( "Rebuild", this);
 		return this;
 	}
 	
  	
 	public TExp Replace(TExp f, TIdent id) { 
		Debug.Trace( "Replace", this);
		
		if (f instanceof TEvent) {
 			TExp res = id;
			TEvent e = (TEvent)f;
			float v1,v2;
		
			if (color.equals(e.color)) {
			
				if (getType() == SOUND)  {
				
					if (e.getType() == SOUND) {  // note dans note
			
						if ((v1 = e.getDur()) != (v2 = getDur()))
						 	res = new TModifyExp(res,XPND,v2/v1,TMult.op);
						if ((v1 = e.getPitch()) != (v2 = getPitch()))
						 	res = new TModifyExp(res,TRSP,v2- v1 ,TAdd.op);
						if ((v1 = e.getVel()) != (v2 = getVel()))
						 	res = new TModifyExp(res,ATTN,v2-v1 ,TAdd.op);
						if ((v1 = e.getChan()) != (v2 = getChan()))
						 	res = new TModifyExp(res,INST,v2- v1 ,TAdd.op);
					}else { 					// silence dans note
					
						if ((v1 = e.getDur()) != (v2 = getDur()))
						 	res = new TModifyExp(res,XPND,v2/v1,TMult.op);
					}
						
				} else {
				
					if (e.getType() == SOUND)  {  // note dans silence
						
						if ((v1 = e.getDur()) != (v2 = getDur()))
						 	res = new TModifyExp(res,XPND,v2/v1,TMult.op);
						 
						res = new TMuteExp(res); // Mute 
						 	
					}else { 					 //silence dans silence
						
						if ((v1 = e.getDur()) != (v2 = getDur()))
						 	res = new TModifyExp(res,XPND,v2/v1,TMult.op);
					}
				}
				 	
				absTable.put(this,res); 		// insère un élément dans la table	
				return res;
				
			}else{
 				return this;
 			}
		}else {
			return this;
		}
	}
	
	public  float 	Duration () {return DurationAux(MaxDur,MaxRecur);}
	
	public  float 	DurationAux (float limit,int n) { 
		if (n <= 0) return 0f;
		float dur = getDur();
		return (dur >= limit) ? Float.POSITIVE_INFINITY :  dur;
	}
	
	
	public void 	Accept(TValueVisitor v,int date, Object arg) {
		v.Visite(this, date, arg);
	}
	
	/*
	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TEvent) 
			&& color.equals(((TEvent)obj).color)
			&& getType() == ((TEvent)obj).getType()
			&& getPitch() == ((TEvent)obj).getPitch()
			&& getVel() == ((TEvent)obj).getVel()
			&& getChan() ==( (TEvent)obj).getChan()
			&& getDur() == ((TEvent)obj).getDur());
	}
	*/
	
	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TEvent) 
			&& color.equals(((TEvent)obj).color)
			&& val.equals(((TEvent)obj).val));
			
	}
	
	public int hashCode() {
	 	if (hashcode == 0)  hashcode = (int)(getPitch() + getVel() + getChan() + getDur());
	 	return hashcode;
	}
	 
	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	 
	public  TValue getValArg1() {System.out.println("Error getArg1 of TEvent"); return null ;}
	public  TValue getValArg2() {System.out.println("Error getArg2 of TEvent"); return null ;}
	 
	public TExp unNameExp(){return this;}
		
	public TValue Mute() { return new TEvent(TEvent.SILENCE,Color.black,0f,0f,0f, Duration());}
	
	public TExp Reify(float dur) { return this;}
	
	final public float getType() { return val[TYPE];}
	final public Color getColor() { return color;}
	
	final public float getPitch() { return val[PITCH];}
	final public float getVel() { return val[VEL];}
	final public float getChan() { return val[CHAN];}
	final public float getDur() { return val[DURATION];}
	
	public float getField(int index) { return val[index];}
	
	final public TEvent setType(float type) {
		TEvent ev = (TEvent)clone();
		ev.val[TYPE] = type;
		return ev;
	}
	
	final public TEvent setColor(Color color) { 
		TEvent ev = (TEvent)clone();
		ev.color = color;
		return ev;
	}
	
	final public TEvent setPitch(float pitch) {
		TEvent ev = (TEvent)clone();
		ev.val[PITCH] = pitch;
		return ev;
	}
	
	final public TEvent setVel(float vel) {
		TEvent ev = (TEvent)clone();
		ev.val[VEL] = vel;
		return ev;
	}
	
	final public TEvent setChan(float chan) { 
		TEvent ev = (TEvent)clone();
		ev.val[CHAN] = chan;
		return ev;
	}	
	
	public TEvent setDur(float dur) { 
	final 	TEvent ev = (TEvent)clone();
		ev.val[DURATION] = dur;
		return ev;
	}
	
	final public TEvent setField(int index, float val) { 
		TEvent ev = (TEvent)clone();
		ev.val[index] = val;
		return ev;
	}
	
	public  Object clone () {
		try {
		   	TEvent ev = (TEvent)super.clone();
		   	ev.color = color;
		   	ev.val = new float[5];
	   	   	System.arraycopy(val, 0, ev.val, 0, 5);
	      	return ev;
	} catch (CloneNotSupportedException e) { 
	    	throw new InternalError();
		}
    }
}
