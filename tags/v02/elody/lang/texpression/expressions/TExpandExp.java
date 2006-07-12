package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.operateurs.TMult;
import grame.elody.lang.texpression.valeurs.TValue;

public final class TExpandExp extends TExp {
	public TExp arg1;
	public TExp arg2;
	transient TValue cache = null;
	

	public TExpandExp (TExp arg1, TExp arg2) {this.arg1 = arg1; this.arg2 = arg2;}
	
	/*
 	public TValue 	Eval (TEnv env) { 
 		TValue v1 = arg1.Eval(env);
 		return  v1.Modify(TExp.DURATION,
 				arg2.Eval(env).Duration()/ v1.Duration(), TMult.op);
 	}
 	*/
 	
 	/*
 	public TValue 	Eval (TEnv env) { 
 		if (closed) {
 			//System.out.println("CacheExpand");
 			if (cache ==  null){
 				TValue v1 = arg1.Eval(env);
 				cache =  v1.Modify(TExp.DURATION,
 				arg2.Eval(env).Duration()/ v1.Duration(), TMult.op);
 			}
 			return  cache;
 		}else {
 			TValue v1 = arg1.Eval(env);
 			return  v1.Modify(TExp.DURATION,
 				arg2.Eval(env).Duration()/ v1.Duration(), TMult.op);
 		}
 	}
	*/
	
	
	public TValue 	Eval (TEnv env) { 
 		if (cache != null) 
 			return cache;
 		else {
 			TValue v1 = arg1.Eval(env);
 			return  v1.Modify(TExp.DURATION,
 				arg2.Eval(env).Duration()/ v1.Duration(), TMult.op);
 		}
 	}
	
	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TExpandExp) 
			&& arg1.equals(((TExpandExp)obj).arg1)
			&& arg2.equals(((TExpandExp)obj).arg2));
	}
	
	public int hashCode() {
		if (hashcode == 0)  hashcode = arg1.hashCode() + arg2.hashCode();
		return  hashcode;
	}
	
	TExp  createNew(TExp a1,TExp a2) {return new TExpandExp(a1,a2);}
  	
	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() {return arg1;}
	public  TExp getArg2() {return arg2;}
	
	public TExp convertDilateExp () {return this;} 	
	
	public void setCache(TValue value){ cache = value;}
	public TValue getCache(){return cache;}
}
