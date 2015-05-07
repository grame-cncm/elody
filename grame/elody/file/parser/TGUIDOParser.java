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
import grame.elody.file.guido.parser.TBasicGuidoReader;
import grame.elody.lang.texpression.expressions.TExp;

import java.io.InputStream;

/*******************************************************************************************
*
*	 TGUIDOParser (classe) : le parser GUIDO
* 
*******************************************************************************************/

public final class TGUIDOParser implements TImpFileParser {
	BGuidoParser parser;
	BasicGuidoReader reader;
	
	public TFileContent readFile(InputStream input) throws Exception{ 
		parser = new BGuidoParser(input);
		reader = new TBasicGuidoReader();
		parser.readGuidoFile(reader);
		return new TFileContent ("","","",(TExp)reader.getExp());
	}
}
