package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.AbstractionOp;
import grame.elody.editor.constructors.operateurs.ApplicationOp;
import grame.elody.editor.constructors.operateurs.BeginOp;
import grame.elody.editor.constructors.operateurs.MixageOp;
import grame.elody.editor.constructors.operateurs.Operateur;
import grame.elody.editor.constructors.operateurs.RestOp;
import grame.elody.editor.constructors.operateurs.SequenceOp;
import grame.elody.editor.constructors.operateurs.StretchOp;
import grame.elody.editor.constructors.operateurs.YAbstractionOp;
import grame.elody.editor.expressions.ExprDecomposer;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.net.URL;

public class Rules extends BasicApplet {
	   public Rules() {
	    	super(TGlobals.getTranslation("Rules"));
			setLayout(new GridLayout(0,1,10,2));
			setSize(250, 360);
		}
	    public void init() {
			Image abstr, yabstr, appl, seq, mix, equal, begin, reste, stretch;
			try {
				URL base = getDocumentBase();
				MediaTracker mTrk = new MediaTracker(this);
				mTrk.addImage (abstr = getImage(base, "Images/abstr.png"), 1);
				mTrk.addImage (appl = getImage(base, "Images/appl.png"), 2);
				mTrk.addImage (seq = getImage(base, "Images/seq.png"), 3);
				mTrk.addImage (mix = getImage(base, "Images/mix.png"), 4);
				mTrk.addImage (yabstr = getImage(base, "Images/yabstr.png"), 5);
				mTrk.addImage (equal = getImage(base, "Images/equal.png"), 6);
				mTrk.addImage (begin = getImage(base, "Images/begin.png"), 7);
				mTrk.addImage (reste = getImage(base, "Images/rest.png"), 8);
				mTrk.addImage (stretch = getImage(base, "Images/stretch.png"), 9);
				mTrk.waitForAll ();
				if (mTrk.checkAll (true) && !mTrk.isErrorAny()) {
					RulePanel.eqImage = equal;
				}
				else { yabstr = abstr = appl = seq = mix = begin = reste = stretch = null; }
			}
			catch (Exception e) { 
				yabstr = abstr = appl = seq = mix = begin = reste = stretch = null;
				System.err.println( "Rules init " + e);
			}
			add ( new RulePanel(new AbstractionOp (abstr), new AbstrResultHolder()));
			add ( new RulePanel( new YAbstractionOp (yabstr), new YAbstrResultHolder()));
			add ( new RulePanel( new ApplicationOp (appl), new ApplResultHolder()));
			add ( new RulePanel( new SequenceOp (seq), new SeqResultHolder()));
			add ( new RulePanel( new MixageOp (mix), new MixResultHolder()));
			add ( new RulePanel( new BeginOp (begin), new BeginResultHolder()));
			add ( new RulePanel( new RestOp (reste), new RestResultHolder()));
			add ( new RulePanel( new StretchOp (stretch), new StretchResultHolder()));
			moveFrame (10, 10);
		}
}

class EqualSign extends Panel
{
	Image image;
	
    public EqualSign (Image img) {
    	super();
    	image = img;
    }
    public void paint(Graphics g) {
		Dimension d = getSize();
    	int offset = 6;
    	d.width-= offset*2; d.height-= offset*2;
    	if (image == null) {
			int h = d.height/3;
			int left = offset + 4;
			int w = d.width - 8;
			int y = offset + h;
			int y2 = y + h;
			g.setColor	(Color.darkGray);
			g.fillRect (left, y-2, w, 2);
			g.fillRect (left, y2-2, w, 2); 
			g.setColor	(Color.gray);
			g.fillRect (left, y, w, 2);
			g.fillRect (left, y2, w, 2); 
		}
		else {
			int w; do { w = image.getWidth(null);  } while (w < 0);
			int h; do { h = image.getHeight(null); } while (h < 0);
		/*	if (w > d.width)  w = d.width;
			if (h > d.height) h = d.height; */
			g.drawImage (image, offset+(d.width-w)/2, offset+(d.height-h)/2, w, h, null);
		}
    }
}

class RuleExprHolder extends ExprHolder
{
	RulePanel container;
	
	public RuleExprHolder (TExp e, boolean accept, RulePanel p) {
		super(e, new TNotesVisitor(), accept);
		container = p;
	}
	public void setExpression (TExp e) { 
		super.setExpression(e);
		container.composeExpr();
	}
}

class MixResultHolder extends ResultExprHolder {
	public boolean accept (TExp exp) { return ExprDecomposer.isMix (exp); }
}

class SeqResultHolder extends ResultExprHolder {
	public boolean accept (TExp exp) { return ExprDecomposer.isSeq (exp); }
}

class ApplResultHolder extends ResultExprHolder {
	public boolean accept (TExp exp) { return ExprDecomposer.isAppl (exp); }
}

class AbstrResultHolder extends ResultExprHolder {
	public boolean accept (TExp exp) { return ExprDecomposer.isAbstr (exp); }
}

class YAbstrResultHolder extends ResultExprHolder {
	public boolean accept (TExp exp) { return ExprDecomposer.isYAbstr (exp); }
}

class BeginResultHolder extends ResultExprHolder {
	public boolean accept (TExp exp) { return ExprDecomposer.isBeg (exp); }
}

class RestResultHolder extends ResultExprHolder {
	public boolean accept (TExp exp) { return ExprDecomposer.isEnd (exp); }
}

class StretchResultHolder extends ResultExprHolder {
	public boolean accept (TExp exp) { return ExprDecomposer.isStretch (exp); }
}

class RulePanel extends Panel
{
    RuleExprHolder eh1, eh2;
    ResultExprHolder resultat;
    Operateur op; Image opImage;
    EqualSign eq;
    static Image eqImage = null;
    
	public RulePanel (Operateur op, ResultExprHolder resultHolder) {
		setLayout(new GridLayout(1,5,4,6));
		eq = new EqualSign (eqImage);
		add (eh1 = new RuleExprHolder (null, true, this));
		add (this.op = op);
		add (eh2 = new RuleExprHolder (null, true, this));
		add (eq = new EqualSign (eqImage));
		resultat = resultHolder;
		resultat.container = this;
		add (resultat);
	}
    public void composeExpr() {
    	TExp e1 = eh1.getExpression();
    	TExp e2 = eh2.getExpression();
    	if ((e1!=null) && (e2!=null)) {
    		resultat.setExpression (op.compose(e1, e2));
    	}
    }
}
