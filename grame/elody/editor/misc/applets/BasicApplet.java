package grame.elody.editor.misc.applets;

import grame.elody.editor.misc.appletframe.AppletFrame;
import grame.elody.editor.misc.winsolver.Screen;
import grame.elody.lang.texpression.expressions.TExp;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.AWTEvent;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;

public class BasicApplet extends Applet implements Observer {
	private static BasicContext context = new BasicContext();
	protected static int count = 0;
	public static Screen screen = new Screen();
	
	protected AppletFrame frame;
	protected Window w;
	
	protected BasicApplet () { }
	static  {
		Toolkit.getDefaultToolkit().addAWTEventListener(new FunctionKeyListener(), AWTEvent.KEY_EVENT_MASK);
	}
	public BasicApplet (String title) {
		super();
		frame = new AppletFrame (title, this);
		w = new Window(frame);
		screen.addWindow(w);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent arg0) {
				screen.delWindow(w);
			}
		});

		setFont (new Font("Times", Font.PLAIN, 12));
	}
	
	public static void updateScreen()
	{
		if (screen.c1)			screen.c1 = false;
		if (screen.c2)			screen.c2 = false;
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

class FunctionKeyListener implements AWTEventListener {
	
	public void eventDispatched(AWTEvent event) {
		if (event.getID() == KeyEvent.KEY_PRESSED) {
			KeyEvent e = (KeyEvent) event;
			int kc = e.getKeyCode();
			if (kc == KeyEvent.VK_F11) {
				BasicApplet.screen.compute1();
			}
			if (kc == KeyEvent.VK_F12) {
					BasicApplet.screen.compute2();
			}
		}
	}
}