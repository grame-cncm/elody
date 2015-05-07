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

public final class TUrlExp extends TExp {
	public TExp arg1;
	public String url;
		
 	public TUrlExp ( TExp arg1, String url) { 
 		this.arg1 = arg1; 
 		this.url = url;
  	}
 	
 	public String getUrl(){ return url;}
	public void setUrl(String url) { this.url = url;}
	
	
 	public TValue 	Eval(TEnv env) { return arg1.Eval(env);}
	
 	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TUrlExp) 
			&& url.equals(((TUrlExp)obj).url)
			&& arg1.equals(((TUrlExp)obj).arg1));
	}
	
	public int hashCode()  {
		if (hashcode == 0)  hashcode = url.hashCode();
		return  hashcode;
	}

 	TExp  createNew(TExp a1,TExp a2) {return  new TUrlExp(a1,url);}
 	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() {return arg1;}
		
	public TExp convertMixExp () 	{return  arg1.convertMixExp();}
  	public TExp convertSeqExp () 	{return  arg1.convertSeqExp();} 	
  	public TExp convertApplExp () 	{return  arg1.convertApplExp();}
  	public TExp convertAbstrExp () 	{return  arg1.convertAbstrExp();}
  	public TExp convertYAbstrExp ()	{return  arg1.convertYAbstrExp();}
  	public TExp convertBeginExp () 	{return  arg1.convertBeginExp();}
  	public TExp convertRestExp () 	{return  arg1.convertRestExp();} 
  	public TExp convertDilateExp () {return  arg1.convertDilateExp();} 
  	
  	public TExp unNameExp(){return arg1.unNameExp();}	
  	
  	public void setCache(TValue value){arg1.setCache(value);}
  	public TValue getCache(){return arg1.getCache();}
}
