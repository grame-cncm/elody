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

import grame.elody.editor.expressions.ExprHolderPanel;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.player.TSeqRTRecorder;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MidiUtils;
import grame.midishare.MidiException;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

public final class Recorder extends BasicApplet implements Observer {
	ExprHolderPanel[] holdTable;
 	TSeqRTRecorder record = null;
 	int size = 8;
 	String name;
 	
	public Recorder () {
		super (TGlobals.getTranslation("Recorder"));
		holdTable= new ExprHolderPanel[size];
	}
	
	public void start(){
	 	super.start();
	 	try {
	 		if (record == null) {
	     		record = new TSeqRTRecorder();
	     		name = MidiUtils.availableName("ElodyRecorder");
  				record.Open(name);
  				MidiUtils.connect("MidiShare",name,1);
  				TGlobals.context.restoreConnections(name);
  				record.addObserver(this);
  		   	 }
	   }catch (MidiException e) {
		 	System.err.println("Recorder start : " + e);
	    }
	}
		
	public void update (Observable o, Object arg) {
		for (int i =  holdTable.length - 1; i> 0; i--) { 
			holdTable[i].setHolder (holdTable[i-1].getHolder());
		}
		holdTable[0].setHolder((TExp)arg);
	}
	
	public void stop(){
 		if (record != null) {
 			TGlobals.context.saveConnections(name);
 			record.Close();
 			record = null;
 		}
 	}	
 	/*
	public  void  singleInit () {
		for (int i = 0; i<holdTable.length; i++) { 
			holdTable[i] = new ExprHolderPanel();
			add (holdTable[i]);
		}
		moveFrame (250, 240);
	}
	*/
	
	public  void  init () {
		setSize(size *  50, 60);
		setLayout (new GridLayout(1,size,3,3));
		for (int i = 0; i<holdTable.length; i++) { 
			holdTable[i] = new ExprHolderPanel();
			add (holdTable[i]);
		}
		moveFrame (250, 240);
	}
}
