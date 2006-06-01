package grame.elody.lang.texpression.suspensions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

public final class TEvalSusp extends TSusp {
	public TExp exp;
 	public TEnv env;
 	
 		
	public TEvalSusp(){}
 	
 	public TEvalSusp( TExp exp , TEnv env) {this.exp = exp; this.env = env;}
  	
	public TValue Force() { 
		Debug.Trace( "Force", this);
		TValue res = exp.Eval(env);
		//exp  = null;
  		//env = null;
  		return res;
   	}
   
     
   	public TExp Reify(float dur) {
		if (dur > 0) {
		 	return exp.Eval(env).Reify(dur);
		}else {
			//System.out.println ("End Reify TEvalSusp");
			TEnv cur = env;
   	
	 		while (cur != null) {
	 			if (TExp.globalEnv != cur) exp = exp.RebuildBody(cur.getSusp().Reify(dur),cur.getIdent());
	 			cur = cur.getNext();
	 		}
	 		return exp;
		}
	}
}
