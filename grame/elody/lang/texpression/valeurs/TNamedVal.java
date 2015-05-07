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
import grame.elody.lang.texpression.expressions.TNamedExp;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.util.Debug;

public final class TNamedVal implements TValue {
	public TValue arg1;
	public String name;
	
	public TNamedVal (TValue arg1, String name) {
		this.arg1 = arg1; 
		this.name = name; 
	}
	
	
    public TValue 	Modify(int index, float value, TOperator op) { 
		Debug.Trace( "Modify", this);
		return arg1.Modify(index, value, op);
	}
		
	public TValue Begin(float n) { return arg1.Begin(n);}
	public TValue Rest(float n) { return  arg1.Rest(n); }
	public TValue Apply(TValue susp) {  return   arg1.Apply(susp);}

	
	public float Duration () { return arg1.Duration(); }
	public float DurationAux (float limit, int n) { return arg1.DurationAux(limit,n);}
	
	public void Accept(TValueVisitor v, int date,  Object arg) { arg1.Accept(v,date,arg); }
	public TValue getValArg1() {return arg1.getValArg1();}
	public TValue getValArg2() {return arg1.getValArg2(); }
	
	public TValue Mute() {  return   arg1.Mute();}
	
	public TExp Reify(float dur) { return new TNamedExp (arg1.Reify(dur),name);}
}
