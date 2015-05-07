/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.midifile;

import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.lang.texpression.valeurs.TApplVal;
import grame.elody.lang.texpression.valeurs.TClosure;
import grame.elody.lang.texpression.valeurs.TErrorVal;
import grame.elody.lang.texpression.valeurs.TMixVal;
import grame.elody.lang.texpression.valeurs.TNamedVal;
import grame.elody.lang.texpression.valeurs.TSequenceVal;
import grame.elody.lang.texpression.valeurs.TValueVisitor;

/*******************************************************************************************
*
*	 TBValueVisitor (classe) : le parser MIDIFile
* 
*******************************************************************************************/

public abstract class TBValueVisitor implements TValueVisitor {
	public boolean contVisite(int date) {return true;} 
	
	public void Visite(TEvent val,int date, Object arg) {}

	public void Visite(TSequenceVal val,int date,Object arg) {
		val.getValArg1().Accept(this,date, arg);
		date += (int) val.getValArg1().Duration();
		if (contVisite(date)) val.getValArg2().Accept(this , date,arg);
	}
	
	public void Visite(TMixVal val,int date,Object arg) {
		val.getValArg1().Accept(this,date,arg);
 		val.getValArg2().Accept(this,date,arg);
	}
	
	public void Visite(TClosure val,int date,Object arg) {}
	public void Visite(TApplVal val,int date,Object arg) {}
	public void Visite(TErrorVal val,int date,Object arg){}
	public void Visite(TNullExp val,int date,Object arg){}
	public void Visite(TNamedVal val,int date,Object arg){ val.Accept(this, date,arg);}
	
	public void Visite(TInput val,int date,Object arg){ }

	
	public void renderExp(TExp exp) {}
}
