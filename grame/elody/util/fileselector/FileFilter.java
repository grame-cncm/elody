/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.util.fileselector;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter implements FilenameFilter
{	
	String [] acceptedExtensions;
	
	public FileFilter (String [] ext) {
		acceptedExtensions = ext;
	} 
 	public boolean accept (File dir, String name) {
		for (int i=0; i<acceptedExtensions.length; i++) {
 			if (name.endsWith (acceptedExtensions[i]))
 				return true;
 		}
 		return false;
 	}
}
