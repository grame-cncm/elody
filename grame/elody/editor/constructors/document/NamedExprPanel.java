/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors.document;

import grame.elody.editor.expressions.ExprHolder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NamedExprPanel extends Panel {
	public NamedExprPanel (NamedExprHolder holder) {
 		setLayout(new BorderLayout(0,2));
     	add ("Center", holder);
   		NameTextField edit = new NameTextField (holder);
		edit.setBackground (new Color(240,240,255));
		edit.setFont (new Font("Times", Font.PLAIN, 10));
		add ("South", edit);
      	holder.nameHolder = edit;
	}
}

class NameTextField extends TextField implements KeyListener
{
	ExprHolder expr;
	
	public NameTextField (ExprHolder eh) {
		super ();
		expr = eh;
		addKeyListener (this);
	}
    public void keyPressed(KeyEvent e)	{}
    public void keyReleased(KeyEvent e)	{}
    public void keyTyped (KeyEvent e) {
		expr.notifyObservers ();
    }
}