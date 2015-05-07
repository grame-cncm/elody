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

public final class TMuteExp extends TExp {
	public TExp arg1;
	transient TValue cache = null;
	
	
 	public TMuteExp ( TExp arg1) { this.arg1 = arg1;}
 	
	/*
  	public TValue 	Eval(TEnv env) { 
  		return arg1.Eval(env).Mute() ; //Mute serait une opération sur les valeurs
 	}
 	*/
 	
 	/*
 	public TValue 	Eval (TEnv env) { 
 		if (closed) {
 			//System.out.println("CacheMute");
 			if (cache ==  null) cache = arg1.Eval(env).Mute() ; //Mute serait une opération sur les valeurs
 			return  cache;
 		}else {
 			return arg1.Eval(env).Mute() ; //Mute serait une opération sur les valeurs
 		}
 	}
 	*/
 	
 	 public TValue 	Eval(TEnv env) { 
		return (cache != null) ? cache :arg1.Eval(env).Mute() ; //Mute serait une opération sur les valeurs
	}
	
	
 	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TMuteExp) 
			&& arg1.equals(((TMuteExp)obj).arg1));
	}
	
	public int hashCode()  {
		if (hashcode == 0)  hashcode = arg1.hashCode();
		return  hashcode;
	}

 	TExp  createNew(TExp a1,TExp a2) {return  new TMuteExp(a1);}
 	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() {return arg1;}
	
	public void setCache(TValue value){ cache = value;}
	public TValue getCache(){return cache;}
}
