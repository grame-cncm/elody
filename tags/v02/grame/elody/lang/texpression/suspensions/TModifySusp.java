package grame.elody.lang.texpression.suspensions;

import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TModifyExp;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

public final class TModifySusp extends TSusp {
	public TValue susp;
	public int index;
	public float value;
	public TOperator op;
	
	public TModifySusp (TValue susp, int index, float value, TOperator op) {
		this.susp =susp;  this.index = index; this.value = value; this.op = op;
	}
	
	public TValue  Force () { Debug.Trace( "Force", this); 
		TValue res = susp.Modify(index, value, op);
		//susp = null;
		return res;
	}
	
	public TExp Reify(float dur) {
		if (dur > 0) {
		 	return susp.Modify(index, value, op).Reify(dur);
		}else {
			//System.out.println ("End Reify TModifySusp");
			return new TModifyExp (susp.Reify(dur), index , value, op);
		}
	}
}
