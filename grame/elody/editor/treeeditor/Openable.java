/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.treeeditor;

//===========================================================================
//Openable : 	l'interface que doit implementer un objet qui peut être 
//				ouvert ou fermé par un bouton
//===========================================================================

public interface Openable {
	public void open();
	public void close();
}
