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

import grame.elody.file.text.saver.TTextExpWriter;
import grame.elody.lang.texpression.TStepApplVisitor;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.io.StringWriter;

/*******************************************************************************************
*
*	 TExpRenderer (classe) : objet global pour obtenir la représentation textuelle d'une expression
* 
*******************************************************************************************/

public class TExpRenderer {
	public static TExpRenderer gExpRenderer = new TExpRenderer();

	public String getText(TExp exp) {
		StringWriter output = new StringWriter();
		exp.Accept(new TTextExpWriter(output), null);
		return output.toString();
	}
	
	public Color getColor(TExp exp) 		
		{ return (Color) exp.Accept(new TCalcColorVisitor(), null);}
		
	public TExp oneStepEval(TExp exp) 	
		{ return (TExp) exp.Accept(new TStepApplVisitor(), null);}
}
