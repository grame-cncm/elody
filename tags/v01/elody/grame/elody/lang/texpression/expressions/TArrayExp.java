package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.valeurs.TValue;

import java.util.Vector;


public final class TArrayExp extends TExp {
	public Vector arg1;
	
 	public TArrayExp ( Vector arg1) { this.arg1 = arg1; }
 	
 	
 	public TValue 	Eval(TEnv env) { 
 		return ((TExp)arg1.firstElement()).Eval(env);
	}
	
 	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TArrayExp) 
			&& arg1.equals(((TArrayExp)obj).arg1));
	}
	
	public int hashCode()  {
		if (hashcode == 0)  hashcode = arg1.hashCode();
		return  hashcode;
	}

 	TExp  createNew(TExp a1,TExp a2) {return TNullExp.instance;}
 	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() {return TNullExp.instance;}
	public  Vector getArray() {return arg1;}
}
