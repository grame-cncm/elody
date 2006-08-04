package grame.elody.editor.controlers;

import grame.elody.editor.misc.Define;
import grame.elody.util.MsgNotifier;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observer;

public class TextBarField extends TextField implements ActionListener,
		FocusListener {
	MsgNotifier notifier;
	String previous;
	
	public TextBarField (String text, int col) {
		super (text, col);
		initialize();
	}
	public TextBarField (String text) {
		super (text);
		initialize();
	}
	public TextBarField (int col) {
		super (col);
		initialize();
	}
    public void initialize () {
		notifier = new MsgNotifier(Define.TextBarFieldMsg);
		previous = new String ("");
     	addActionListener (this);
    	addFocusListener (this);
    }
	public synchronized void setText (String text) {
    	previous = text;
    	super.setText (text);
	}
    public void actionPerformed (ActionEvent e) {
    	if (e.getID() == ActionEvent.ACTION_PERFORMED) {
    		notifyObservers (getText());
    	}
    }
    public void focusGained(FocusEvent e) {
		selectAll();
    }
    public void focusLost(FocusEvent e) {
		select(0, 0);
    	notifyObservers (getText());
    }
    public void notifyObservers	 (String text) { 
    	if (!text.equals (previous)) {
    		previous = text;
    		notifier.notifyObservers (text);
    	}
    }
    public void addObserver	 (Observer o) { notifier.addObserver(o); }
    public void deleteObserver(Observer o) { notifier.deleteObserver(o); }
}
