package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.AbstractionOp;
import grame.elody.editor.constructors.operateurs.ApplicationOp;
import grame.elody.editor.constructors.operateurs.MixageOp;
import grame.elody.editor.constructors.operateurs.Operateur;
import grame.elody.editor.constructors.operateurs.SequenceOp;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.misc.draganddrop.DragAble;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;

public class UniversalBuilder extends BasicApplet {
	public UniversalBuilder() {
		super(TGlobals.getTranslation("Builder"));
		setLayout(new GridLayout(3,3,2,2));
    	setSize (294,294);
	} 
    public void init() {
		ExprHolder result = new ExprHolder (null, new TNotesVisitor(), true);
		add (new BuildAppl	(result, true));
		add (new BuildMix 	(result, true));
		add (new BuildAbstr	(result, false));
		add (new BuildSeq 	(result, true));
		add (result);
		add (new BuildSeq 	(result, false));
		add (new BuildAbstr	(result, true));
		add (new BuildMix 	(result, false));
		add (new BuildAppl	(result, false));
		moveFrame (100, 100);
	}   
}

class BuildMix extends BuildExprDrop 
{
	public BuildMix (ExprHolder result, boolean first) {
		super (result, first);
		this.op = new MixageOp(null);
		name = TGlobals.getTranslation("mix");
	}
}

class BuildSeq extends BuildExprDrop 
{
	public BuildSeq (ExprHolder result, boolean first) {
		super (result, first);
		this.op = new SequenceOp(null);
		name = TGlobals.getTranslation("seq");
	}
}

class BuildAppl extends BuildExprDrop 
{
	public BuildAppl (ExprHolder result, boolean first) {
		super (result, first);
		this.op = new ApplicationOp(null);
		name = TGlobals.getTranslation("appl");
	}
}

class BuildAbstr extends BuildExprDrop 
{
	public BuildAbstr (ExprHolder result, boolean first) {
		super (result, first);
		this.op = new AbstractionOp(null);
		name = TGlobals.getTranslation("abstr");
	}
}

abstract class BuildExprDrop extends ExprHolder
{
	ExprHolder resultExpr;
	Operateur op; String name;
	boolean firstArg;
	
	public BuildExprDrop (ExprHolder result, boolean first) {
		super (null, new TNotesVisitor(), true);
		resultExpr = result;
		firstArg = first;
	}
	public DragAble getDragObject (int x, int y) {
		return null;
    }
    public void paint(Graphics g, Point p, Dimension d) {
    	super.paint(g, p, d);
    	if (name != null) {
     		int textH = getFontMetrics (getFont()).getHeight();
			g.setColor(Color.black);
			g.drawString (name, p.x+4, p.y+textH);
		}
    }
	public void setExpression (TExp e) { 
		TExp exp1 = e;
		TExp exp2 = resultExpr.getExpression();
		if (!firstArg) {
			exp1 = exp2;
			exp2 = e;
		}
		resultExpr.setExpression (op.compose (exp1, exp2));
	}
}