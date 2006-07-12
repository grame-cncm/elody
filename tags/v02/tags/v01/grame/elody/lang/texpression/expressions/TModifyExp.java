package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.operateurs.TAdd;
import grame.elody.lang.texpression.operateurs.TMult;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.lang.texpression.operateurs.TSet;
import grame.elody.lang.texpression.operateurs.TSub;
import grame.elody.lang.texpression.suspensions.TEvalSusp;
import grame.elody.lang.texpression.suspensions.TModifySusp;
import grame.elody.lang.texpression.valeurs.TValue;

import java.awt.Color;
import java.io.IOException;

public final class TModifyExp extends TExp {
	public TExp arg1;
	public int index;
	public float value;
	public TOperator op;
	transient TValue cache = null;
	
	

	public TModifyExp (TExp arg1, int index,float value, TOperator op) {
		this.arg1 = arg1; this.value = value; 
		this.index = index; this.op = op; 
	}
 	
 	//public TValue 	Eval (TEnv env) { return  arg1.Eval(env).Modify(index,value, op);}
 	
 	/*
 	public TValue 	Eval (TEnv env) { 
 		if (closed) {
 			//System.out.println("CacheModify");
 			if (cache ==  null) cache = arg1.Eval(env).Modify(index,value, op);
 			return  cache;
 		}else {
 			return arg1.Eval(env).Modify(index,value, op);
 		}
 	}
 	*/
 	
 	public TValue 	Eval (TEnv env) { 
 		return (cache != null) ? cache :arg1.Eval(env).Modify(index,value, op);
 	}
	
	public TValue 	Suspend( TEnv env){ return new TModifySusp(new TEvalSusp (arg1,env) ,index, value,op);} 
 	
	public boolean equals(Object obj) {
		return (this == obj) ||  ((obj instanceof TModifyExp) 
			&& index == ((TModifyExp)obj).index
			&& value == ((TModifyExp)obj).value
			&& op == ((TModifyExp)obj).op
			&& arg1.equals(((TModifyExp)obj).arg1));
	}
	
	public int hashCode() {
		if (hashcode == 0)  hashcode = index * (int)value + arg1.hashCode() ;
		return  hashcode;
	}
	
	
	//  restaure les opérateur (qui sont des objects static) 
	
	private void readObject(java.io.ObjectInputStream stream)
     throws IOException, ClassNotFoundException {
     	 stream.defaultReadObject();
     	 switch (index) {
			case TExp.TRSP:
				op = TAdd.op;
				break;
			case TExp.ATTN:
				op = TSub.op;
				break;
			case TExp.INST:
				op = TSet.op;
				break;
			case TExp.XPND:
				op = TMult.op;
				break;
		}
     }
    

 	TExp  createNew(TExp a1,TExp a2) { return new TModifyExp(a1,index, value,op);}
	
	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public TExp convertModifyExp () {return  this;} 	
	
	public  TExp getArg1() {return arg1;}
	
	public  TExp getArg2() {
		TExp res = TNullExp.instance;
		switch (index) {
			case TExp.TRSP:
				res = new TEvent(TExp.SOUND,Color.black,value,0,0,1);
				break;
			case TExp.ATTN:
				res =  new TEvent(TExp.SOUND,Color.black,0,value,0,1);
				break;
			case TExp.INST:
				res =  new TEvent(TExp.SOUND,Color.black,0,0,value,1);
				break;
			case TExp.XPND:
				res =  new TEvent(TExp.SOUND,Color.black,0,0,0,value);
				break;
		}
		return res;
	}
	
	public void setCache(TValue value){ cache = value;}
	public TValue getCache(){return cache;}
}
