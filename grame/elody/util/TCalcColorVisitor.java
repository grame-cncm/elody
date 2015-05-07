/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.util;

import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TAbstrExp;
import grame.elody.lang.texpression.expressions.TApplExp;
import grame.elody.lang.texpression.expressions.TArrayExp;
import grame.elody.lang.texpression.expressions.TBeginExp;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExpVisitor;
import grame.elody.lang.texpression.expressions.TExpandExp;
import grame.elody.lang.texpression.expressions.TIdent;
import grame.elody.lang.texpression.expressions.TMixExp;
import grame.elody.lang.texpression.expressions.TModifyExp;
import grame.elody.lang.texpression.expressions.TMuteExp;
import grame.elody.lang.texpression.expressions.TNamedExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.lang.texpression.expressions.TRestExp;
import grame.elody.lang.texpression.expressions.TSequenceExp;
import grame.elody.lang.texpression.expressions.TUrlExp;
import grame.elody.lang.texpression.expressions.TYAbstrExp;

import java.awt.Color;



/*******************************************************************************************
*
*	 TCalcColorVisitor (classe) : object global pour obtenir la couleur d'une expression
* 
*******************************************************************************************/

public final class TCalcColorVisitor extends TExpVisitor {
	Object res ;
	
	public Object Visite( TEvent s ,Object arg) {
		if (arg == null) {
			return s.getColor();
		}else{
			return (s.getColor().equals((Color)arg)) ? arg : null;
		}
	}
	public Object Visite( TApplExp s ,Object arg){
		res  = s.getArg1().Accept(this,arg);
		return (res == null) ? res : s.getArg2().Accept(this,res);
	}
	
	public Object Visite( TAbstrExp s ,Object arg){return s.getArg2().Accept(this,arg);}
	
	public Object Visite( TYAbstrExp s ,Object arg){return s.getArg2().Accept(this,arg);}
	
	public Object Visite( TNamedExp s ,Object arg) {return s.getArg1().Accept(this,arg);}
	
	public Object Visite( TSequenceExp s ,Object arg){
		res  = s.getArg1().Accept(this,arg);
		return (res == null) ? res : s.getArg2().Accept(this,res);
	}
	public Object Visite( TMixExp s ,Object arg){
		res  = s.getArg1().Accept(this,arg);
		return (res == null) ? res : s.getArg2().Accept(this,res);
	}
	public Object Visite( TModifyExp s ,Object arg){return s.getArg1().Accept(this,arg);}
	
	public Object Visite( TExpandExp s ,Object arg){
		res  = s.getArg1().Accept(this,arg);
		return (res == null) ? res : s.getArg2().Accept(this,res);
	}
	public Object Visite( TRestExp s ,Object arg){
		Object res  = s.getArg1().Accept(this,arg);
		return (res == null) ? res : s.getArg2().Accept(this,res);
	}
	public Object Visite( TBeginExp s ,Object arg){
		res  = s.getArg1().Accept(this,arg);
		return (res == null) ? res : s.getArg2().Accept(this,res);
	}
	
	public Object Visite( TIdent s ,Object arg){return s.getArg1().Accept(this,arg);}
	public Object Visite( TUrlExp s ,Object arg){return s.getArg1().Accept(this,arg);}
	public Object Visite( TNullExp s ,Object arg){return null;}
	
	public Object Visite( TMuteExp s ,Object arg){return s.getArg1().Accept(this,arg);}
	public Object Visite( TArrayExp s ,Object arg){return null;}
	
	public Object Visite( TInput s ,Object arg){return Color.gray;}
}
