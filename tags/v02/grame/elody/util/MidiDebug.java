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
