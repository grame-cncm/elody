package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.valeurs.TValue;

public final class TRestExp extends TExp {
	public TExp arg1;
	public TExp arg2;
	transient TValue cache = null;
 	
 	public TRestExp ( TExp arg1, TExp arg2) { this.arg1 = arg1; this.arg2 = arg2;}
 	
 	/*
 	public TValue 	Eval(TEnv env) { 
		return arg1.Eval(env).Rest(arg2.Eval(env).Duration());
	}
	*/
	
	/*
	public TValue 	Eval (TEnv env) { 
 		if (env == globalEnv) {
 			//System.out.println("CacheRest");
 			if (cache ==  null) cache =  arg1.Eval(env).Rest(arg2.Eval(env).Duration());
 			return  cache;
 		}else {
 			return arg1.Eval(env).Rest(arg2.Eval(env).Duration());
 		}
 	}
	*/
	
	 public TValue 	Eval(TEnv env) { 
		return (cache != null) ? cache : arg1.Eval(env).Rest(arg2.Eval(env).Duration());
	}
	
	
	 public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TRestExp) 
			&& arg1.equals(((TRestExp)obj).arg1)
			&& arg2.equals(((TRestExp)obj).arg2)) ;
	}
	
	public int hashCode() { 
		if (hashcode == 0 ) hashcode = arg2.hashCode() + arg1.hashCode();
		return hashcode;
	}

	TExp  createNew(TExp a1,TExp a2) {return new TRestExp(a1,a2);}
 	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() {return arg1;}
	public  TExp getArg2() {return arg2;}
	
	 public TExp convertRestExp () 	{return this;} 	
	 
	 public void setCache(TValue value){ cache = value;}
	 public TValue getCache(){return cache;}
}
