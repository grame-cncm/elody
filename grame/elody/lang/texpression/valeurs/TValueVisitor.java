/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.lang.texpression.valeurs;

import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;

/*******************************************************************************************
*
*	 TValueVisitor (interface) : à implémenter dans des visiteur de valeur concrèt
*
*******************************************************************************************/

public interface TValueVisitor {
	public void Visite(TEvent val,int date, Object arg);
	public void Visite(TSequenceVal val,int date,Object arg);
	public void Visite(TMixVal val,int date,Object arg);
	public void Visite(TClosure val,int date,Object arg);
	public void Visite(TApplVal val,int date,Object arg);
	public void Visite(TErrorVal val,int date,Object arg);
	public void Visite(TNullExp val,int date,Object arg);
	public void Visite(TNamedVal val,int date,Object arg);
	public void Visite(TInput val,int date, Object arg);
	public void renderExp(TExp exp);
}
