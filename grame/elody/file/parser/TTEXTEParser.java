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

import grame.elody.file.text.parser.TTextParser;
import grame.elody.lang.texpression.expressions.TExp;

import java.io.InputStream;

/*******************************************************************************************
*
*	 TTEXTParser (classe) : le parser de TEXTE
* 
*******************************************************************************************/

public final class TTEXTEParser implements TImpFileParser {
	TTextParser parser;
	TExp res;
	
	public TFileContent readFile(InputStream input) throws Exception{ 
		parser = new TTextParser(input);
		res = parser.readTextFile();
		return new TFileContent ("","","",res);
	}
	/*
	public TFileContent readFile(InputStream input) throws Exception{ 
		System.out.println("Not yet available");
		return new TFileContent ("","","",TExpMaker.gExpMaker.createNull());
	}
	*/
}
