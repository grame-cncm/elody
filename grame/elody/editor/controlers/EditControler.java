/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.controlers;

import grame.elody.editor.misc.Define;
import grame.elody.util.MsgNotifier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextField;
import java.util.Observable;
import java.util.Observer;

public class EditControler extends Panel implements Observer {

	protected boolean displayAbs;
	protected TextBarField edit;
	protected TextBarField editAbs;
	protected int absRef = 0;
	protected Controler ctrl;
	protected MsgNotifier notifier;
	
	public EditControler (Controler ctrl, int cols) {
		this.displayAbs = false;
		construct(ctrl, cols);
	}
	public EditControler (Controler ctrl, int cols, boolean displayAbs) {
		this.displayAbs = displayAbs;
		construct(ctrl, cols);
	}
	
	private void construct(Controler ctrl, int cols)
	{
		this.ctrl = ctrl;
		notifier = new MsgNotifier (Define.EditBarControlerMsg);
		edit = new TextBarField (cols);
		edit.setBackground (Color.white);
		edit.setFont (new Font("Times", Font.PLAIN, 10));
		edit.addObserver (this);
		if (displayAbs)
		{
			editAbs= new TextBarField (cols);
			editAbs.setBackground (Color.white);
			editAbs.setFont (new Font("Times", Font.PLAIN, 10));
			editAbs.addObserver (this);
		}
		ctrl.addObserver (this);
		setLayout (new BorderLayout (2,2));
		Panel p = new Panel ();
		if (ctrl.getDirection() == Controler.kVertical) {
			p.setLayout (new FlowLayout (FlowLayout.CENTER, 0,0));
			p.add (edit);
			if (displayAbs)	p.add (editAbs);	
			add ("North", p);
			add ("Center", ctrl);
		} else {
			p.setLayout (new BorderLayout (2,2));
			p.add (edit);
			if (displayAbs)	p.add (editAbs);
			add ("Center", ctrl);
			add ("East", p);
		}
	}

  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		switch (mn.message()) {
  			case Define.TextBarFieldMsg:
  				try {
  					if ( ctrl.getValue()==Integer.valueOf(edit.getText()).intValue() )
  					{
  						int v = Integer.valueOf(editAbs.getText()).intValue()-absRef;
  						ctrl.setValue(v);
  					}
  					else
  						ctrl.setValue (new Integer((String)arg).intValue());
	    		}
	    		catch (Exception e) {
	    			edit.setText (new Integer(getValue()).toString());
	    		}
  				break;
  			case Define.BarControlerMsg:
    			int nArg = ((Integer)arg).intValue();
    			edit.setText(String.valueOf(nArg));
  				if (displayAbs&&(absRef!=-1))
  					editAbs.setText (String.valueOf(nArg+absRef));					
  				break;
  			case Define.ShiftControlMsg:
    			shiftValue (((Integer)arg).intValue());
  				break;
  			case Define.SetControlMsg:
    			ctrl.setValue (((Integer)arg).intValue());
  				break;
  		}
     	notifyObservers ();
  	}
    public void setValue (int v) {
    	ctrl.setValue(v);
    	edit.setText (new Integer(ctrl.getValue()).toString());
     	notifyObservers ();
   }
    public void initAbsValue (int v) {
    	if (v!=-1)
    	{
    		absRef = v-ctrl.getValue();
    		editAbs.setText(String.valueOf(v));
    		editAbs.setEnabled(true);
    	}
    	else
    	{
    		absRef = -1;
    		editAbs.setText("");
    		editAbs.setEnabled(false);
    	}
    }
    
    public void setRange (int min, int max, int newValue, int home) 
    										{ ctrl.setRange (min, max, newValue, home); }
    public void	shiftValue (int v) 			{ ctrl.shiftValue (v); }
    public int 	getValue () 				{ return ctrl.getValue(); }
    public void addObserver (Observer o) 	{ notifier.addObserver (o); }
    public void notifyObservers () 			{ notifier.notifyObservers (new Integer(getValue()));}
    public void setMessage (int newMsg) 	{ notifier.setMessage(newMsg); }
    public int  getDirection () 			{ return ctrl.getDirection(); }
}
