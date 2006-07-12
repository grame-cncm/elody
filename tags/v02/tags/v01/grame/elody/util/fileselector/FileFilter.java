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
