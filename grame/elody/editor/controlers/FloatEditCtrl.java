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

import java.util.Observable;

public class FloatEditCtrl extends EditControler {
	// on suppose que les valeurs sont comprises entre 0 et max, 	
	public FloatEditCtrl (Controler ctrl, int cols) {
		super (ctrl, cols);
	}
	public FloatEditCtrl (Controler ctrl, int cols, boolean displayAbs) {
		super (ctrl, cols, displayAbs);
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		switch (mn.message()) {
  			case Define.TextBarFieldMsg:
  				if ( ctrl.getValue()==(int)(Float.valueOf(edit.getText()).floatValue()*10) )
				{
					float v = (float) Integer.valueOf(editAbs.getText()).intValue()/absRef;
  					ctrl.setValue(float2int(v));
				}
  				else
  				{
  					String s = (String)arg;
  					ctrl.setValue (float2int(new Float(s).floatValue()));
  				}
  				break;
  			case Define.BarControlerMsg:
  				Integer i = (Integer)arg;
  				Float f = new Float(int2float(i.intValue()));
    			edit.setText (f.toString());
    			if (displayAbs&&(absRef!=-1))
    				editAbs.setText (String.valueOf((int)(f.floatValue()*absRef)));
  				break;
  			case Define.ShiftControlMsg:
    			shiftValue (float2shift(((Float)arg).floatValue()));
  				break;
  			case Define.SetControlMsg:
    			ctrl.setValue (float2shift(((Float)arg).floatValue()));
  				break;
  		}
  		notifyObservers ();
  	}
    public int float2shift (float v) {
    	if (v >= 1) return (int)v - 1;
    	return (int)(v * 10) - 10;
    }
    public int float2int (float v) {
    	if (v >= 1) return (int)v + 9;
    	return (int)(v / 0.1);
    }
    public float int2float (int v) {
    	if (v >= 10) return (float)v - 9;
    	return (float)v / 10;
    }
    public void setValue (float v)  {
    	int intValue = float2int (v);
    	ctrl.setValue (intValue);
    	edit.setText (new Float(int2float(ctrl.getValue())).toString());
  		notifyObservers ();
    }
    
    public void setValue (int v) 	{ setValue ((float)v); }
    public float getFloatValue () 	{ return int2float(ctrl.getValue()); }
    public void notifyObservers () 	{ notifier.notifyObservers (new Float(getFloatValue())); }
}
