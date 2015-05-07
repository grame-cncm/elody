/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.util.fileselector;

import java.awt.Color;
import java.awt.Label;

public class ColoredLabel extends Label {
	public ColoredLabel (String text, int align, Color color) {
		super (text, align);
		setForeground (color);
	}
}
