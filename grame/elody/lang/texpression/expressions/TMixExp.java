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
import grame.elody.lang.texpression.valeurs.TMixVal;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

public final class TMixExp extends TExp {
	public TExp arg1;
	public TExp arg2;
	transient TValue cache = null;
		
 	public TMixExp (TExp arg1, TExp arg2) {this.arg1 = arg1; this.arg2 = arg2;}
  	
 	/*
 	public TValue 	Eval (TEnv env) { 
 		Debug.Trace( "Eval", this); 
  		return new TMixVal(arg1.Eval(env),arg2.Eval(env));
  		//return new TMixVal(new TEvalSusp(arg1,env), new TEvalSusp(arg2,env));
 	}
 	*/
 	/*
 	public TValue 	Eval (TEnv env) { 
 		if (closed) {
 			//System.out.println("CacheMix");
 			if (cache ==  null) cache = new TMixVal(arg1.Eval(env),arg2.Eval(env));
 			return  cache;
 		}else {
 			return  new TMixVal(arg1.Eval(env),arg2.Eval(env));
 		}
 	}
 	*/
 	
 	public TValue 	Eval (TEnv env) { 
 		Debug.Trace( "Eval", this); 
  		return (cache != null) ? cache :  new TMixVal(arg1.Eval(env),arg2.Eval(env));
  	}
 	
 	public boolean equals(Object obj) {
 		return (this == obj) || ((obj instanceof TMixExp) 
			&& arg1.equals(((TMixExp)obj).arg1)
			&& arg2.equals(((TMixExp)obj).arg2));
	}
	
	public int hashCode() {
		if (hashcode == 0)   hashcode = arg1.hashCode() + arg2.hashCode();
		return  hashcode;
	}

 	TExp  createNew(TExp a1,TExp a2) {return new TMixExp(a1,a2);}
 	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	
	public  TExp getArg1() {return arg1;}
	public  TExp getArg2() {return arg2;}
	
	public TExp convertMixExp () {return  this;}
	
	public void setCache(TValue value){ cache = value;}
	public TValue getCache(){return cache;}
}
