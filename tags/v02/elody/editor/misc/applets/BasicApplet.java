package grame.elody.editor.misc.applets;

import grame.elody.editor.misc.appletframe.AppletFrame;
import grame.elody.lang.texpression.expressions.TExp;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class BasicApplet extends Applet implements Observer {
	private static BasicContext context = new BasicContext();
	protected AppletFrame frame;
	
	protected BasicApplet () { }
	public BasicApplet (String title) {
		super();
		frame = new AppletFrame (title, this);
		setFont (new Font("Times", Font.PLAIN, 12));
	}

	public Insets getInsets () 						{ return new Insets (5, 5, 5, 5); }	
	public void update (Observable o, Object arg) 	{ }
	public final Frame getFrame () 					{ return frame; }
	public boolean frontApplet ()					{ return frame.front();	}
	
	public void decompose (TExp exp) {
		System.err.println ("Decompose(TExp exp) not implemented : " + this);
	}
	public AppletContext getAppletContext() 	{ return context; }
	public void setResizable (boolean resize) 	{ getFrame().setResizable (resize); }
	public void setTitle (String title) 		{ getFrame().setTitle (title); }
	public void moveFrame (int x, int y) 		{ getFrame().setLocation (x, y); }
	public void toFront () 						{ getFrame().toFront(); }
}

class BasicContext implements AppletContext 
{
    public AudioClip getAudioClip(URL url)	{ return null; }
    public Image getImage(URL url) {
    	Toolkit tk = Toolkit.getDefaultToolkit();
    	return tk.getImage(url);
    }
    public Applet getApplet(String name)	{ return null; }
    public Enumeration getApplets()			{ return null; }
    public void showDocument(URL url)					{ }
    public void showDocument(URL url, String target)	{ }
    public void showStatus(String status)				{ }
	public void setStream(String arg0, InputStream arg1) throws IOException { }
	public InputStream getStream(String arg0) { return null; }
	public Iterator getStreamKeys() { return null; }
}
