/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.saver;

import grame.elody.file.parser.TFileContent;

import java.io.ObjectOutputStream;
import java.io.OutputStream;

/*******************************************************************************************
*
*	 TOBJECTSaver (classe) : le saver OBJECT (utilisation de la serialisation)
* 
*******************************************************************************************/

public final class TOBJECTSaver implements TImpFileSaver {
	public TOBJECTSaver () {}
	
	public void writeFile(TFileContent content,OutputStream outstream) throws Exception{
		ObjectOutputStream s = new ObjectOutputStream(outstream);
		s.writeObject(content);
		s.flush();
	}
}
