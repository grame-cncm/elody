package grame.elody.editor.main;

import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

public class ComponentMenu extends Choice implements ItemListener {
	Vector v;
	
	public ComponentMenu (String title) {
		v = new Vector();
		addItemListener (this);
		add (title.trim());
		add ("----------------------");
	}
	
	public void add (ComponentLauncher cl) {
		v.addElement (cl);
		add (cl.name());
	}
	public void itemStateChanged(ItemEvent e) {
		int i = getSelectedIndex () - 2;
		if (i >= 0) {
			ComponentLauncher cl = (ComponentLauncher)v.elementAt(i);
			cl.activate();
		}
		select (0);
	}
}
