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

public class DurationControler extends FloatEditCtrl {
	public DurationControler (Color inColor, Image img, int msg) {
		super (new JamButtonControler(1,19,10,inColor,img), Define.TextCtrlSize);
		setMessage (msg);
	}
	public DurationControler (Color inColor, Image img, int msg, boolean displayAbs) {
		super (new JamButtonControler(1,19,10,inColor,img), Define.TextCtrlSize, displayAbs);
		setMessage (msg);
	}
    public void initAbsValue (int v) {
    	if (v!=-1)
    	{
    		absRef = v*10/ctrl.getValue();
    		editAbs.setText(String.valueOf(v));
    		editAbs.setEnabled(true);
    	}
    	else
    	{
    		absRef = -1;
    		editAbs.setText("");
    		editAbs.setEnabled(false);
    	}
    }
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		if (mn.message() == Define.ResetMsg) 
  			setValue (1);
  		super.update (o, arg);
  	}
}
