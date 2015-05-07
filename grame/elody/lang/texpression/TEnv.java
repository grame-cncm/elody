/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.lang.texpression;

import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.valeurs.TValue;

public final class TEnv {
	TExp ident;
 	TValue susp;
 	TEnv next;
 	
 	public TEnv() {}
 	public TEnv(TExp ident , TValue susp, TEnv next) 
 	{this.ident = ident; this.susp = susp; this.next =  next;} 
 	
 	public TEnv Bind(TExp ident, TValue susp) {
 		return new TEnv(ident, susp, this);
 	}
 	
 	public  TExp getIdent() {return ident;}
	public  TValue getSusp() {return susp;}
	public  TEnv getNext() {return next;}
	public void setIdent(TExp ident) { this.ident = ident; }
}
