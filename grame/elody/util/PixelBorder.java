/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Panel;

public class PixelBorder extends Panel {
	int size = 5;
	
	public  PixelBorder (Component borderme) {
		setLayout(new BorderLayout());
		add("Center", borderme);
	}
	
	public  PixelBorder (Component borderme, int size) {
		setLayout(new BorderLayout());
		add("Center", borderme);
		this.size = size;
	}

	public Insets getInsets() {return new Insets (size,size,size,size);}
}
