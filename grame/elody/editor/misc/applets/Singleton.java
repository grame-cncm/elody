/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.misc.applets;

public class Singleton extends BasicApplet {
	protected Singleton () { }
	public Singleton (String title) 		{ super(title);}

	public void toFront () {
		if (!frame.isShowing()) frame.setVisible(true);
		getFrame().toFront(); 
	}

	public static boolean isSingle (String className) throws ClassNotFoundException {
		Class c = Class.forName(className);
		while (c!=null) {
			if (c.getName().equals("grame.elody.misc.applets.Singleton")) {
				return true;
			}
			c = c.getSuperclass();
		}
		return false;
	}
}
