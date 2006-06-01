package grame.elody.editor.player;

import grame.elody.lang.texpression.expressions.TExp;

public interface ExpObserver {
	TExp getExpression(int i);
	void startExpression(int i);
}
