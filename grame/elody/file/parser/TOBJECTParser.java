/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.parser;

import grame.elody.file.guido.parser.BGuidoParser;
import grame.elody.file.guido.parser.BasicGuidoReader;

import java.io.InputStream;
import java.io.ObjectInputStream;

/*******************************************************************************************
*
*	 TOBJECTParser (classe) : le parser OBJECT (utilisation de la serialisation)
* 
*******************************************************************************************/

public final class TOBJECTParser implements TImpFileParser {
	BGuidoParser parser;
	BasicGuidoReader reader;
	
	public TFileContent readFile(InputStream input) throws Exception{ 
		ObjectInputStream s = new ObjectInputStream(input);
		return (TFileContent)s.readObject(); 
	}
}
