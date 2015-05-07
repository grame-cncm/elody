/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.util.fileselector;

import java.awt.Frame;

public class GUIDOFileSelector extends FileSelector {
	public FileSelector createInstance(Frame frame, String info, String file, int mode) {
	 	return  new GUIDOFileSelector (frame, info, file, mode);
	}
	
	public GUIDOFileSelector () {}
	
	public GUIDOFileSelector (Frame frame, String info, String file, int mode) {
    	super (frame, info, file, mode);
	} 
	public boolean select () {
		String exts[] = { ".gmn", ".GMN" };
		return select (new FileFilter (exts));
	}
}
