/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.treeeditor;

import java.awt.Color;

//===========================================================================
//ColorObserver : un "observateur" de couleurs
//===========================================================================

public interface ColorObserver {
	public void dropColor(Color c);
}
