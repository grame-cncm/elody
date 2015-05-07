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



import grame.elody.file.html.parser.TBasicHtmlParser;

import java.io.InputStream;

/*******************************************************************************************
*
*	 THTMLParser (classe) : le parser HTML
* 
*******************************************************************************************/

public final class THTMLParser implements TImpFileParser{
	TBasicHtmlParser parser ;
	
	public TFileContent readFile(InputStream input) throws Exception{ 
		parser = TBasicHtmlParser.readFileHeader(input); // alloue le parser correspondant à la version du fichier
		return parser.readFileContent();
	}
}

