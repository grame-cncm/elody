/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.expressions;

import grame.elody.lang.texpression.expressions.TExp;
import grame.midishare.Midi;

public class DelayedExprHolder extends ExprHolder implements Runnable {
	private Thread changeThread;
	private long timeout, delay;

	public DelayedExprHolder (TExp e, TGraphVisitor v, boolean accept, long delay) {
		super (e, v, accept);
		this.delay = delay; 
	}
	public void changed () {
		uptodate = false;
		timeout = Midi.GetTime() + delay;
		if (changeThread == null) {
			changeThread = new Thread (this);
			changeThread.start ();
		}
	}
	public void run () {
		while (true) {
			long toWait = timeout - Midi.GetTime();
			if (toWait > 0) {
				try { Thread.sleep (toWait); }
				catch (Exception e) { System.err.println( "DelayedExprHolder run : " + e); break; }
			}
			else break;
		}
		changeThread = null;
		super.changed ();
	}
	public TExp getExpression() {
		if (changeThread != null) {
			timeout = 0;
			try { 
				changeThread.resume(); 
				changeThread.join(); 
			}
			catch (Exception e) {}
		}
		return super.getExpression();
	}
}
