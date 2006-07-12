package grame.elody.lang.texpression.valeurs;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.expressions.TAbstrExp;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TIdent;
import grame.elody.lang.texpression.expressions.TModifyExp;
import grame.elody.lang.texpression.expressions.TMuteExp;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.util.Debug;

import java.awt.Color;


public final class TClosure implements TValue {
 	public float val;
 	public TExp ident;
 	public TExp body;
 	public TEnv env ;
 	
 	public TClosure (float val, TExp ident, TExp body, TEnv env) 
 		{this.val = val; this.ident = ident;   this.body = body; this.env = env; }
 			
 	public TValue 	Modify(int index, float value, TOperator op) { 
		Debug.Trace( "Modify", this);
		
		if (index == TExp.XPND)  // modification de duree
			return new TClosure(val*value, ident, body,env);
		else
			return new TClosure(val, ident, new TModifyExp(body,index,value,op),env);
	}
	
	/*
	public TValue 	Begin(float n) {
		Debug.Trace( "Begin", this);
		
		if (n < val) {
			return new TClosure(n, ident, body,env);
		}else {
			return new TSequenceVal(this, new TEvent(TEvent.SILENCE,Color.black,0f,0f,0f, n - val).Suspend(new TEnv()));
		}
	}
	*/	
	
	public TValue 	Begin(float n) {
		Debug.Trace( "Begin", this);
		
		if (n < val) {
			return new TClosure(n, ident, body,env);
		}else {
			return this;
		}
	}	
	
	public TValue 	Rest(float n) {
		Debug.Trace( "Rest", this);
		
		if (n < val) {
			return new TClosure(val - n, ident, body, env);
		}else{
			return new TEvent(TEvent.SILENCE,Color.black);
		}
	}	
	
	
	public TValue 	Apply (TValue susp) {
		Debug.Trace( "Apply", this);
		//return body.Eval(env.Bind(ident,susp)).Format(val);
		
		return body.Format(val).Eval(env.Bind(ident,susp));
		
		//return body.Eval(env.Bind(ident,susp));
	}
	
	
 	public  float 	Duration () {return DurationAux (MaxDur,MaxRecur);}
 	
  	
  	public  float DurationAux (float limit,int n) { 
 		if (n <= 0) return 0f;
 		return  (val >= limit) ?  Float.POSITIVE_INFINITY : val;
 	}
	
	public void 	Accept(TValueVisitor v, int date, Object arg) { v.Visite(this, date, arg);}
	
	
	public  TValue getValArg1() {
		return ((TIdent)ident).arg.Eval(env);
	}
	
	
	public  TValue getValArg2() {
		return body.Eval(env.Bind(ident,((TIdent)ident).arg.Suspend(env))) ;
	} 
	
	public  TValue Mute() { return new TClosure(val, ident, new TMuteExp(body),env);}
	
	public TExp Reify(float dur) {
		TEnv cur = env;
		TExp newbody = body; 
   	
   		// reconstruction du corps pour toutes les variables "libres", dont les valeurs sont dans l'environnement
	 	while (cur != null) {
	 		if (TExp.globalEnv != cur) newbody = newbody.RebuildBody(cur.getSusp().Reify(dur),cur.getIdent());
	 		cur = cur.getNext();
	 	}
	 	return new TAbstrExp (val, ident, newbody);
	}
}
