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

import grame.elody.file.guido.saver.TGuidoExpSaver;
import grame.elody.file.parser.TFileContent;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

/*******************************************************************************************
*
*	 TGUIDOSaver (classe) : le saver GUIDO
* 
*******************************************************************************************/

public final class TGUIDOSaver implements TImpFileSaver {
	public TGUIDOSaver () {}
	
	public void writeFile(TFileContent content,OutputStream out) throws Exception{
	
		TGuidoExpSaver saver = new TGuidoExpSaver (new OutputStreamWriter(out));
		saver.writeFileHeader();
		saver.writeTitle (content.title);
		saver.writeAuthor (content.author);
		saver.writeDescription (content.description);
		saver.writeExp(content.exp);
		saver.writeFileEnd();
	}
}
