package grame.elody.editor.constructors;

import grame.elody.editor.misc.TGlobals;
import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiTask;

public final class DisplayTask extends MidiTask {
	PlayerPanel5 player;

	DisplayTask(PlayerPanel5 player) {
		this.player = player;
	}

	public void Execute(MidiAppl appl, int date) {
		if (player.updateState()) { // player toujours en marche
			TGlobals.midiappl.ScheduleTask(this, Math.max(Midi.GetTime(),
					date + 500));
		} else
			player.clearTask();
	}
}
