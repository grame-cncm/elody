package grame.elody.util;

import grame.midishare.Midi;

/*******************************************************************************************
*
*	 MidiUtils (classe) :utilitaires Midi
* 
*******************************************************************************************/

public final class MidiUtils {

	public static boolean connect(int ref1, String name, int state) {
		int ref2;
		if ((ref2 = Midi.GetNamedAppl(name)) >= 0) {
			Midi.Connect(ref1, ref2, state);
			return true;
		}else
			return false;
	}
	
	public static boolean connect(String name1, String name2, int state) {
		int ref1 = Midi.GetNamedAppl(name1);
		int ref2 = Midi.GetNamedAppl(name2);
		if ((ref2 >= 0) && (ref1 >= 0)) {
			Midi.Connect(ref1, ref2, state);
			return true;
		}else
			return false;
	}
	
	public static boolean isConnected(String name1, String name2) {
		int ref1 = Midi.GetNamedAppl(name1);
		int ref2 = Midi.GetNamedAppl(name2);
		return  (Midi.IsConnected(ref1,ref2) == 1) ? true :false;
	}
	
	public static int length(int seq) {
		int cur = Midi.GetFirstEv(seq);
		int count = 0;
		
		while (cur != 0) {
			count++;
			cur = Midi.GetLink(cur);
		}
		return count;
	}
	
	public static	String availableName(String name) {
		int num = Midi.CountAppls();
		for (int i = 0 ; i< num; i++) {
			if (Midi.GetNamedAppl(name+(i+1)) < 0) return name + (i+1);
		}
		return name + num;
	}
}
