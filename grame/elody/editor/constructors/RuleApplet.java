package grame.elody.editor.constructors;

import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;

public class RuleApplet extends BasicApplet {
    ResultExprHolder result;
	
    public RuleApplet (String ruleName) {
    	super(ruleName);
		setSize(250, 60);
	}
    public Image getRuleImage(String imgName) {
		Image img = null;
		try {
			URL base = getDocumentBase();
			MediaTracker mTrk = new MediaTracker(this);
			mTrk.addImage (img = getImage(base, "Images/" + imgName), 1);
			mTrk.waitForAll ();
			if (mTrk.isErrorAny()) { img = null; }
		}
		catch (Exception e) { 
			img = null;
			System.err.println( "RuleApplet getImage : " + e);
		}
		return img;
    }
    public void init() {
    	if (RulePanel.eqImage == null)
    		RulePanel.eqImage = getRuleImage ("equal.png");
	}   
	public void decompose (TExp exp) {
		if (result != null) {
			result.decomposeExpr (exp);
		}
		else System.err.println ("RuleApplet : result expr holder is null !");
	}
}