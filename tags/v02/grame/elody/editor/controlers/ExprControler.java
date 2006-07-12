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
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		if (mn.message() == Define.ResetMsg) {
  			setValue (0);
  		}
  		super.update (o, arg);
  	}
}
