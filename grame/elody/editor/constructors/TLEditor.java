/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors;

import grame.elody.editor.expressions.PlayExprHolder;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.player.TRealTimePlayer;
import grame.elody.editor.tleditor.TLMultiTracks;
import grame.elody.editor.tleditor.TLPane;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MidiUtils;
import grame.elody.util.MsgNotifier;
import grame.elody.util.fileselector.FileSelector;
import grame.midishare.MidiException;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

public class TLEditor extends BasicApplet implements Observer, ActionListener {
	static final String cmds[] = { TGlobals.getTranslation("Load"), TGlobals.getTranslation("Save"), TGlobals.getTranslation("Save_as") };
	static final int kLoad=0, kSave=1, kSaveAs=2;
	MsgNotifier fNotifier ;
	
	boolean fileBased, dirty; 
	String path;

	TLPane		fTLPane;
	Scrollbar	fVSB;
	Scrollbar	fHSB;
	Panel		fControlPanel;
	TextField	fName;
	PlayExprHolder 	fDisplayer;
	Button[] 		fButtons; 
	TRealTimePlayer fPlayer;
	String 			fPlayerName;
	
	public TLEditor () 
	{ 
		super(TGlobals.getTranslation("TimeLine_Editor"));
		fDisplayer = new PlayExprHolder(false);
		fNotifier = new MsgNotifier (2002);
		fVSB = new Scrollbar(Scrollbar.VERTICAL);
		fHSB = new Scrollbar(Scrollbar.HORIZONTAL);
		fControlPanel = new Panel(new BorderLayout());
		fControlPanel.add("Center", fName = new TextField());
		fPlayer = new TRealTimePlayer() ;
		fPlayerName = MidiUtils.availableName("ElodyTLPlayer");
		fTLPane = new TLPane(fHSB, fVSB, fNotifier, fControlPanel, fName, fPlayer);
		fTLPane.addObserver(this);
		fButtons = new Button[3];
		dirty = false;
	}
		
	public Insets getInsets () { return new Insets (0, 0, 0, 0); }
	
	
	 private Button CreateCommand (String command) 
	 {
    	Button button = new Button (command);
    	button.setActionCommand (command);
    	button.addActionListener (this);
    	return button;
    }
    
    public void start()
    {
		super.start();
		try {
		 	fPlayer.Open (fPlayerName);
		 	TGlobals.context.restoreConnections(fPlayerName);
		}catch (MidiException e) {
		 	System.err.println("TLEditor start : " + e);
		}
	}

	public  void stop()
	{
		fPlayer.stopPlayer ();
		TGlobals.context.saveConnections(fPlayerName);
		fPlayer.Close();
	}
    
	public void init()
	{
		setLayout(new BorderLayout());
		fButtons[kLoad] = CreateCommand (cmds[kLoad]);
		fButtons[kSave] = CreateCommand (cmds[kSave]);
		fButtons[kSaveAs] = CreateCommand (cmds[kSaveAs]);
		
		Panel editor = new Panel();
		editor.setLayout(new BorderLayout());
		
		fTLPane.setBackground(Color.white);
		editor.add("North", fControlPanel);
		editor.add("East", fVSB);
		editor.add("South", fHSB);
		editor.add("Center", fTLPane);
		
		Panel buttonspanel = new Panel();
		buttonspanel.add(fButtons[kLoad]);
    	buttonspanel.add(fButtons[kSave]);
    	buttonspanel.add(fButtons[kSaveAs]);
    	
		setSize(500, 200);
		moveFrame (100, 280);
				
		add("North",fDisplayer);
		add("Center",editor);
		//add("South", buttonspanel);
	}
	
	public void update (Observable o, Object arg) {
		MsgNotifier msg = (MsgNotifier)o;
  		if (msg == fNotifier) {
  			if (arg instanceof TExp) {
  				fDisplayer.setExpression ((TExp) arg);
  			}else {
  				if (!dirty) {
  					dirty = true;
  					enableCmd (kSave, true);
  				}
  			}
  		}
	}
	
	
	//-------------------------------------------------
    public void enableCmd (int command, boolean state) {
		if (!Define.appletMode) {
			Button button = fButtons [command];
			if (button != null)
				button.setEnabled(state);
		}
    }
	
	//-------------------------------------------------
    public void actionPerformed (ActionEvent e) {
    	String title = getFrame().getTitle();
    	String action = e.getActionCommand();
    	if (action.equals(cmds[kSave])) 	{ 
			if (fileBased) 	Save (title, path); 
			else 			SaveAs (title);
		}
		else if (action.equals(cmds[kSaveAs]))	{ SaveAs (title); }
		else if (action.equals(cmds[kLoad])) 	{ Load (); }
    }
    
     //-------------------------------------------------
     public void Save (String name, String path) {
   		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			File file = new File(path, name);
			FileOutputStream f = new FileOutputStream(file);
        	ObjectOutputStream  s  =  new  ObjectOutputStream(f);
       	 	s.writeObject(fTLPane.getFMultiTracks());
        	s.flush();
			fileBased = true;
  			enableCmd (kSave, dirty = false);
		} catch (Exception e) { System.err.println( "While saving " + name + " : " + e); } 
   		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    //-------------------------------------------------
    public void SaveAs (String file) {
    	FileSelector fs = new FileSelector (getFrame(), TGlobals.getTranslation("SaveAs_prompt"), file, FileDialog.SAVE);
    	if (fs.select ()) {
    		String fname = fs.getFile();
    		Save (fname, path = fs.getDirectory());
    		getFrame().setTitle(fname);
    	}
    }
    
    //-------------------------------------------------
    public void Load () {
     	FileSelector fs = new FileSelector (getFrame(),  TGlobals.getTranslation("Load_prompt"), "", FileDialog.LOAD);
    	if (fs.select ()) {
    		String fname = fs.getFile();
    		path = fs.getDirectory();
   			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try {
	 			File file =  new File (path, fname);
				FileInputStream f = new FileInputStream(file);
        		ObjectInputStream  s  =  new  ObjectInputStream(f);
       	 		TLMultiTracks tracks = (TLMultiTracks) s.readObject();
       	 		fTLPane.setMultiTracks(tracks);
       			fDisplayer.setExpression (TExpMaker.gExpMaker.createNull());
				
	    		getFrame().setTitle(fname);
				fileBased = true;
  				enableCmd (kSave, dirty = false);
			} catch (Exception e) {  System.err.println("While loading " + fname + " : " + e); } 
   			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	    }
    }
}