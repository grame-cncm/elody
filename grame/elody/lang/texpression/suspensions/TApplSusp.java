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

import grame.elody.lang.texpression.expressions.TApplExp;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

public final class TApplSusp extends TSusp {
	public TValue fun ;
 	public TValue arg ;
 	
 	public TApplSusp (TValue fun, TValue arg) { this.fun = fun; this.arg = arg;}
 	
	public TValue   Force() {
		Debug.Trace( "Force", this);
		TValue res = fun.Apply(arg);
		//fun = null; arg = null;
		return res;
	}
	
		
	public TExp Reify(float dur) {
		if (dur > 0) {
		 	return fun.Apply(arg).Reify(dur);
		}else {
			//System.out.println ("End Reify TModifySusp");
			return new TApplExp (fun.Reify(dur), arg.Reify(dur));
		}
	}
}
