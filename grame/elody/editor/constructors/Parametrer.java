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

import grame.elody.editor.constructors.parametrer.ParamExprHolder;
import grame.elody.editor.constructors.parametrer.ParamPanel;
import grame.elody.editor.constructors.parametrer.TParamVisitor;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.lang.TEvaluator;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MsgNotifier;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

public class Parametrer extends BasicApplet implements Observer {
	static final String appletName = "Parametrer";
	ParamPanel param;
	ParamExprHolder exprHolder;
	
	public Parametrer() {
		super(TGlobals.getTranslation("Parametrer"));
		setLayout(new BorderLayout());
		setSize (180, 260);
	} 
    public void init() {
		Define.getButtons(this);
    	param = new ParamPanel(true);
    	exprHolder = new ParamExprHolder();
    	exprHolder.addObserver(this);
    	param.init (exprHolder, 120, 60);
		add("Center", param);
		moveFrame (200, 240);
	}   
	public void decompose (TExp exp) {
		param.decompose (exp);
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier msg = (MsgNotifier)o;
  		if (msg.message() == Define.ExprHolderMsg) {
  			if (exprHolder.isRecentlyDropped())
  			{
  				exprHolder.setRecentlyDropped(false);
  				TParamVisitor v = new TParamVisitor();
  				TEvaluator.gEvaluator.Eval(exprHolder.getExpression()).Accept(v, 0, null);
  				float dur = TEvaluator.gEvaluator.Duration(exprHolder.getExpression());
  				param.initControls(v.getAbsPitch(), v.getAbsVel(), dur, v.getAbsChan());
  			}
  		}
  	}
}
