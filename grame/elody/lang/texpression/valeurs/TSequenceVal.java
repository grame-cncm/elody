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
import grame.elody.lang.texpression.expressions.TSequenceExp;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.lang.texpression.suspensions.TApplSusp;
import grame.elody.lang.texpression.suspensions.TBeginSusp;
import grame.elody.lang.texpression.suspensions.TModifySusp;
import grame.elody.lang.texpression.suspensions.TMuteSusp;
import grame.elody.util.Debug;

public final class TSequenceVal implements TValue {
	public TValue arg1;
	public TValue arg2;
	float  duration = -1;
	
	public TSequenceVal(){}
	
	public TSequenceVal (TValue arg1,  TValue arg2) { 
		 this.arg1 = arg1; this.arg2 = arg2;
	}
	 	
   	public TValue 	Modify(int index, float value, TOperator op) { 
		Debug.Trace( "Modify", this);
		return new TSequenceVal(arg1.Modify(index,value,op), new TModifySusp(arg2,index,value,op));
	}
	
	public TValue 	Begin(float n) {
		Debug.Trace( "Begin", this);
		
		float d = arg1.Duration();
	
		if (n < d) {
			return arg1.Begin(n);
		}else if (n == d){
			return arg1;
		}else {
			return new TSequenceVal (arg1, new TBeginSusp(arg2, n - d));
		}
	}
		
	public TValue 	Rest(float n) {
		Debug.Trace( "Rest", this);
		
		float d = arg1.Duration();
	
		if (n < d) {
			return new TSequenceVal(arg1.Rest(n), arg2);
		}else if (n == d){
			return arg2;
		}else {
			return arg2.Rest(n - d);
		}
	}
	
	/*
	public TValue 	Apply(TValue susp) {
		Debug.Trace("Apply", this);
		float d = arg1.Duration();
		return new TSequenceVal(arg1.Apply(new TBeginSusp(susp, d))
			,new TApplSusp(arg2, new TRestSusp(susp, d)));
	}	
	*/
	
	public TValue 	Apply(TValue susp) {
		Debug.Trace("Apply", this);
		float d = arg1.Duration();
		return new TSequenceVal(arg1.Apply(susp.Begin(d)),new TApplSusp(arg2, susp.Rest(d)));
	}	
	
		
	public  float 	Duration () {
		if (duration < 0) duration = DurationAux(MaxDur, MaxRecur);
		return duration;
	}
	
	public  float 	DurationAux (float limit, int n) {
	
		if (n <= 0) return 0f;
		
		float v1 = arg1.DurationAux(limit,n);
		
		if (v1 >= limit) { 
			return  Float.POSITIVE_INFINITY;
		}else if ((v1 < 0.5) && ( v1 > 0)) {
			return v1 + arg2.DurationAux(limit, n - 1);
		}else {
			return v1 + arg2.DurationAux(limit - v1, n);
		}
	}

		
	
	public void 	Accept(TValueVisitor v, int date, Object arg) {
		v.Visite(this, date, arg);
	}
	
	
	public  TValue getValArg1() {return arg1;}
	public  TValue getValArg2() {return arg2;}
	
	public  TValue Mute() { return new TSequenceVal(arg1.Mute(),  new TMuteSusp(arg2));}

	
	public TExp Reify(float dur) { 
		return new TSequenceExp ( arg1.Reify(dur),arg2.Reify(dur - arg1.Duration()));
	}
}
