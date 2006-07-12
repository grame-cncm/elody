package grame.elody.editor.constructors.parametrer;

import grame.elody.editor.expressions.DelayedExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.util.MsgNotifier;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;

public class ParamExprHolder extends DelayedExprHolder
{
	int pitch, vel, chan;
	protected float dur;
	TExp original;
		
	public ParamExprHolder () {
		super (null, new TNotesVisitor(), true, 400);
		original = expr;
		pitch = vel = 0;
		dur = 1;
	}
  	public boolean catchPitch (Object arg) {
		int p = ((Integer)arg).intValue();
		if (p != pitch) {
			pitch = p;
			refresh = true;
			return true;
		}
		return false;
  	}
  	public boolean catchVel (Object arg) {
		int p = ((Integer)arg).intValue();
		if (p != vel) {
			vel = p;
			refresh = false;
			return true;
		}
		return false;
  	}
  	public boolean catchChan (Object arg) {
		int p = ((Integer)arg).intValue();
		if (p != chan) {
			chan = p;
			refresh = false;
			return true;
		}
		return false;
  	}
  	public boolean catchDur (Object arg) {
		float p = ((Float)arg).floatValue();
		if (p != dur) {
			dur = p;
			refresh = true;
			return true;
		}
		return false;
  	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		boolean changed = false;
  		switch (mn.message()) {
			case ParamPanel.pitchMsg:	changed = catchPitch (arg);
				break;
			case ParamPanel.velMsg:		changed = catchVel (arg);		
				break;
			case ParamPanel.chanMsg:	changed = catchChan (arg);
				break;
			case ParamPanel.durMsg:		changed = catchDur (arg);	
				break;
  		}
  		if (changed)  changed ();
	}
	public void setExpression (TExp e) {
		original = e;
		super.setExpression (buildExpression(original));
	}
	public void changed () { 
		if (!(original instanceof TNullExp)) {			
			if (refresh) super.changed ();
			else {
				uptodate = false;
				notifyObservers ();
			}
		}
	}
	public boolean isNull () 	{ return original instanceof TNullExp; }
	public TExp buildExpression() {
		return buildExpression (original);
	}
	public TExp createPitch (TExp e, float pitch) {
		return (pitch != 0) ? TExpMaker.gExpMaker.createTrsp  (e, pitch) : e;
	}
	public TExp createVelocity (TExp e, float vel) {
		return (vel != 0) ? TExpMaker.gExpMaker.createAttn  (e, vel) : e;
	}
	public TExp createDuration (TExp e, float dur) {
		return (dur != 1) ? TExpMaker.gExpMaker.createExpv  (e, dur) : e;
	}
	public TExp createChannel (TExp e, float chan) {
		return (chan != 0) ? TExpMaker.gExpMaker.createTrsch  (e, chan) : e;
	}
	public TExp buildExpression(TExp e) {
		if (e instanceof TNullExp) return e;
		e = createPitch  	(e, (float)pitch);
		e = createVelocity  (e, (float)vel);
		e = createChannel  	(e, (float)chan);
		e = createDuration  (e, dur);
		return e;
	}
/*    public void handleDoubleClick (MouseEvent e) {
    	setCursor (new Cursor(Cursor.WAIT_CURSOR));
		new ExprDecomposer (original);
    	setCursor (new Cursor(Cursor.DEFAULT_CURSOR));
    }
*/
	public void frame (boolean draw) {
		ParamFrame f = (ParamFrame)getParent();
		Graphics g = f.getGraphics ();
		if (draw) f.paint (g, Color.black);
		else f.paint (g);
	}
}
