package grame.elody.editor.constructors.colors;

import grame.elody.editor.misc.draganddrop.TColorContent;
import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;

public class DraggedColor implements TColorContent, TExpContent {
	private final Color color;
	
	public DraggedColor (Color c) {
		color = c;
	}
	public TExp getExpression() {
		return  TExpMaker.gExpMaker.createNote(color, 60, 100, 0, 1000);
	}
	public Color getColor() {
		return color;
	}
}
