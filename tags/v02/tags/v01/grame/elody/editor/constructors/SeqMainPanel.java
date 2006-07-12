package grame.elody.editor.constructors;

import grame.elody.editor.constructors.parametrer.ParamPanel;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.misc.Define;

import java.awt.Color;

public class SeqMainPanel extends ParamPanel {
	public SeqMainPanel () {
		super ();
		borderColor = Color.red;
	}
	public void init (ExprHolder eh, int w, int h) {
		super.init (eh, w, h);
		int msg = Define.ShiftControlMsg;
		pitchCtrl.setMessage (msg);
		velCtrl.setMessage (msg);
		durCtrl.setMessage (msg);
		chanCtrl.setMessage (msg);
	}
	public void addStepObserver (ParamPanel step) {
		pitchCtrl.addObserver 	(step.getPitchCtrl());
		velCtrl.addObserver 	(step.getVelCtrl());
		durCtrl.addObserver 	(step.getDurCtrl());
		chanCtrl.addObserver 	(step.getChanCtrl());
		addObserver (step);	
	}
}
