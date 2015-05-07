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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.util.Observer;

public class TextBarCtrl extends Panel {
	EditControler eCtrl;
	
	public TextBarCtrl (EditControler ctrl, String text, int fontSize) {
		setLayout(new BorderLayout (1,1));
   		eCtrl = ctrl;
   		setFont (new Font("Times", Font.PLAIN, fontSize));
		Label tp = new Label(text, Label.CENTER);
  		add ("Center", eCtrl);
		if (ctrl.getDirection() == Controler.kVertical) {
 	  		add ("South", tp);
		} else {
 	  		add ("West", tp);
		}
	}
    public void addObserver 	(Observer o) { eCtrl.ctrl.addObserver(o); }
    public void deleteObserver	(Observer o) { eCtrl.ctrl.deleteObserver(o); }
    public void setValue (int v) 	{ eCtrl.setValue(v); }
    public int 	getValue () 		{ return eCtrl.getValue(); }
    public String toString() { return new String ("TextBarCtrl"); }
}
