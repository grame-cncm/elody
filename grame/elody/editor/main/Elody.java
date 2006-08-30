package grame.elody.editor.main;

import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.appletframe.AppletFrame;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.misc.applets.BasicStub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public final class Elody extends BasicApplet {
	/**
	 * constructeur d'Elody
	 */
  	public Elody() {
  		super("Elody");
 		if (Define.appletMode) BasicStub.share (this);
  	}
	/**
	 * initialisation d'Elody : initialise l'environnement et initialise
	 * le contenu de la fenêtre
	 */
  	public void init() {
  		TGlobals.init ();
  		
  		if (Define.appletMode) {
  			setupContent (getParameter("editors"));
  		}else {
 			setupContent (getParameter("editors") + ",AboutApplet/About_Elody/0,QuitElody/Quit/0");
 			moveFrame (20,20);
 		}
	}
	/**
	 * méthode stop() d'Elody : quitte tout l'environnement Elody
	 */
  	public void stop() {
  		if (Define.appletMode) { 
  			TGlobals.quit ();
  		}else {
  			AppletFrame.quitElody();
  		}
  	}
	
//____________________________________________________________________________________________
// méthode privées
	/**
	 * méthodes privées d'Elody
	 * setupContent installe les différents menus d'accès aux composants Elody
	 * activate permet d'activer un composant
	 */
  	private void setupContent (String str) {
  		ArgsReader ar = new ArgsReader (this, str);
  		Vector<Component> v = ar.components ();
		setLayout (new GridLayout( v.size()+1, 1, 0, 3));
		int w=0, h=0;
		for (int i=0; i<v.size(); i++) {
		    Component c = v.elementAt(i);
		    add (c);
		    Dimension d = c.getMinimumSize();
		    if (d.width > w) w = d.width;
		    h += d.height + 3;
		}
		Panel pan = new Panel(new BorderLayout());
		pan.add("Center",TGlobals.midiapplThru.getMidiLed());
		pan.add("East",TGlobals.midiapplThru.getThru());
		add(pan);
		Insets i = getInsets();
		setSize (w+i.left+i.right, h+i.bottom+i.top);
		activate (ar.actifs ());
	}   
	private void activate (Vector<ComponentLauncher> v) {
		for (int i=0; i<v.size(); i++) {
		    ComponentLauncher c = v.elementAt(i);
		    c.activate();
		 }
	}
	
//____________________________________________________________________________________________
// méthodes statiques
	/**
	 * renvoie la version d'Elody pour la partie interface
	 */
	public static String version() {return "1.21 + Sampler";}	

	/**
	 * point d'entrée d'Elody en mode application
	 */
	public static void main (String[] args) {
		Define.appletMode = false;
		StringBuffer sbuff = new StringBuffer();
		try {
			for (int i=0; i < args.length; i++) {
				BufferedReader reader = new BufferedReader (new InputStreamReader (new FileInputStream (args[i])));
/*				FileReader fr = new FileReader(args[i]);
				BufferedReader reader = new BufferedReader(fr); 
*/				do {
					String str = reader.readLine();
					if (str != null) sbuff.append (str);
					else break;
				} while (true);
			}
		} catch (Exception e) { System.err.println( "Elody main args reading : " + e); }
		BasicStub.init (System.getProperty("user.dir") + "/");
		Hashtable<String, String> appletParams = new Hashtable<String, String>();
		appletParams.put ("EDITORS", sbuff.toString());
		AppletFrame.startApplet("grame.elody.editor.main.Elody", appletParams);
		storeErrsTo ("ElodyErr.txt");
	}

	/**
	 * redirige la sortie d'erreur dans un fichier
     * @param     fileName    le nom du fichier d'erreur, il sera créé dans
     *                        le dossier courant d'Elody
	 */
	public static void storeErrsTo (String fileName) {
		try {
			FileOutputStream file = new FileOutputStream(Define.makeUserFile(fileName));
			PrintStream newErr = new PrintStream(file);
			System.setErr (newErr);
			System.err.println (new Date());
			System.err.println ();
		} catch (Exception e) { System.err.println( "Elody errs redirecting " + e); }
	}
}
