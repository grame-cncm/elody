/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
