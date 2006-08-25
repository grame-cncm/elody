package grame.elody.editor.main;

import grame.elody.editor.misc.TGlobals;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Component;
import java.util.StringTokenizer;
import java.util.Vector;

public class ArgsReader {
	
	final String menuFlag = "Menu:";
	final String langFlag = "Language:";
  	Vector<Component> v;
  	Vector<ComponentLauncher> actifs;
	
	public ArgsReader (Applet applet, String str) {
  		v = new Vector<Component> ();
  		actifs = new Vector<ComponentLauncher> ();
  		for (StringTokenizer t = new StringTokenizer(str, ","); t.hasMoreTokens(); ) {
			String token = t.nextToken().trim();
			if (token.startsWith(langFlag))
				TGlobals.setLanguage(token.substring(langFlag.length()));
			else
			add (applet, token);
		}
	}

	public void add (Applet applet, String str) {
		if (str.startsWith (menuFlag)) {
			addMenu (TGlobals.getTranslation(str.substring (menuFlag.length())));
		}
		else if (v.size() != 0) {
			Object last = v.lastElement();
			if ((last instanceof ComponentMenu) && menuItem(str)){
				addItem (applet, (ComponentMenu)last, str.substring (1));
			}
			else  addButton (applet, str);
		}
		else addButton (applet, str);
	}
	
	public boolean menuItem (String str) {
		return (str.charAt(0) == '+');
	}
	public ComponentLauncher newComponent (Applet applet, String str) {
		ComponentLauncher c = new ComponentLauncher(applet, str);
		if (c.actif()) actifs.addElement (c);
		return c;
	}
	public void addMenu (String str) {
		ComponentMenu menu = new ComponentMenu (str);
		v.addElement (menu);
	}
	public void addItem (Applet applet, ComponentMenu menu, String str) {
		ComponentLauncher c = newComponent (applet, str);
		menu.add (c);
	}
	public void addButton (Applet applet, String str) {
		ComponentLauncher c = newComponent (applet, str);
		Button button = new Button (c.name());
		button.addActionListener (c);
		v.addElement (button);
	}

	public Vector<Component> components ()		{ return v; }
	public Vector<ComponentLauncher> actifs ()	{ return actifs; }
}
