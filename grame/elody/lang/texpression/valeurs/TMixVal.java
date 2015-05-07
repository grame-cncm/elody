/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.lang.texpression.valeurs;

import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TMixExp;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.util.Debug;

public final class TMixVal implements TValue {
	public TValue arg1;
	public TValue  arg2;
	float  duration = -1;
	
	public TMixVal(){}
	
	public TMixVal (TValue arg1,  TValue arg2) { 
		 this.arg1 = arg1; this.arg2 = arg2;
	}
	
    public TValue 	Modify(int index, float value, TOperator op) { 
		Debug.Trace( "Modify", this);
		return new TMixVal(arg1.Modify(index,value,op), arg2.Modify(index,value,op));
	}
	
	public TValue 	Begin(float n) {
		return new TMixVal(arg1.Begin(n),arg2.Begin(n));
	}
		
	public TValue 	Rest(float n) {
		return new TMixVal(arg1.Rest(n),arg2.Rest(n));
	}
	
	public TValue 	Apply(TValue susp) {
		return new TMixVal(arg1.Apply(susp),arg2.Apply(susp)); 
	}	
	
	public  float 	Duration () {
		if (duration < 0) duration = DurationAux(MaxDur, MaxRecur);
		return duration;
	}
	
	public  float 	DurationAux (float limit, int n) {
		if (n <= 0) return 0f;
	
	 	float dur = arg1.DurationAux(limit,n);
	 	
	  	return (dur >= limit) ?  Float.POSITIVE_INFINITY : Math.max(dur, arg2.DurationAux(limit,n));
	}
	
	
	public void 	Accept(TValueVisitor v, int date,  Object arg) { 
		v.Visite(this, date, arg);
	}
	
	
	public  TValue getValArg1() {return arg1;}
	public  TValue getValArg2() {return arg2 ;}
	
	public  TValue Mute() { return new TMixVal(arg1.Mute(),  arg2.Mute());}
	
	public TExp Reify(float dur) { return new TMixExp ( arg1.Reify(dur),arg2.Reify(dur));}
}
