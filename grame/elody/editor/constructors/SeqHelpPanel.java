package grame.elody.editor.constructors;

import grame.elody.editor.misc.TGlobals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;

public class SeqHelpPanel extends Panel {
	public SeqHelpPanel() {
		setLayout (new GridLayout(2,2, 2,2));
		add (new Label(TGlobals.getTranslation("pitch"), Label.CENTER));
		add (new Label(TGlobals.getTranslation("vel"), Label.CENTER));
		add (new Label(TGlobals.getTranslation("dur"), Label.CENTER));
		add (new Label(TGlobals.getTranslation("chan"), Label.CENTER));
	}
	public Insets getInsets () 	{ return new Insets (1,4,1,4); }	
    public void paint(Graphics g) {
    	Dimension d = getSize ();
		g.setColor (Color.lightGray);
		g.fillRect (0, 0, d.width-1, d.height-1);
		g.setColor (Color.darkGray);
		g.drawRect (0, 0, d.width-1, d.height-1);
    }
}
