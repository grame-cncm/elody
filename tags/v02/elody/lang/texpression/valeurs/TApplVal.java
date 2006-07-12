package grame.elody.lang.texpression.valeurs;

import grame.elody.lang.texpression.expressions.TApplExp;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.util.Debug;

import java.awt.Color;


public final class TApplVal implements TValue {
	public TValue arg1;
	public TValue arg2;
	float  duration = -1;
	
	public TApplVal (TValue arg1,  TValue arg2) { 
		 this.arg1 = arg1; this.arg2 = arg2;
	}
  	
    public TValue 	Modify(int index, float value, TOperator op) { 
		Debug.Trace( "Modify", this);
		return new TApplVal (arg1.Modify(index,value,op), arg2);
	}
	
	public TValue 	Begin(float n) {
		return new TApplVal (arg1.Begin(n), arg2);
	}
		
	public TValue 	Rest(float n) {
		return new TApplVal (arg1.Rest(n), arg2);
	}

	public TValue 	Apply(TValue susp) {
		return new TApplVal (this, susp);
	}	
	

	public  float 	Duration () {
		if (duration < 0) duration = DurationAux(MaxDur, MaxRecur);
		return duration;
	}
	
	public  float 	DurationAux (float limit, int n) {
	
		if (n <= 0) return 0f;
		
		float v1 = arg1.DurationAux(limit,n);
		
		if (v1 >= limit)  
			return  Float.POSITIVE_INFINITY;
		else if ((v1 < 0.5) && ( v1 > 0)) {
			return v1 + arg2.DurationAux(limit, n - 1);
		}else {
			return v1 + arg2.DurationAux(limit - v1, n);
		}
	}
		
	public void 	Accept(TValueVisitor v, int date, Object arg) {v.Visite(this, date, arg);}
	
	public  TValue getValArg1() { return arg1 ;}
	public  TValue getValArg2() { return arg2 ;}
	
	public  TValue Mute() { return new TEvent(TEvent.SILENCE,Color.black,0f,0f,0f, Duration());}
	
	public TExp Reify(float dur) { return new TApplExp (arg1.Reify(dur),arg2.Reify(dur));}
}
