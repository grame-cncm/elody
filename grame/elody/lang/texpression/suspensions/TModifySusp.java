/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
