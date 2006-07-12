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

public class TGlobals {
	public static TGlobalPrefs 		context = new TGlobalPrefs();
	//public static TEvaluator 		evaluator =  new TEvaluator();
	public static MidiAppl  	 	midiappl = null;
	public static TRealTimePlayer   player = null;
	//public static TExpMaker 		maker =  new TExpMaker2();
	//public static TExpRenderer 	renderer = new TExpRenderer();
	public static String 			appl = "ElodySharedAppl";
	
	static int midishareVersion = 121;
	static int ref = 0; // compteur du nombre d'appel de init et quit
	
	public static synchronized void init() {  // init g�re le nombre d'appel
	
		try {
			
			// Verifie la version des classes MidiShare
				
			if (Midi.Version < midishareVersion) {
				System.out.println ("---------------------------------------------------------");
				System.out.println ("The MidiShare classes version [" + Midi.Version + "]" + " are too old");
				System.out.println("Elody may not work correctly");
				System.out.println("Please update them!");
				System.out.println ("---------------------------------------------------------");
			}
			
			
			if (ref == 0) { // � faire une seule fois
			
				// Force les m�thodes finalize � �tre �x�cut�es
				//System.runFinalizersOnExit(true);
		
				
				// Lit le ficher de pr�f�rence
				context.readPrefs();
				
				TFileParser.registerParser(THTMLParser.class);
				TFileParser.registerParser(TMIDIFileParser.class);
				TFileParser.registerParser(TTEXTEParser.class);
				TFileParser.registerParser(TOBJECTParser.class);
				TFileParser.registerParser(TGUIDOParser.class);
				
				// Ouverture d'une application partag�e pour les taches et d'un Player partag�
				midiappl = new MidiAppl();
				player = new TRealTimePlayer();
				midiappl.Open(appl);
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
			midiappl.Close(); 
			player.stopPlayer();
			player.Close();
			context.writePrefs();
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

	public static String   version() {return  "1.053";}
}
