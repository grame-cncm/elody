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
