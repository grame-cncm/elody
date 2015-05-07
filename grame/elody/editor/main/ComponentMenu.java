/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.main;

import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

public class ComponentMenu extends Choice implements ItemListener {
	Vector<ComponentLauncher> v;
	
	public ComponentMenu (String title) {
		v = new Vector<ComponentLauncher>();
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
			ComponentLauncher cl = v.elementAt(i);
			cl.activate();
		}
		select (0);
	}
}
