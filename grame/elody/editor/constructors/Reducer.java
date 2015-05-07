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

import grame.elody.editor.constructors.document.NamedExprHolder;
import grame.elody.editor.constructors.document.NamedExprPanel;
import grame.elody.editor.constructors.parametrer.ParamExprHolder;
import grame.elody.editor.constructors.parametrer.ParamFrame;
import grame.elody.editor.constructors.parametrer.ParamPanel;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.FunctionTag;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.expressions.VarArgsFunctionDecomposer;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.util.fileselector.ColoredLabel;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;

public class Reducer extends BasicApplet implements Observer, ActionListener {
	static final String appletName = "Reducer";
	static final int stepsCount = 6;
	static public String clearCommand = TGlobals.getTranslation("Clear");
	NamedExprHolder function; MainExprHolder meh;
	ParamPanel ppanel[];
	
	public Reducer() {
		super(TGlobals.getTranslation("Combiner"));
	}
    public void init() {
    	int exprWidth = 30;
    	int totalSteps = stepsCount + 3;
		Define.getButtons(this);

    	setSize(totalSteps * 110, 176);
		setLayout(new GridLayout(1, totalSteps, 3,3));
		ParamExprHolder exprHolders[] = new ParamExprHolder[stepsCount];
		ppanel = new ParamPanel[stepsCount];
		function = new NamedExprHolder (null, new TNotesVisitor(), true);
    	FunctionPanel fPanel = new FunctionPanel(new NamedExprPanel (function));
    	add (fPanel);
    	SeqMainPanel mainPane = new SeqMainPanel ();
       	mainPane.init (meh = new MainExprHolder (null), exprWidth, exprWidth);
    	add (mainPane);
    	for (int i=0; i<stepsCount; i++) {
    		ppanel[i] = new ParamPanel ();
     		ppanel[i].init (exprHolders[i] = new ParamExprHolder(), exprWidth, exprWidth);
    		add (ppanel[i]);
   			mainPane.addStepObserver (ppanel[i]);
    	}
		meh.setList (exprHolders);
    	add (new ReduceCommandsPanel(function, exprWidth, exprHolders, this));
   		moveFrame (10, 300);
   }
    public void actionPerformed (ActionEvent ev) {
    	if (ev.getActionCommand().equals (clearCommand)) {
			meh.clear();
/*			TExp e = TExpMaker.gExpMaker.createNull();
			for (int i=0; i < exprHolders.length; i++) {
				exprHolders[i].setExpression (e);
			}
*/    	}
	}
	public void decompose (TExp exp) {
		VarArgsFunctionDecomposer md = new VarArgsFunctionDecomposer (exp, ppanel.length+1);
		int n = md.getArgsCount(), i=0;
		function.setExpression (md.getArg(--n));
		while (n > 0) {
			ppanel[i++].decompose (md.getArg(--n));
		}
	}
}

class ReduceHolder extends ResultHolder
{
	ExprHolder function;
	
	public ReduceHolder (ExprHolder function, ExprHolder exprHolders[]) {
		super(exprHolders);
		this.function = function;
	}
	public TExp buildFunction () {
		int n = countExpr();
		TExp f = function.getExpression();
		if (f instanceof TNullExp) return f;
		if (n==0) return exprHolders[0].getExpression ();

		TExpMaker maker = TExpMaker.gExpMaker;
		TExp note[] = new TExp[n];
		TExp r = note[0] = maker.createNote(Define.expColors[0], 60, 100, 0, 1000);
		for (int i=1; i<n; i++) {
			note[i] = maker.createNote( Define.expColors[i], 60, 100, 0, 1000);
			r = TExpMaker.gExpMaker.createAppl ( TExpMaker.gExpMaker.createAppl (f, note[i]), r);
		}
		for (int i=0; i< n; i++) {
			r = maker.createAbstr (note[i], r);
		}
		r = maker.createAbstr (f, r);
		FunctionTag tagger = new FunctionTag (Reducer.appletName, n+1);
		return tagger.tag (r);
	} 
	public TExp buildExpression (TExp f) {
 		f = TExpMaker.gExpMaker.createAppl (f, function.getExpression());
		return super.buildExpression (f);
  	}
    public String toString() 	{ return "ReduceHolder"; }
}

class FunctionPanel extends Panel
{
	public FunctionPanel (NamedExprPanel p) {
		setLayout (new BorderLayout (4,4));
		add ("North", new Label (TGlobals.getTranslation("Function"), Label.CENTER));
		add ("Center", p);
		add ("South", new Label ("", Label.CENTER));
	}
}

class ReduceCommandsPanel extends ParamFrame
{
	public ReduceCommandsPanel(ExprHolder function, int width, ExprHolder exprHolders[], 
							  ActionListener listener) {
		super (Color.red);
		ReduceHolder rh = new ReduceHolder (function, exprHolders);
		super.init (rh, width, width);
   		GridBagLayout gbl = (GridBagLayout)getLayout();
		GridBagConstraints c = new GridBagConstraints();
		int eol = GridBagConstraints.REMAINDER;
		int align = GridBagConstraints.CENTER;

		setConstraints (c, eol, GridBagConstraints.NONE, align, 0,0, 0,0, 0,2,2,1);
		add (new ColoredLabel(TGlobals.getTranslation("Parameters"), Label.CENTER, Color.darkGray), gbl, c);
		setConstraints (c, eol, GridBagConstraints.BOTH, align, 30,30, 1,1, 0,4,4,4);
		add (new SeqHelpPanel (), gbl, c);
		setConstraints (c, eol, GridBagConstraints.NONE, align, 20,0, 1,0, 0,5,5,4);
		Button clear = new Button (Reducer.clearCommand);
     	clear.setActionCommand (Reducer.clearCommand);
     	clear.addActionListener (listener);
		add (clear, gbl, c);

		for (int i=0; i < exprHolders.length; i++) {
			exprHolders[i].addObserver (rh);
		}
		function.addObserver (rh);
	}
	public void add (Component p, GridBagLayout gbl, GridBagConstraints c) {
		gbl.setConstraints (p, c);
		add (p);
	}
}