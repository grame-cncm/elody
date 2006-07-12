package grame.elody.editor.constructors;

import grame.elody.editor.constructors.document.NamedExprHolder;
import grame.elody.editor.constructors.document.NamedExprPanel;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.file.parser.TFileContent;
import grame.elody.file.parser.TFileParser;
import grame.elody.file.saver.TFileSaver;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TArrayExp;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MsgNotifier;
import grame.elody.util.fileselector.HTMLFileSelector;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Vector;

public class Stock extends BasicApplet implements ActionListener {
	ExprHolder [] expHolders; CmdsPanel cmdsPanel;
	boolean fileBased, dirty; String path;
	static int nRows = 5, nColumns = 7;
	static String cmds[] = { "Load", "Save", "Save as", "Clear" };
	static final int kLoad=0, kSave=1, kSaveAs=2, kClear=3;
	 
	//-------------------------------------------------
	public Stock() {
		super("Untitled");
		setLayout(new BorderLayout(4,4));
		setSize (nColumns*65, nRows*70);
		expHolders = new ExprHolder[nRows * nColumns];
		dirty = false;
	}
  
	//-------------------------------------------------
   
  
    public void init() {
    	Container center = new Panel ();
    	add ("Center", center);
		center.setLayout(new GridLayout(nRows, nColumns, 5, 5));
		for (int i=0, n=nRows*nColumns; i < n; i++) {
			NamedExprHolder eh = new NamedExprHolder (null, new TNotesVisitor(), true);
			eh.addObserver (this);
			center.add (new NamedExprPanel (eh));
			expHolders[i] = eh;
		}
		if (!Define.appletMode) {
    		add ("South", cmdsPanel = new CmdsPanel(cmds, this));
		}
		moveFrame (80, 130);
	}   
	
	//-------------------------------------------------
    public void enableCmd (int command, boolean state) {
		if (!Define.appletMode) {
			Button button = cmdsPanel.getButton (cmds[command]);
			if (button != null)
				button.setEnabled(state);
		}
    }

	//-------------------------------------------------
	public void update (Observable o, Object arg) {
  		MsgNotifier msg = (MsgNotifier)o;
  		if (msg.message() == Define.ExprHolderMsg) {
  			if (!dirty) {
  				dirty = true;
  				enableCmd (kSave, true);
  			}
  		}
	}

	//-------------------------------------------------
    public void Clear () {
    	for (int i=0, n = nRows * nColumns; i<n; i++) {
    		expHolders[i].setExpression (TExpMaker.gExpMaker.createNull());
    	}
    }
	//-------------------------------------------------
    public void Save (String name, String path) {
   		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			File file = new File(path, name);
			Vector v = new Vector ();
    		for (int i=0, n = nRows * nColumns; i<n; i++)
				v.addElement (expHolders[i].getExpression ());
			TArrayExp ae = new TArrayExp (v);
			TFileSaver saver = new TFileSaver();
			saver.writeFile (new TFileContent("","","",ae), file, "HTML");
			fileBased = true;
  			enableCmd (kSave, dirty = false);
		} catch (Exception e) { System.err.println( "While saving " + name + " : " + e); } 
   		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
	//-------------------------------------------------
    public void SaveAs (String file) {
    	HTMLFileSelector fs = new HTMLFileSelector (getFrame(), "Enter the file name:", file, FileDialog.SAVE);
    	if (fs.select ()) {
    		String fname = fs.getFile();
    		Save (fname, path = fs.getDirectory());
    		getFrame().setTitle(fname);
    	}
    }
	//-------------------------------------------------
    public void Load () {
     	HTMLFileSelector fs = new HTMLFileSelector (getFrame(),  "Select a file:", "", FileDialog.LOAD);
    	if (fs.select ()) {
    		String fname = fs.getFile();
    		path = fs.getDirectory();
   			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try {
	 			File file =  new File (path, fname);
				TExp exp; int i=0, n = nRows * nColumns;
				TFileParser parser = new TFileParser();
				TExp  res =  parser.readFile(file).getExp();	
				
				if (res instanceof TArrayExp) { // cas d'une expression ARRAY
					Enumeration e = ((TArrayExp)res).getArray().elements();
					for (; e.hasMoreElements() && (i<n); i++) {
						exp = (TExp)e.nextElement ();
						if (exp!=null) expHolders[i].setExpression (exp);
						else break;
					}
	    			for (; i<n; i++)
						expHolders[i].setExpression (TExpMaker.gExpMaker.createNull());
				}else{   // cas d'une expression simple
					expHolders[0].setExpression (res);
				}
				
	    		getFrame().setTitle(fname);
				fileBased = true;
  				enableCmd (kSave, dirty = false);
			} catch (Exception e) {  System.err.println("While loading " + fname + " : " + e); } 
   			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	    }
    }
	//-------------------------------------------------
    public void actionPerformed (ActionEvent e) {
    	String title = getFrame().getTitle();
    	String action = e.getActionCommand();
    	if (action.equals(cmds[kClear])) {
			Clear();
    	}
		else if (action.equals(cmds[kSave])) 	{ 
			if (fileBased) 	Save (title, path); 
			else 			SaveAs (title);
		}
		else if (action.equals(cmds[kSaveAs]))	{ SaveAs (title); }
		else if (action.equals(cmds[kLoad])) 	{ Load (); }
    }
}

class CmdsPanel extends Panel
{
	Button [] button;
	
	public CmdsPanel ( String [] cmds, ActionListener listener) {
		setLayout(new GridLayout(1, cmds.length, 5, 5));
		setFont (new Font("Times", Font.PLAIN, 12));
		button = new Button [cmds.length];
		for (int i=0, n= cmds.length; i<n; i++) {
			button[i] = new Button (cmds[i]);
     		button[i].setActionCommand (cmds[i]);
     		button[i].addActionListener (listener);
			add (button[i]);
		}
	}
	public Button getButton (String name) {
		for (int i=0, n= button.length; i<n; i++) {
			if (button[i].getLabel().equals (name))
				return button[i];
		}
		return null;
	}
}
