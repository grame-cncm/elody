package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.valeurs.TNamedVal;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

public final class TNamedExp extends TExp {
	public TExp arg1;
	public String name;
	transient TValue cache = null;

	public TNamedExp (TExp arg1, String name) {
		this.arg1 = arg1; 
		this.name = name; 
	}
	
	public String getName() {return name;}
	
  	//public TValue 	Eval (TEnv env) { return arg1.Eval(env);}
   	
   	
  	public TValue 	Eval (TEnv env) { 
  		return (cache != null) ? cache : new TNamedVal(arg1.Eval(env),name);
   	}
	
	public boolean equals(Object obj) {
		return (this == obj) ||  ((obj instanceof TNamedExp) 
			&& name.equals(((TNamedExp)obj).name)
			&& arg1.equals(((TNamedExp)obj).arg1));
	}
	
	public int hashCode() {
		if (hashcode == 0)  hashcode = arg1.hashCode();
		return  hashcode;
	}
	
	TExp  createNew(TExp a1,TExp a2) { return new TNamedExp(a1,name);}
	
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
  	public TExp convertModifyExp () {return  arg1.convertModifyExp();}	
  	
 	public TExp unNameExp(){return arg1.unNameExp();}
  	
 	//public void setCache(TValue value){arg1.setCache(value);}
  	//public TValue getCache(){return arg1.getCache();}
  	
  	
  	public TExp Replace(TExp e, TIdent id) {  
 		Debug.Trace( "Replace", this); 
 		if (e instanceof TNamedExp) e = e.getArg1(); // denommage en surface
 		TExp res = arg1.Replace(e,id);
 		return (arg1.equals(res)) ? this : res;
 	}
}
