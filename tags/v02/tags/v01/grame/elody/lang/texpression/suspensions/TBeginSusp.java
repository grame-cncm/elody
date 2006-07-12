package grame.elody.lang.texpression.suspensions;

import grame.elody.lang.texpression.expressions.TBeginExp;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

import java.awt.Color;

public final class TBeginSusp extends TSusp {
	public TValue arg1;
	public float m;
 	
 	public TBeginSusp ( TValue arg1, float m) { this.arg1 = arg1; this.m = m;}
 
 	public TValue 	Force() { 
		Debug.Trace( "Force", this);
		TValue res = arg1.Begin(m);
		//arg1 = null;
		return res;
	}
	
	public TExp Reify(float dur) {
		if (dur > 0) {
		 	return arg1.Begin(m).Reify(dur);
		}else {
			//System.out.println ("End Reify TBeginSusp");
			return new TBeginExp (arg1.Reify(dur), new TEvent (TExp.SOUND ,Color.black, 0,0,0, m));
		}
	}
}
