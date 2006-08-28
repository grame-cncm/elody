package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.suspensions.TEvalSusp;
import grame.elody.lang.texpression.valeurs.TSequenceVal;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

public final class TSequenceExp extends TExp{
	public TExp arg1;
	public TExp arg2;
	transient TValue cache = null;
	
 	
 	public TSequenceExp (TExp arg1, TExp arg2) {this.arg1 = arg1; this.arg2 = arg2; }
  	
 	/*
 	public TValue 	Eval (TEnv env) { 
 		Debug.Trace( "Eval", this); 
 		return new TSequenceVal(arg1.Eval(env), new TEvalSusp(arg2,env));
  	}
  	*/
  	
  	/*
  	public TValue 	Eval (TEnv env) { 
 		if (closed) {
 			//System.out.println("CacheSeq");
 			if (cache ==  null) cache = new TSequenceVal(arg1.Eval(env), new TEvalSusp(arg2,env));
 			return  cache;
 		}else {
 			return  new TSequenceVal(arg1.Eval(env), new TEvalSusp(arg2,env));
 		}
 	}
    */	
    
   public TValue 	Eval (TEnv env) { 
 		Debug.Trace( "Eval", this); 
 		return (cache != null) ? cache :  new TSequenceVal(arg1.Eval(env), new TEvalSusp(arg2,env));
   }
  	
  	
 	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TSequenceExp) 
			&& arg1.equals(((TSequenceExp)obj).arg1)
			&& arg2.equals(((TSequenceExp)obj).arg2));
	}
	
	public int hashCode() {
		if (hashcode == 0)   hashcode = arg1.hashCode() + arg2.hashCode();
		return  hashcode;
	}
 	
 	TExp  createNew(TExp a1,TExp a2) {return new TSequenceExp(a1,a2);}
 	
	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() {return arg1;}
	public  TExp getArg2() {return arg2;}
	
	public TExp convertSeqExp () {return  this;}
	
	public void setCache(TValue value){ cache = value;}
	public TValue getCache(){return cache;}
}
