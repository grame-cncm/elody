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

import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.util.Debug;

import java.awt.Color;


public final class TErrorVal implements TValue {
	public TExp arg1;
	
	public TErrorVal (TExp arg1) { 
		 this.arg1 = arg1; 
	}
  	
    public TValue 	Modify(int index, float value, TOperator op) { 
		Debug.Trace( "Modify", this);
		return this;
	}
	
	public TValue 	Begin(float n) { return this;}
	public TValue 	Rest(float n) { return this;}
	public TValue 	Apply(TValue susp) { return this;}
	
	public  float 	Duration () {return 0f;}
	public  float 	DurationAux (float limit,int n) {return 0f;}
	
	public void 	Accept(TValueVisitor v, int date,  Object arg) { v.Visite(this, date, arg);}
	
	public  TValue getValArg1() {return null;}
	public  TValue getValArg2() {return null;}
	
	public  TExp getArg1() {return arg1;}
	
	public  TValue Mute() { return new TEvent(TEvent.SILENCE,Color.black,0f,0f,0f, Duration());}
	
	public TExp Reify(float dur) { System.out.println ("Error : Reify TErrorVal"); return null;}
}
