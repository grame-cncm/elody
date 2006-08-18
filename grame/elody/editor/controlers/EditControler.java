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
	    			ctrl.setValue (new Integer((String)arg).intValue());
	    		}
	    		catch (Exception e) {
	    			edit.setText (new Integer(getValue()).toString());
	    		}
  				break;
  			case Define.BarControlerMsg:
    			edit.setText (((Integer)arg).toString());
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
    public void setRange (int min, int max, int newValue, int home) 
    										{ ctrl.setRange (min, max, newValue, home); }
    public void	shiftValue (int v) 			{ ctrl.shiftValue (v); }
    public int 	getValue () 				{ return ctrl.getValue(); }
    public void addObserver (Observer o) 	{ notifier.addObserver (o); }
    public void notifyObservers () 			{ notifier.notifyObservers (new Integer(getValue()));}
    public void setMessage (int newMsg) 	{ notifier.setMessage(newMsg); }
    public int  getDirection () 			{ return ctrl.getDirection(); }
}
