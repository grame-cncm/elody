/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.util;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class TLogfile {
	static FileOutputStream outfile = null;
	static PrintStream  accesslog = null;

	public static void printMessage(Object mes) {
		System.out.println(mes);
		accesslog.println(mes); 
	}
	
	public static void printCurdate() {
		Calendar calendar = Calendar.getInstance(); 
		String sdate = "Date = " + calendar.get(Calendar.HOUR_OF_DAY) + " h "+ calendar.get(Calendar.MINUTE) 
			+ " min "+ calendar.get(Calendar.SECOND) + " sec";
		System.out.println(sdate);
		accesslog.println(sdate);
	}
	
	public static void close (){
	 	try {
			if (outfile !=  null) outfile.close();
     		if (accesslog !=  null) accesslog.close();
		} catch (Exception e) { 
	 		System.out.println(e);
	 	} 
     } 
	
	 static {
	 	try {
	 		Calendar calendar = Calendar.getInstance();
			outfile = new FileOutputStream("ElodyServer[" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) +"-" + (calendar.get(Calendar.YEAR) - 1900) + "].log");
			accesslog = new PrintStream(outfile);
		} catch (Exception e) { 
	 		System.out.println(e);
	 	} 
	 }
}
