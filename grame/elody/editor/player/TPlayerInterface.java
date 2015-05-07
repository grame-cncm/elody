/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.player;

import grame.elody.lang.texpression.expressions.TExp;

public interface TPlayerInterface {
	 
 	public void setSynchroPlayer(int synchro); 
 	public void startPlayer(TExp exp);
 	public void insertPlayer(TExp exp); // ins?re à la position courante
	public void stopPlayer(); 
	public void contPlayer();
	public void freePlayer(); 
	public void setPosMsPlayer(int date_ms); 
	public void setBufferPlayer(int val );
	
}
