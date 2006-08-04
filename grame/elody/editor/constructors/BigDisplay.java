package grame.elody.editor.constructors;

import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.VarGraphExprHolder;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;

import java.awt.BorderLayout;

public class BigDisplay extends BasicApplet {
	public BigDisplay() {
		super(TGlobals.getTranslation("Graphic_Display"));
		setLayout(new BorderLayout());
		setSize(400, 200);
	}

	public void init() {
		ExprHolder eh = new VarGraphExprHolder(null, true);
		add("Center", eh);
		moveFrame(200, 240);
	}
}
