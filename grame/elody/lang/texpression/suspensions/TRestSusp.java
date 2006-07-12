package grame.elody.lang.texpression.suspensions;

import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TRestExp;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

import java.awt.Color;


public final class TRestSusp extends TSusp {
	public TValue arg1;
	public float m;
 	
 	public TRestSusp (TValue arg1, float m) { this.arg1 = arg1; this.m = m;}
 	
	public TValue 	Force() { 
		Debug.Trace( "Force", this);
		TValue res = arg1.Rest(m);
		//arg1 = null;
		return res;
	}
	
	public TExp Reify(float dur) {
		if (dur > 0) {
		 	return arg1.Rest(m).Reify(dur);
		}else {
			//System.out.println ("End Reify TRestSusp");
			return new TRestExp (arg1.Reify(dur), new TEvent (TExp.SOUND ,Color.black, 0,0,0, m));
		}
	}
}
