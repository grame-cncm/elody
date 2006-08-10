package grame.elody.editor.misc;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;

public class Define {
	public static boolean	 appletMode		= true;
	/**
		taille des zones TextField de paramétrage des expressions liées à 
		des boutons rotatifs
	*/
	public static final int TextCtrlSize 	= 2;
	/**
		couleur des boutons rotatifs de paramétrage d'une expression 
	*/
	public static final Color pitchColor 	= new Color (255,160,140);
	public static final Color velColor 	= new Color (140,160,255);
	public static final Color durColor 	= new Color (140,255,160);
	public static final Color chanColor 	= new Color (240,220,220);
	/**
		couleur de fond d'une expression nulle
	*/
	public static final Color nullExpColor = new Color (235,235,235);
	/**
		liste des couleurs disponibles dans l'applet Colors
	*/
	public static final Color expColors[] = { Color.white, Color.yellow, Color.orange, Color.red, 
							Color.pink, Color.magenta, Color.green, Color.cyan, Color.blue, 
							Color.lightGray, Color.gray, Color.darkGray, Color.black };
	
	public static final int TextBarFieldMsg		= 1000;
	public static final int BarControlerMsg		= 1001;
	public static final int EditBarControlerMsg	= 1002;
	public static final int ExprHolderMsg			= 1003;
	public static final int ShiftControlMsg		= 1004;
	public static final int ResetMsg				= 1005;
	public static final int ExprNameKeyMsg			= 1006;
	public static final int SetControlMsg			= 1007;

	public static final String AbstractApplet 	= "AbstractRule";
	public static final String ApplyApplet 	= "ApplyRule";
	public static final String SeqApplet 		= "SeqRule";
	public static final String MixApplet 		= "MixRule";
	public static final String BeginApplet 	= "BeginRule";
	public static final String EndApplet 		= "EndRule";
	public static final String StretchApplet 	= "StretchRule";
	public static final String YAbstractApplet = "YAbstrRule";
	public static final String ParamApplet 	= "Parametrer";

	public static Image pitchButton, velButton, durButton, chanButton;

	/**
		teste la propriété java.vendor, renvoie vrai si le début correspond
		à la chaine passée en argument.
		ex: isVMFrom("Apple") renvoie vrai pour une machine virtuelle Apple)
	*/
	public static boolean isVMFrom (String vendor) {
		String str = System.getProperty("java.vendor");
		return str.startsWith (vendor);
	}

	/**
		construit le chemin d'acces des fichiers utilisateurs
		dependant plate-forme:
			MacOS, Windows : renvoie file
			Linux          : renvoie $HOME/.file
	*/

	public static String makeUserFile (String file) {
		if (isOS ("Linux")) {
			return userHome()+"/."+file;
		}
		else return file;
	}
	/**
		teste la propriété os.name, renvoie vrai si elle correspond à la chaine passée 
		en argument.
	*/
	public static boolean isOS (String os) {
		String str = System.getProperty("os.name").toLowerCase();
		return str.startsWith (os.toLowerCase());
	}
	/**
		renvoie la propriété user.home 
	*/
	public static String userHome () {
		return System.getProperty("user.home");
	}
	/**
		renvoie le numéro de version de java sous forme d'un integer à 3 chiffres
		par exemple, pour la version 1.1.6, l'entier renvoye sera 116
		si la troisieme position n'est pas un chiffre, elle sera mise à zero
		par exemple, la version 1.2beta sera chiffrée 120
	*/
	public static int javaVersion () {
		int version = 0;
		try {
			String str = System.getProperty("java.version");
			char [] v = new char[3];
			v[0] = str.charAt(0); v[1] = str.charAt(2);
			v[2] = Character.isDigit(str.charAt(4)) ? str.charAt(4) : '0';
			version = Integer.valueOf(new String(v)).intValue();
		}
		catch (Exception e) {
			System.err.println( "converting java version: " + e);
		}
		return version;
	}
	
    //__________________________________________________________________
    static public void getButtons (/*Basic*/ Applet applet) {
    	if (pitchButton == null) {
			try {
				URL base = applet.getDocumentBase();
				MediaTracker mTrk = new MediaTracker(applet);
				mTrk.addImage (pitchButton = applet.getImage(base, "Images/rotatifRouge.png"), 1);
				mTrk.addImage (velButton = applet.getImage(base, "Images/rotatifBleu.png"), 2);
				mTrk.addImage (durButton = applet.getImage(base, "Images/rotatifVert.png"), 3);
				mTrk.addImage (chanButton = applet.getImage(base, "Images/rotatifOrange.png"), 4);
				mTrk.waitForAll (10000);
				if (/*!mTrk.checkAll (true) || */ mTrk.isErrorAny()) {
	  				System.err.println( "getImages failed !!");
					pitchButton = velButton = durButton = chanButton = null;
				}
			}
			catch (Exception e) { 
	  			System.err.println( "static getButtons : while getImages : exception " + e);
				pitchButton = velButton = durButton = chanButton = null;
			}
		}
    }

	public static Color[] getExpColors() { return expColors; }
}
