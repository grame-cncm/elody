package grame.elody.editor.treeeditor;

import grame.elody.lang.texpression.expressions.TExp;

//===========================================================================
//ExpDropObserver : un "observateur" d'expressions
//===========================================================================

public interface ExpDropObserver {
	public void dropExpression(TExp e);
}
