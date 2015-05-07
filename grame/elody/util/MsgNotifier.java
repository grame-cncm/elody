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

import java.util.Observable;

public class MsgNotifier extends Observable {
	/*public*/ protected int msgValue;
	
	public MsgNotifier(int msg) { msgValue = msg; }
	public int message() 		{ return msgValue; }
	public void setMessage(int msg) 		{ msgValue = msg; }
	public void notifyObservers (Object o) {
		setChanged();
		super.notifyObservers (o);
	}
}
