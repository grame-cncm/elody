package grame.elody.editor.constructors;

import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.TExpMaker;

import java.awt.BorderLayout;
import java.awt.ScrollPane;

//===========================================================================
//TreeApplet : applet visualisant un TreePanel
//===========================================================================

public class TreeApplet extends BasicApplet {
	public TreeApplet() {
		super("Structured Editor");
		setLayout(new BorderLayout());
		setSize (250, 500);
	} 
    public void init() {
		//Define.getButtons(this);
    	ScrollPane p = new ScrollPane();
    	add("Center", p);
		p.add(new TreePanel ( null, 0, TExpMaker.gExpMaker.createNull() ));
		moveFrame (200, 240);
	}   
}
