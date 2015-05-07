/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.valeurs.TValue;

import java.util.Vector;


public final class TArrayExp extends TExp {
	public Vector<TExp> arg1;
	
 	public TArrayExp ( Vector<TExp> arg1) { this.arg1 = arg1; }
 	
 	
 	public TValue 	Eval(TEnv env) { 
 		return arg1.firstElement().Eval(env);
	}
	
 	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TArrayExp) 
			&& arg1.equals(((TArrayExp)obj).arg1));
	}
	
	public int hashCode()  {
		if (hashcode == 0)  hashcode = arg1.hashCode();
		return  hashcode;
	}

 	TExp  createNew(TExp a1,TExp a2) {return TNullExp.instance;}
 	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() {return TNullExp.instance;}
	public  Vector<TExp> getArray() {return arg1;}
}
