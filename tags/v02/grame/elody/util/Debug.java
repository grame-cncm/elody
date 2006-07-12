package grame.elody.util;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Debug {
	public static boolean trace = false;
	
	static FileOutputStream outfile = null;
	public static PrintStream  log = null;
	
	public static void TraceOn() { trace = true;}
	public static void TraceOff() { trace = false;}
	
	public static void Trace(String mth, Object term) {
		if (trace) System.err.println (mth + " " + term);
	}
	
	/*
	 static {
	 	try {
	 		Date date = new Date();
			outfile = new FileOutputStream("access" + date.getDate() + "-" + date.getMonth() +"-" + date.getYear() + ".txt");
			log = new PrintStream(outfile);
		} catch (Exception e) { 
	 		System.out.println(e);
	 	} 
	 }
	 */
}
