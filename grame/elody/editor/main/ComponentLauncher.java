package grame.elody.editor.main;

import grame.elody.editor.misc.appletframe.AppletFrame;

import java.applet.Applet;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

public class ComponentLauncher implements ActionListener {
	String name, className;
	boolean activate; Applet applet;
	
	
	public ComponentLauncher (Applet a, String str) {
		applet = a;
		StringTokenizer t 	= new StringTokenizer(str, "/");
	    className 	= t.nextToken().trim();
	    name 		= t.hasMoreTokens() ? t.nextToken().trim() : className;
	    activate = false;
	    if (t.hasMoreTokens()) activate = new Integer(t.nextToken()).intValue() != 0;
	}

	public void activate() {
	    applet.setCursor (new Cursor(Cursor.WAIT_CURSOR));
		AppletFrame.startApplet ("grame.elody.editor.constructors.".concat(className));
	    applet.setCursor (new Cursor(Cursor.DEFAULT_CURSOR));
	}
    public void actionPerformed (ActionEvent e) {
    	activate ();
    }
    public String 	name () 	{ return name; }
    public boolean 	actif () 	{ return activate; }
}
