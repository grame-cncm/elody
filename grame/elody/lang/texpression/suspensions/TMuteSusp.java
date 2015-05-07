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
import grame.elody.lang.texpression.expressions.TMuteExp;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

public final class TMuteSusp extends TSusp {
	public TValue arg1;
 	
 	public TMuteSusp (TValue arg1) { this.arg1 = arg1;}
 	
	public TValue 	Force() { 
		Debug.Trace( "Force", this);
		TValue res = arg1.Mute();
		//arg1 = null;
		return res;
	}
	
	public TExp Reify(float dur) {
		if (dur > 0) {
		 	return arg1.Mute().Reify(dur);
		}else {
			//System.out.println ("End Reify TRestSusp");
			return new TMuteExp (arg1.Reify(dur));
		}
	}
}
