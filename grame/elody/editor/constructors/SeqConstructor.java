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
import grame.elody.editor.expressions.VarArgsFunctionDecomposer;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;

public class SeqConstructor extends BasicApplet implements Observer,
		ActionListener {
	static final String appletName = "SeqConstructor";
	static final int stepsCount = 8;
	static public String clearCommand = TGlobals.getTranslation("Clear");
	SeqPlayerMgr playMgr; MainExprHolder meh;
	ParamPanel ppanel[];
	
	public SeqConstructor() {
		super(TGlobals.getTranslation("Sequence_constructor"));
	}
    public void init() {
    	int exprWidth = 30;
		Define.getButtons(this);

    	setSize(10 * 110, 176);
		setLayout(new GridLayout(1, 10, 3,3));
		ParamExprHolder exprHolders[] = new ParamExprHolder[stepsCount];
		ppanel = new ParamPanel[stepsCount];
    	SeqMainPanel mainPane = new SeqMainPanel ();
		playMgr = new SeqPlayerMgr (exprHolders);  	
       	mainPane.init (meh = new MainExprHolder (playMgr), exprWidth, exprWidth);
    	add (mainPane);
    	for (int i=0; i<stepsCount; i++) {
    		ppanel[i] = new ParamPanel ();
     		ppanel[i].init (exprHolders[i] = new ParamExprHolder(), exprWidth, exprWidth);
    		add (ppanel[i]);
   			mainPane.addStepObserver (ppanel[i]);
   			exprHolders[i].addObserver (playMgr);
    	}
		meh.setList (exprHolders);
    	add (new SeqCommandsPanel(exprHolders, exprWidth, playMgr, this));
   		moveFrame (10, 300);
   }
    public void start() {
    	super.start();
    	if (playMgr!=null) playMgr.open();
    }
    public void stop() {
    	if (playMgr!=null) playMgr.close();
   	}
    public void actionPerformed (ActionEvent e) {
    	String action = e.getActionCommand();
    	if (action.equals (clearCommand)) {
    		meh.clear();
    	}
	}
	public void decompose (TExp exp) {
		VarArgsFunctionDecomposer sd = new VarArgsFunctionDecomposer (exp, ppanel.length);
		int n = sd.getArgsCount(), i=0;
		while (n > 0)
			ppanel[i++].decompose (sd.getArg(--n));
	}
}