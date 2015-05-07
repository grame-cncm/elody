/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.util;

import grame.midishare.Midi;

/*******************************************************************************************
*
*	 MidiDebug (classe) : utilitaires de debugging
* 
*******************************************************************************************/

public class MidiDebug {
	static int refnum;

	public static void init () {
		refnum = Midi.Open("Debug");
		MidiUtils.connect(refnum,"msExpander",1);
	}
	
	public static void quit () {Midi.Close(refnum);}
	
	
	static void sendEvent (int val) {
		int ev = Midi.NewEv(Midi.typeNote);
		if (ev != 0) {
			Midi.SetField(ev, 0, val%127);
			Midi.SetField(ev, 1, 127);
			Midi.SetField(ev, 2, 25);
			Midi.SendIm(refnum, ev);
		}
	}
}
