package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.valeurs.TValue;

public final class TApplExp extends TExp {
	public TExp fun;
 	public TExp arg;
 	transient TValue cache = null;
 	
 	
 	public TApplExp (TExp fun, TExp arg) { this.fun = fun; this.arg = arg;}
 	
 	
 	/*
 	public TValue 	Eval (TEnv env) { 
 		Debug.Trace( "Eval", this);
 		return fun.Eval(env).Apply(arg.Suspend(env));
 	}
 	 */
 	
 	/*
 	public TValue 	Eval (TEnv env) { 
 		if (closed) {
 			//System.out.println("CacheAppl");
 			if (cache ==  null) cache =  fun.Eval(env).Apply(arg.Suspend(env));
 			return  cache;
 		}else {
 			return fun.Eval(env).Apply(arg.Suspend(env));
 		}
 	}
 	 */		
 	 
 	public TValue 	Eval (TEnv env) { 
 		return (cache != null) ? cache : fun.Eval(env).Apply(arg.Suspend(env));
 	} 
 	 
  	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TApplExp) 
			&& fun.equals(((TApplExp)obj).fun)
			&& arg.equals(((TApplExp)obj).arg));
	}
		
	public int hashCode() {
		if (hashcode == 0)  hashcode = fun.hashCode() + arg.hashCode();
		return  hashcode;
	}
	
	TExp  createNew(TExp a1,TExp a2) {return new TApplExp(a1,a2);}
 	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() {return fun;}
	public  TExp getArg2() {return arg;}
	
	public TExp convertApplExp () {return  this;}
	
	public void setCache(TValue value){ cache = value;}
	public TValue getCache(){return cache;}
}
