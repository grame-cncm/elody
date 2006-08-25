package grame.elody.editor.misc.applets;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.net.URL;
import java.util.Hashtable;

public class BasicStub implements AppletStub {
	static private Applet sharedStub = null;
	static private String base = ".";
	private Hashtable<String, String> paramTable;
	private Frame frame;
	
    static public void init (String doc) {
    	if (doc!=null) base = doc;
    }
    static public void share (Applet a) { sharedStub = a; }

	public BasicStub (Frame f, Hashtable<String, String> params) {
		frame = f;
		paramTable = params;
	}
    public URL getDocumentBase() {
    	try {
    		return (sharedStub != null) ? sharedStub.getDocumentBase() : new URL("file:" + base);
    	}
    	catch (Exception e) {
			System.err.println( "getDocumentBase exception : " + e);
    	}
    	return null;
    }
    public URL getCodeBase() {
    	try {
    		return (sharedStub != null) ? sharedStub.getCodeBase() : new URL("file:" + base);
    	}
    	catch (Exception e) {
			System.err.println( "getCodeBase exception : " + e);
    	}
    	return null;
    }
    public String getParameter(String name)	
    	{ return (paramTable != null) ? paramTable.get(name.toUpperCase()) : null; }
    public AppletContext getAppletContext()	
    	{ return (sharedStub != null) ? sharedStub.getAppletContext() : null; }
    public boolean isActive()				
    	{ return true; }
    public void appletResize(int width, int height) { 
			Insets i = frame.getInsets ();
			width += i.left + i.right;
			height+= i.bottom + i.top;
			Dimension d = frame.getSize();
			if ((d.width != width) || (d.height != height)) {
				frame.setSize (width, height);
			}
    }
}
