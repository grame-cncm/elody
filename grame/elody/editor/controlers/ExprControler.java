/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.controlers;

import grame.elody.editor.misc.Define;
import grame.elody.util.MsgNotifier;

import java.awt.Color;
import java.awt.Image;
import java.util.Observable;

public class ExprControler extends EditControler {
	public ExprControler (Color inColor, Image img, int msg) {
		super (new JamButtonControler(-64,+64,0,inColor, img), Define.TextCtrlSize);
		setMessage (msg);
	}
	public ExprControler (Color inColor, Image img, int msg, boolean displayAbs) {
		super (new JamButtonControler(-64,+64,0,inColor, img), Define.TextCtrlSize, displayAbs);
		setMessage (msg);
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		if (mn.message() == Define.ResetMsg) {
  			setValue (0);
  		}
  		super.update (o, arg);
  	}
}
