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

/*******************************************************************************************
*
*	 ElodyFileFormatException (classe) : exception lancée eventuellement lors du parsing
*                                        en cas de non reconnaissance d'un format
* 
*******************************************************************************************/

public final class ElodyFileFormatException extends Exception {
	   /**
     * Constructor.
     */
    public ElodyFileFormatException() {
		super();
    }

    /**
     * Constructor with error number.
     */
    public ElodyFileFormatException(int rc) {
		super("ElodyFileFormatException error: " + rc);
    }

    /**
     * Constructor with string.
     */
    public ElodyFileFormatException(String string) {
		super("ElodyFileFormatException error: " + string);
    }
}
