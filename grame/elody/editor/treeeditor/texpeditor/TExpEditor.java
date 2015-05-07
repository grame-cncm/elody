/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.treeeditor.texpeditor;

import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TAbstrExp;
import grame.elody.lang.texpression.expressions.TApplExp;
import grame.elody.lang.texpression.expressions.TBeginExp;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TExpandExp;
import grame.elody.lang.texpression.expressions.TMixExp;
import grame.elody.lang.texpression.expressions.TModifyExp;
import grame.elody.lang.texpression.expressions.TNamedExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.lang.texpression.expressions.TRestExp;
import grame.elody.lang.texpression.expressions.TSequenceExp;
import grame.elody.lang.texpression.expressions.TYAbstrExp;

import java.awt.Color;

//===========================================================================
//TExpEditor : fournit la mécanique pour "éditer" une TExp particulière
//===========================================================================

public abstract class TExpEditor {
	// l'interface a implementer pour chaque type d'expression
	public abstract String getKindName ();									// le nom de genre
	public abstract TExp modifySubExpression (TExp subexp, int norder);	// expression résultante de la modification d'une sous-expression
	public abstract TExp modifySubString (String substr, int norder);		// expression résultante de 
	public abstract TExp modifySubInt (int subint, int norder);
	public abstract TExp modifySubFloat (float subfloat, int norder);
	public abstract TExp modifySubColor (Color subcolor, int norder);
	public abstract void addSonsTo(TreePanel t);
	
	static public TExpEditor makeEditor(TExp e)
	{ 	
				if (e instanceof TSequenceExp) 	return new TSequenceEditor	( (TSequenceExp) e);
		else 	if (e instanceof TMixExp) 		return new TMixEditor		( (TMixExp) e);
		else 	if (e instanceof TBeginExp) 	return new TBeginEditor		( (TBeginExp) e);
		else 	if (e instanceof TRestExp) 		return new TRestEditor		( (TRestExp) e);
		else 	if (e instanceof TExpandExp) 	return new TExpandEditor	( (TExpandExp) e);
		else 	if (e instanceof TApplExp) 		return new TApplEditor		( (TApplExp) e);
		else 	if (e instanceof TAbstrExp) 	return new TAbstrEditor		( (TAbstrExp) e);
		else 	if (e instanceof TYAbstrExp) 	return new TYAbstrEditor	( (TYAbstrExp) e);
		else 	if (e instanceof TNamedExp) 	return new TNamedEditor		( (TNamedExp) e);
		else 	if (e instanceof TModifyExp) 	return new TModifyEditor	( (TModifyExp) e);
		else 	if (e instanceof TEvent) 		return new TEventEditor		( (TEvent) e);
		else 	if (e instanceof TNullExp) 		return new TNullEditor		( (TNullExp) e);
		else 	if (e instanceof TInput) 		return new TInputEditor		( (TInput) e);
		else 									return new TUnknownEditor (e);
	}
}
