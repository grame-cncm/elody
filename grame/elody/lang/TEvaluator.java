/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.lang;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.valeurs.TErrorVal;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.lang.texpression.valeurs.TValueVisitor;

/*******************************************************************************************
*
*	 TEvaluator (classe abstraite) donne un accès à l'évaluateur 
*
*******************************************************************************************/

public abstract class TEvaluator {
	 static public TEvaluator gEvaluator = new TEvaluator1();   // Evaluateur redéfinissable

	 public abstract  void Render (TExp exp, TValueVisitor visitor, int date, Object arg);
	 public abstract  void Render1 (TExp exp, TValueVisitor visitor, int date, Object arg);
	
	 public abstract  TValue Eval (TExp exp);
	 public abstract  TValue Eval (TExp exp, TEnv env); 
	 public abstract  TExp  Reify (TValue val, float dur);
	 public abstract  float Duration (TExp exp);
}

/*******************************************************************************************
*
*	 TEvaluator1 (classe) : implementation concrète
*
*******************************************************************************************/

final class TEvaluator1 extends TEvaluator{

	static long memoryLimit = 50000;
	static int MaxCall  	= 50;
	int countCall 			= 0;
	
	
	public TEvaluator1() {}
	
	 final void printMemory () {
		Runtime runtime = Runtime.getRuntime();
		System.out.println ("Total memory " + runtime.totalMemory());
		System.out.println ("Free memory " + runtime.freeMemory());
	
		runtime.runFinalization ();
		runtime.gc();
		System.out.println ("Free memory " + runtime.freeMemory());
		System.out.println ("Used memory " + ( runtime.totalMemory()- runtime.freeMemory()));
	}
	
	
	final boolean checkMemory () {
		Runtime runtime = Runtime.getRuntime();
		boolean res = runtime.freeMemory() < memoryLimit;
		return res;
	}
	
	final boolean errorMemory() {
		if (countCall++ >= MaxCall){
			Runtime runtime = Runtime.getRuntime();
			countCall = 0;
			if (runtime.freeMemory() < memoryLimit) runtime.gc();
			return (runtime.freeMemory() < memoryLimit);
		}else
			return false;
	}
		
	TValue evalExp(TExp exp) { 
		TValue val = exp.getCache();
		return (val == null) ? exp.Eval(TExp.globalEnv) : val;
	}
	
	TValue evalCacheExp(TExp exp) {
	
		TValue val = exp.getCache();
		
		if (val == null) {
			//System.out.println("Eval");
			val = exp.Eval(TExp.globalEnv);
			exp.setCache(val);
		}else {
			////System.out.println("Cache");
		}
		
		Runtime runtime = Runtime.getRuntime();
		////System.out.println("Total Mem " + runtime.freeMemory());
		if (runtime.freeMemory() < 50000) {
			//System.out.println("FREE MEM");
			exp.setCache(null);
		}
		return val;
	}
	
	
	public final  void Render (TExp exp, TValueVisitor visitor, int date, Object arg) {
		try {
			evalCacheExp(exp).Accept(visitor,date,arg);
		}catch (Throwable e) {
			System.out.println(" Catched Error " + e);
		}
	}
	
	//-----------------------------------------------------------------------------------
	// Utilisé par le visiteur Graphique qui lance des exceptions java.lang.ThreadDeath 
	// qui ne doivent pas être catchés
	//-----------------------------------------------------------------------------------
	
	
	public final  void Render1 (TExp exp, TValueVisitor visitor, int date, Object arg) {
		evalCacheExp(exp).Accept(visitor,date,arg);
	}
	
	public final  float Duration (TExp exp) {
		try {
			TValue val = exp.getCache();
			exp.setCache(null);
			float dur = exp.Eval(TExp.globalEnv).Duration();
			exp.setCache(val);
			return dur;
		}catch ( Throwable e) {
			System.out.println("Catched Error during Duration " + e);
			e.printStackTrace();
			return Float.POSITIVE_INFINITY;
		}
	}
	
	public final  TValue Eval (TExp exp) {
		try {
			return evalCacheExp(exp);
		}catch (Throwable e) {
			System.out.println("Catched Error during Eval " + e);
			e.printStackTrace();
			return new TErrorVal(exp);
		}
	}

	
	public final  TValue Eval (TExp exp, TEnv env) {
		try {
			return evalExp(exp);
		}catch (Throwable e) {
			System.out.println("Catched Error during Eval " + e);
			e.printStackTrace();
			return new TErrorVal(exp);
		}
	}
	
	public final  TExp  Reify (TValue val, float dur) {
		try {
			return val.Reify(dur);
		}catch (Throwable e) {
			System.out.println("Catched Error during Reify " + e);
			e.printStackTrace();
			return null;
		}
	}
}