package grame.elody.editor.constructors;

import grame.elody.editor.expressions.ExprHolderPanel;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.Persistent;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

public class HistoryEditor extends Persistent implements Observer {
	ExprHolderPanel[] holdTable;
	
	public HistoryEditor () {
		super (TGlobals.getTranslation("History"));
		int size = 8;
		setLayout (new GridLayout(size, 1, 0, 2));
		holdTable= new ExprHolderPanel[size];
		TExpMaker.gExpMaker.addObserver(this);
	}
	public  void init () {
		for (int i = 0; i<holdTable.length; i++) { 
			holdTable[i] = new ExprHolderPanel();
			add (holdTable[i]);
		}
		setSize (64, 340);
		moveFrame (250, 240);
	}
	public void update (Observable o, Object arg) {
		for (int i =  holdTable.length - 1; i> 0; i--) { 
			holdTable[i].setHolder (holdTable[i-1].getHolder());
		}
		holdTable[0].setHolder((TExp)arg);
	}
	
	public void stop () { TExpMaker.gExpMaker.deleteObserver(this);}
}
