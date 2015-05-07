/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.text.saver;

import grame.elody.lang.texpression.expressions.TExp;

import java.io.BufferedWriter;
import java.io.Writer;


/*******************************************************************************************
*
*	 TTextExpSaver (classe) : le saver TEXT (interne)
* 
*******************************************************************************************/

public final class TTextExpSaver {
	TTextExpWriter writer;
	
	public TTextExpSaver (Writer out) { writer = new TTextExpWriter(new BufferedWriter(out));}
	public void writeFileHeader() {writer.writeFileHeader();}
	public void writeTitle(String title) {writer.writeTitle(title);}
	public void writeAuthor(String author) { writer.writeAuthor(author);}
	public void writeDescription(String dec) {writer.writeDescription(dec);}
	public void writeFileEnd() {writer.writeFileEnd();}
	public void writeExp(TExp exp) {exp.Accept(writer,null);}
}
