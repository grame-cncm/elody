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
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.lang.texpression.valeurs.TValueVisitor;

public final class TNullExp extends TExp implements TValue{
 
 	public static TNullExp instance = new TNullExp ();
 	public static Object defaultRes ; 
	
 	public TNullExp () {}
 	
 	public TValue 	Suspend( TEnv env){ System.out.println ("Error : Suspend NullExp"); return null;}
	
	public TValue 	Eval(TEnv env){  return this;}
	
	public TExp   	Replace(TExp e, TIdent id) {  return this;}
	public TExp   	Rebuild(TExp e, TIdent id) {  return this;}
 	
 	public Object 	Accept(TExpVisitor v, Object arg)  {return v.Visite(this,arg);}
 	
 	public  TValue 	Begin(float n) {System.out.println ("Error : Begin NullExp");  return null;}
	public  TValue 	Rest(float n){ System.out.println ("Error : Rest NullExp");  return null;}
	public  TValue 	Apply(TValue susp){System.out.println ("Error : Apply NullExp");  return null;}
	
	public  float 	Duration (){ System.out.println ("Error : Duration NullExp");  return 0f;}
	public  float 	DurationAux (float limit, int n){ System.out.println ("Error : DurationAux NullExp");  return 0f;}
	
	public void 	Accept(TValueVisitor v,int date,Object arg){
		//System.out.println ("Visite NullExp");
	}
	
	
	public TValue 	Modify(int index,  float value, TOperator op){
		System.out.println ("Error : Replace NullExp");  return null;
	}
	
	public  TValue getValArg1() {System.out.println("Error getArg1 of TNullExp"); return null ;}
	public  TValue getValArg2() {System.out.println("Error getArg2 of TNullExp"); return null ;}
	
	public TExp unNameExp(){return this;}
	
	
 	public  TValue Mute() { System.out.println ("Error : Mute NullExp");  return null;}
 	
 	public TExp Reify(float dur) { System.out.println ("Error : Reify NullExp");  return null;}
}
