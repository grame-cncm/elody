package grame.elody.editor.controlers;

import grame.elody.editor.misc.Define;
import grame.elody.util.MsgNotifier;

import java.awt.Color;
import java.awt.Image;
import java.util.Observable;

public class ChanControler1 extends EditControler {
	public ChanControler1 (Color inColor, Image img, int msg) {
		super (new JamButtonControler(1,32,1,inColor,img), Define.TextCtrlSize);
		setMessage (msg);
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		if (mn.message() == Define.ResetMsg) 
  			setValue (0);
  		super.update (o, arg);
  	}
}
