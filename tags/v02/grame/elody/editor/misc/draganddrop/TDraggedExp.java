package grame.elody.editor.misc.draganddrop;

import grame.elody.lang.texpression.expressions.TExp;

public class TDraggedExp implements TExpContent {
	TExp draggedExp;
	
	public TDraggedExp (TExp exp) 	{ draggedExp = exp; }
	public TExp		getExpression()	{ return draggedExp; }
}
