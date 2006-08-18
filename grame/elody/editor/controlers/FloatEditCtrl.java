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
  				String s = (String)arg;
    			ctrl.setValue (float2int(new Float(s).floatValue()));
  				break;
  			case Define.BarControlerMsg:
  				Integer i = (Integer)arg;
  				Float f = new Float(int2float(i.intValue()));
    			edit.setText (f.toString());
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
