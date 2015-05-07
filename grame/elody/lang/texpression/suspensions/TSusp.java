/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.lang.texpression.suspensions;

import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.lang.texpression.valeurs.TValueVisitor;

public abstract class TSusp implements TValue {

	TValue val = null;
	final static int MAX = 10000;
	
	abstract public TValue Force();
	
	final TValue getValue() {if (val == null) val = Force(); return val;}
	final public  TValue Begin(float n) { return getValue().Begin(n);}
	final public  TValue Rest(float n) { return getValue().Rest(n);}
	final public  TValue Apply(TValue susp) { return getValue().Apply(susp);}
	final public  float Duration (){ return getValue().Duration();}
	final public  float DurationAux (float limit, int n){ return getValue().DurationAux(limit,n); }
	
	/*
	final public  void  Accept(TValueVisitor v,int date, Object arg){
		getValue().Accept(v, date, arg);
	}
	*/
	
	//------------------------------------------------------------------------------
	// Updates des suspension seulement si la date d'evaluation est < MAX, pour garder 
	// seulement une partie des valeurs (voir TEvaluator)
	//------------------------------------------------------------------------------
	
	final public void 	Accept(TValueVisitor v,int date, Object arg){
		if (date < MAX){
			getValue().Accept(v, date, arg);
		}else {
			Force().Accept(v, date, arg);
		}
	}
		
	final public TValue Modify(int index,  float value, TOperator op){
		return getValue().Modify(index, value, op);
	}
	
	final public  TValue Mute() { return getValue().Mute();}
	final public  TValue getValArg1() { return getValue().getValArg1();}
	final public  TValue getValArg2() { return getValue().getValArg2();}
	
	abstract public TExp Reify(float dur);
}
