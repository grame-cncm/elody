package grame.elody.editor.misc.appletframe;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.misc.applets.BasicStub;
import grame.elody.editor.misc.applets.Singleton;
import grame.elody.lang.texpression.expressions.TExp;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * la classe AppletFrame gère les frames des applets Elody
 * elle fournit des constructeurs statiques pour créer de nouvelles applet
 * elle gère la liste des frames actives
 * @see         java.awt.Frame
 * @see         java.awt.event.WindowListener
 */

public class AppletFrame extends Frame implements WindowListener {
	private static final AppletList aList = new AppletList();
	public Applet applet;

	/**
	* constructeur de la frame d'une applet
    * @param     name    le titre de la fenêtre qui va contenir l'applet
    * @param     a       l'applet qui est insérée dans la frame
	*/
	public AppletFrame (String name, Applet a) {
		super(name);
		applet = a;
		add("Center", a);
		pack();
		aList.add (this);
		addWindowListener(this);
	}

    public void windowOpened(WindowEvent e) 		{}
    public void windowClosed(WindowEvent e) 		{}
    public void windowIconified(WindowEvent e) 		{}
    public void windowDeiconified(WindowEvent e) 	{}
    public void windowDeactivated(WindowEvent e) 	{}
    public void windowActivated(WindowEvent e) 		{ aList.toFront (this); }
    public void windowClosing (WindowEvent event) 	{ close(); }
	
	public Point 	getGlobalLocation () 	{ return getLocation(); /*OnScreen ();*/}
	public Applet 	getApplet (Point p) 	{ return aList.getApplet (p);}
	public Applet 	getApplet () 			{ return applet;}
	
	
	// YO (21/09/98)
	public static AppletList 	getAppletList () 	{ return aList;}

	public boolean front () {
		return aList.frontApplet(applet);
	}


	//___________________________________________________________________________
	// méthodes privées 
    private final void close () {
		aList.remove (this);
		applet.stop();
		dispose();
    }	

	//___________________________________________________________________________
	// méthodes statiques 

	/**
	* crée une nouvelle applet et l'initialise avec une expression Elody
    * @param     className    le nom de l'applet à créer, package inclus
    * @param     exp          l'expression qui sera passée à la méthode
    *                         decompose(TExp exp) de l'applet après sa création
	*/
	public static BasicApplet startApplet(String className, TExp exp) {
		BasicApplet a = startApplet(className);
		if (a != null)  a.decompose (exp);
		return a;
	}		
	/**
	* crée une nouvelle applet,
    * @param     className    le nom de l'applet à créer (package inclus)
	*/
	public static BasicApplet startApplet(String className) {
		return startApplet(className, new Hashtable());
	}		
	/**
	* crée une nouvelle applet,
    * @param     className    le nom de l'applet à créer (package inclus)
    * @param     params       ses paramètres : ils seront ensuite accessibles
    *                         pour l'applet via sa méthode getParameter()
	*/
	public static BasicApplet startApplet(String className, Hashtable params) {
		try {
			BasicApplet a;
			if (Singleton.isSingle (className)) {
				a = (BasicApplet)getApplet (className);
				if (a != null) {
					a.toFront();
					return a;
				}
			}
			a = (BasicApplet)Class.forName(className).newInstance();
			BasicStub stub = new BasicStub(a.getFrame(), params);
			a.setStub (stub);
			a.init();
			a.start();
			Frame f = a.getFrame();
			Dimension d = a.getSize();
			stub.appletResize (d.width, d.height);
			f.setVisible(true);
			f.repaint();
			return a;
		} 
		catch (Exception e) {
			System.err.println( "startApplet " + className + " " + e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	* crée une nouvelle applet avec une taille et une position
    * @param     className    le nom de l'applet à créer (package inclus)
    * @param       
    *                        
	*/
	public static BasicApplet startApplet(String className, int x, int y, int width, int height) {
		try {
			BasicApplet a;
			if (Singleton.isSingle (className)) {
				a = (BasicApplet)getApplet (className);
				if (a != null) {
					a.toFront();
					return a;
				}
			}
			a = (BasicApplet)Class.forName(className).newInstance();
			BasicStub stub = new BasicStub(a.getFrame(), new Hashtable());
			a.setStub (stub);
			a.init();
			a.start();
			Frame f = a.getFrame();
			stub.appletResize (width, height);
			a.setSize(width, height);
			f.setLocation (x, y);
			f.setVisible(true);
			f.repaint();
			return a;
		} 
		catch (Exception e) {
			System.err.println( "startApplet " + className + " " + e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	* point de sortie d'Elody :
	* quitte l'environnement dans TGlobals
	* ferme toutes les applets
	* quitte le runtime de la machine virtuelle
	*/
	public static void quitElody () {
   		
   		TGlobals.quit ();
   		
		Enumeration en = aList.elements();
		while (en.hasMoreElements()) {
			Object o = en.nextElement();
			if (o instanceof AppletFrame) {
				AppletFrame f = (AppletFrame)o;
				f.close();
				en = aList.elements();
			}
		}
		
		java.lang.Runtime.getRuntime().exit(0);
	}
	/**
	* renvoie l'applet si elle existe
    * @param     className    le nom de l'applet recherchée
	*/
	public  static Applet getApplet (String className) {
		try 				{ return aList.getApplet (className); } 
		catch (Exception e) { return null; }
	}
}
