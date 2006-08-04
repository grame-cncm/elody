package grame.elody.editor.constructors;

import grame.elody.editor.controlers.MidiChanControlPanel;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.Singleton;
import grame.elody.util.fileselector.FileFilter;
import grame.elody.util.fileselector.FileSelector;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

public class MidiSender extends Singleton implements ActionListener, Observer {
	static final String saveCommand = TGlobals.getTranslation("Save");
	static final String loadCommand = TGlobals.getTranslation("Load");
	
	final int chanCount = 16;
	
	Button save, load;
	
	MidiChanControlPanel[] midiTable;
	
	public MidiSender (){	
		super (TGlobals.getTranslation("EventSender"));
		midiTable = new MidiChanControlPanel[16];
		save = CreateCommand ( saveCommand ) ;
		load  = CreateCommand ( loadCommand ) ;
	}
	
	public void update (Observable o, Object arg) {
		save.setEnabled(true);
	}
	
	private Button CreateCommand (String command) {
		Button button = new Button (command);
		button.setActionCommand (command);
		button.addActionListener (this);
		return button;
	}
	
	public void init(){
		Define.getButtons(this);
		setLayout(new BorderLayout());
		
		for(int i = 0; i<chanCount; i++) {
			midiTable[i] = new MidiChanControlPanel(i);
			midiTable[i].addObserver(this);
		}
		
		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(1 ,chanCount + 1, 5,5));
		
		for(int i = 0; i<chanCount; i++) {
			centerPanel.add(midiTable[i]); 
		}
		add("Center",centerPanel); 
		
		Panel bottomPanel = new Panel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		bottomPanel.add(load);
		bottomPanel.add(save);
		add("South",bottomPanel); 
		setSize (chanCount * 35, 400);
	}
	
	
	public void actionPerformed (ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals (saveCommand)) {
			saveState ();
		}
		else if (action.equals (loadCommand)) {
			loadState ();
		}
	}
	
	
	void loadFile (File file) {
		String  res;
		StringTokenizer st;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			for (int i = 0 ; i < chanCount ; i++) {
				res = in.readLine();
				st = new StringTokenizer(res, "/");
				st.nextToken(); // ProchChange
				midiTable[i].SetProgram(Integer.parseInt(st.nextToken()));
				st.nextToken(); // CtrlChange
				midiTable[i].SetVolume(Integer.parseInt(st.nextToken()));
				midiTable[i].SetPan(Integer.parseInt(st.nextToken()));
			}
			save.setEnabled(false);
			in.close();
		} catch (IOException e) { 
			System.err.println("MidiSender loadFile : " + e);
		}
	}
	
	
	void saveFile (File file) {
		try {
			PrintWriter out = new PrintWriter(new BufferedOutputStream ( new FileOutputStream(file)));
			for (int i = 0 ; i < chanCount ; i++) {
				out.println ("ProgChange/" + midiTable[i].GetProgram()
						+ "/CtrlChange/" + midiTable[i].GetVolume()
						+ "/" + midiTable[i].GetPan() );
			}
			out.close();
		} catch (IOException e) { 
			System.err.println("MidiSender saveFile : " + e);
		}
	}
	
	
	void loadState() {
		FileSelector fs = getSelector (FileDialog.LOAD);
		if (fs.select()) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			loadFile(fs.file());
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	
	void saveState() {
		FileSelector fs = getSelector (FileDialog.SAVE);
		if (fs.select()) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			saveFile(fs.file());
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	public FileSelector getSelector (int mode) {
		return new PatchFileSelector(getFrame(), "", "", mode);
	}
}

class PatchFileSelector extends FileSelector{	
	
	public FileSelector createInstance(Frame frame, String info, String file, int mode) {
		return  new PatchFileSelector (frame, info, file, mode);
	}
	
	public PatchFileSelector () {}
	
	public PatchFileSelector (Frame frame, String info, String file, int mode) {
		super (frame, info, file, mode);
	} 
	public boolean select () {
		String exts[] = { ".pch", ".patch", ".PCH", ".PATCH" };
		return select (new FileFilter (exts));
	}
}