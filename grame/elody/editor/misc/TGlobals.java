package grame.elody.editor.misc;

import grame.elody.editor.player.TRealTimePlayer;
import grame.elody.file.parser.TFileParser;
import grame.elody.file.parser.TGUIDOParser;
import grame.elody.file.parser.THTMLParser;
import grame.elody.file.parser.TMIDIFileParser;
import grame.elody.file.parser.TOBJECTParser;
import grame.elody.file.parser.TTEXTEParser;
import grame.midishare.Midi;
import grame.midishare.MidiAppl;

import java.util.Locale;
import java.util.ResourceBundle;

public class TGlobals {
	public static TGlobalPrefs 		context = new TGlobalPrefs();
	//public static TEvaluator 		evaluator =  new TEvaluator();
	public static MidiAppl		 	midiappl = null;
	public static MidiApplAlarm 	midiapplThru = null;
	public static TRealTimePlayer   player = null;
	//public static TExpMaker 		maker =  new TExpMaker2();
	//public static TExpRenderer 	renderer = new TExpRenderer();
	public static String 			appl = "ElodySharedAppl";
	public static String 			thru = "ElodySharedApplThru";
	public static Locale locale = Locale.getDefault();
	public static ResourceBundle messages = ResourceBundle.getBundle("Elody", locale);
	
	
	static int midishareVersion = 121;
	static int ref = 0; // compteur du nombre d'appel de init et quit
	
	public static synchronized void init() {  // init gère le nombre d'appel
	
		try {
			
			// Verifie la version des classes MidiShare
				
			if (Midi.Version < midishareVersion) {
				System.out.println ("---------------------------------------------------------");
				System.out.println ("The MidiShare classes version [" + Midi.Version + "]" + " are too old");
				System.out.println("Elody may not work correctly");
				System.out.println("Please update them!");
				System.out.println ("---------------------------------------------------------");
			}
			
			
			if (ref == 0) { // à faire une seule fois
			
				// Force les méthodes finalize à être éxécutées
				//System.runFinalizersOnExit(true);
		
				
				// Lit le ficher de préférence
				context.readPrefs();
				
				TFileParser.registerParser(THTMLParser.class);
				TFileParser.registerParser(TMIDIFileParser.class);
				TFileParser.registerParser(TTEXTEParser.class);
				TFileParser.registerParser(TOBJECTParser.class);
				TFileParser.registerParser(TGUIDOParser.class);
				
				// Ouverture d'une application partagée pour les taches et d'un Player partagé
				midiappl = new MidiAppl();
				midiapplThru = new MidiApplAlarm();
				player = new TRealTimePlayer();
				midiappl.Open(appl);
				midiapplThru.Open(thru);
			//	Midi.Connect(0, midiappl.refnum,1);
				player.Open("ElodySharedPlayer");
				context.restoreConnections("ElodySharedPlayer");
				context.restoreAppletsState();
				
				/*
				Profiler.Init(100,10);
				Profiler.StartProfiling();
				*/
			}
			
			ref++;
		
		}catch (Exception e) {
			System.out.println (e);
			System.out.println ("---------------------------------------------------------");
			System.out.println ("Elody can not be launched");
			System.out.println ("Some ressources are not missing or not up to date");
			System.out.println ("Please re-install Elody");
			System.out.println ("---------------------------------------------------------");
			System.out.println(e);
		}
	}
	
	public static synchronized void quit() { 
		if (--ref == 0) {
			context.saveConnections("ElodySharedPlayer");
			context.saveAppletsState();
			midiapplThru.Close();
			midiappl.Close(); 
			player.stopPlayer();
			player.Close();
			context.writePrefs();
			midiapplThru = null;
			midiappl = null;
			player = null;
			/*
			Profiler.StopProfiling();
			Profiler.Dump("Elody.prof");
			Profiler.Terminate();
			*/
		}
	}
	
	// ferme l'appli MidiShare
	protected void finalize () { quit ();}
	
	public static void setLanguage (String lang)
	{
		if (lang.length()>0)
		{
			locale = new Locale(lang.toLowerCase());
			messages = ResourceBundle.getBundle("Elody", locale);
		}
	}
	
	public static String getTranslation (String s)
	{
		return messages.getString(s);
	}

	//public static String   version() {return  "1.053";}
}