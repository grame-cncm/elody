/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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