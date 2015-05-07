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
import grame.elody.util.Debug;

public final class TIdent extends TExp {
 	public TExp arg = null;
 	
 	
 	public TIdent (TExp arg) {this.arg = arg; }
   	
 	public TValue Eval (TEnv env) {
 		Debug.Trace( "Eval", this);
 		TEnv cur = env;
 		
 		if (cur == null || globalEnv == null)
 		{
 			System.out.println("Error: Eval ident " + this+ " no env");
 			return null;
 		}
 		
 		globalEnv.setIdent(this);
 		
 		while ((cur != null) && (this != cur.getIdent())) {cur = cur.getNext();}
 		
 		//while (this != cur.ident) {cur = cur.next;}
 		
 		if (cur != null){ 
 			return cur.getSusp();  // A FINIR
 		}else {
 			System.out.println("Error: Eval ident " + this + " no value");
 			System.out.println(arg);
 			new StackOverflowError().printStackTrace(); 
 			return null;
 		}
 	}
 		
 	TValue getSusp (TEnv env) { 
 		TEnv cur = env;
 		
 		globalEnv.setIdent(this);
 		
 		while ((cur != null) && (this != cur.getIdent())) {cur = cur.getNext();}
 		//while (this != cur.ident) {cur = cur.next;}
 		//return cur.susp;
 		
 		if (cur != null){ 
 			return cur.getSusp();  // A FINIR
 		}else {
 			System.out.println("Error: Eval ident " + this + " no value");
 			System.out.println(arg);
 			new StackOverflowError().printStackTrace(); 
 			return null;
 		}
 	}
 		
 	public TValue Suspend( TEnv env){ return getSusp(env);} // A VERIFIER
 	
 	public  TExp Replace(TExp e, TIdent id) {
 		Debug.Trace( "Replace", this);
 		return this;
 	}  
 	
   	
  	public TExp Rebuild(TExp e , TIdent id) { 
 		Debug.Trace( "Rebuild", this);
 		//return (this.equals(id) ? e : this);
 		return ((this == id) ? e : this);  //egalite de pointeur !!
  	}
 
 	
 	/*******************************************************************************************
	 ATTENTION : ne pas implementer la fonction hashCode:
	 si hashCode est implementée alors des objets égaux (au sens de la méthode "equals")
	 seront considérés comme equivalents dans une Hashtable, CE QU'IL NE FAUT PAS POUR LES IDENT.
	 (car des ident egaux seraient restaurés comme des objets egaux au sens pointeur!! ==> problemes)
	
	/*******************************************************************************************/
		
	 public boolean equals(Object obj) { 
	 	return (this == obj) || ((obj instanceof TIdent) 
			&& ((arg.unNameExp()).equals(((TIdent)obj).arg.unNameExp())));
	}
	
	public Object Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() { return arg;}
	
	public TExp unNameExp(){ return this;}
}
