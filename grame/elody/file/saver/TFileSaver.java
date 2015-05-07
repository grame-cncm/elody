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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

/*******************************************************************************************
*
*	 TFileSaver (classe) : classe générique utilisée par les clients
*
*    TFileSaver saver = new TFileSaver(); 
*    saver.writeFile(new TFileContent ("steph", "test", "description ", exp));
* 
*******************************************************************************************/

public final class TFileSaver {
	public TFileSaver () {}
	
	public void writeFile(TFileContent content,URL url, String format) throws Exception {
		System.out.println ("Not yet implemented");
	}
	
	public void writeFile(TFileContent content,File file, String format) throws Exception {
		TImpFileSaver saver =  (TImpFileSaver)Class.forName("grame.elody.file.saver."+ "T" + format + "Saver").newInstance();
		OutputStream out = new FileOutputStream(file.getAbsolutePath());
		saver.writeFile(content,out);
		out.close();
	}
	
	public void writeFile(TFileContent content, OutputStream out, String format) throws Exception {
		TImpFileSaver saver =  (TImpFileSaver)Class.forName("grame.elody.file.saver."+ "T" + format + "Saver").newInstance();
		saver.writeFile(content,out);
		out.flush();
	}
}
