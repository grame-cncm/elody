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

import grame.elody.file.html.saver.TExpSaver;
import grame.elody.file.parser.TFileContent;

import java.io.OutputStream;

/*******************************************************************************************
*
*	 THTMLSaver (classe) : le saver HTML
* 
*******************************************************************************************/

public final class THTMLSaver implements TImpFileSaver {
	public THTMLSaver () {}
	
	public void writeFile(TFileContent content, OutputStream out) throws Exception {
		TExpSaver saver = new TExpSaver (out);
		saver.writeFileHeader();
		saver.writePlayerApplet();
		saver.writeTitle (content.title);
		saver.writeAuthor (content.author);
		saver.writeDescription (content.description);
		saver.writeExp(content.exp);
		saver.writeFileEnd();
		
	}
}
