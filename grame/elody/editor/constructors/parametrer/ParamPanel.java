package grame.elody.editor.constructors.parametrer;

import grame.elody.editor.controlers.ChanControler;
import grame.elody.editor.controlers.DurationControler;
import grame.elody.editor.controlers.EditControler;
import grame.elody.editor.controlers.ExprControler;
import grame.elody.editor.controlers.FloatEditCtrl;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.ParamDecomposer;
import grame.elody.editor.misc.Define;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MsgNotifier;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

public class ParamPanel extends ParamFrame implements Observer {
	static protected final int pitchMsg 	= 5000;
	static protected final int velMsg		= 5001;
	static protected final int durMsg		= 5002;
	static protected final int chanMsg	= 5003;

	protected EditControler pitchCtrl, velCtrl, chanCtrl, durCtrl;
	protected ResetButton reset;
	protected ExprHolder holder;
	protected MsgNotifier notifier;

	public ParamPanel () {
		super (new Color(175,175,255));
		notifier = new MsgNotifier (Define.ResetMsg);
	}
	public void decompose (TExp exp) {
		ParamDecomposer pdec = new ParamDecomposer();
		pdec.scan(exp);
		TExp body = pdec.getExpr();
		if (body != null) {
			holder.setExpression (body);
			pitchCtrl.setValue ((int)pdec.getPitch());
			velCtrl.setValue ((int)pdec.getVel());
			chanCtrl.setValue ((int)pdec.getChan());
			((FloatEditCtrl)durCtrl).setValue (pdec.getStrech());
		}
	}
	public void init (ExprHolder eh, int w, int h) {
		super.init (eh, w, h);
		holder = eh;
   		GridBagLayout gbl = (GridBagLayout)getLayout();
		GridBagConstraints c = new GridBagConstraints();
		int eol = GridBagConstraints.REMAINDER;

		pitchCtrl = createPitchControl();
		add (pitchCtrl, eh, 0, gbl, c, 1, 5,5,2,2);
		velCtrl = createVelControl();
		add (velCtrl, eh, 0, gbl, c, eol, 5,2,5,2);
		durCtrl = createDurControl();
		add (durCtrl, eh, 1, gbl, c, 1, 2,5,2,1);
		chanCtrl = createChanControl();
		add (chanCtrl, eh, 0, gbl, c, eol, 2,2,5,1);
		setConstraints (c, 0, GridBagConstraints.NONE, GridBagConstraints.EAST,
							1,1, 0,0, 0,0,2,2);
		reset = new ResetButton (Define.ResetMsg);
		gbl.setConstraints (reset, c);
		reset.addObserver (this);
		add (reset);
	}
	protected EditControler createPitchControl () {
		EditControler e = new ExprControler (Define.pitchColor, Define.pitchButton, pitchMsg);
		e.setValue (0);
		return e;
	}
	protected EditControler createVelControl () {
		EditControler e = new ExprControler (Define.velColor, Define.velButton, velMsg);
		e.setValue (0);
		return e;
	}
	protected EditControler createDurControl () {
		EditControler e = new DurationControler (Define.durColor, Define.durButton, durMsg);
		e.setValue (1);
		return e;
	}
	protected EditControler createChanControl () {
		EditControler e = new ChanControler (Define.chanColor, Define.chanButton, chanMsg);
		e.setValue (0);
		return e;
	}
	protected void add (EditControler e, Observer o, int value, GridBagLayout gbl, GridBagConstraints c,
					int gridw, int top, int left, int right, int bottom ) {
		e.addObserver (o);
		setConstraints (c, gridw, GridBagConstraints.NONE, GridBagConstraints.CENTER,
							30,30, 1,1, top, left, right, bottom);
		gbl.setConstraints (e, c);
		addObserver (e);
		add (e);
	}
	public void addObserver (Observer obs) {
		notifier.addObserver (obs);
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
   		switch (mn.message()) {
			case Define.ResetMsg:		notifier.notifyObservers ("reset");
				break;
  		}
  	}
	public EditControler getChanCtrl() {return chanCtrl;}
	public EditControler getDurCtrl() {return durCtrl;}
	public EditControler getPitchCtrl() {return pitchCtrl;}
	public EditControler getVelCtrl() {return velCtrl;}
}
