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