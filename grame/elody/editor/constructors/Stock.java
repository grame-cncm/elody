package grame.elody.editor.constructors;

import grame.elody.editor.constructors.document.NamedExprHolder;
import grame.elody.editor.constructors.document.NamedExprPanel;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
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
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Vector;

public class Stock extends BasicApplet implements ActionListener {
	ExprHolder [] expHolders; CmdsPanel cmdsPanel;
	boolean fileBased, dirty; String path;
	static int nRows = 5, nColumns = 7;
	static String cmds[] = { TGlobals.getTranslation("Load"), TGlobals.getTranslation("Save"),
		TGlobals.getTranslation("Save_as"), TGlobals.getTranslation("Clear") };
	static final int kLoad=0, kSave=1, kSaveAs=2, kClear=3;
	 
	//-------------------------------------------------
	public Stock() {
		super(TGlobals.getTranslation("Untitled"));
		setLayout(new BorderLayout(4,4));
		setSize (nColumns*65, nRows*70);
		expHolders = new ExprHolder[nRows * nColumns];
		dirty = false;
	}
  
	//-------------------------------------------------
   
  
    public void init() {
    	Container center = new Panel ();
    	WindowListener[] l = getFrame().getWindowListeners();
    	getFrame().removeWindowListener(l[0]);
    	getFrame().addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				if (dirty)
					new ConfirmDialog(getFrame(), getThis());
				else
					getFrame().dispose();
			} 		
    	});
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
    
    public Stock getThis()	{ return this; }
	
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
  				String title = getFrame().getTitle();
  				title += "*";
  				getFrame().setTitle(title);
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
			Vector<TExp> v = new Vector<TExp> ();
    		for (int i=0, n = nRows * nColumns; i<n; i++)
				v.addElement (expHolders[i].getExpression ());
			TArrayExp ae = new TArrayExp (v);
			TFileSaver saver = new TFileSaver();
			saver.writeFile (new TFileContent("","","",ae), file, "HTML");
			fileBased = true;
			enableCmd (kSave, dirty = false);
			getFrame().setTitle(name);
		} catch (Exception e) { System.err.println( "While saving " + name + " : " + e); } 
   		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
	//-------------------------------------------------
    public void SaveAs (String file) {
    	HTMLFileSelector fs = new HTMLFileSelector (getFrame(), TGlobals.getTranslation("SaveAs_prompt"), file, FileDialog.SAVE);
    	if (fs.select ()) {
    		String fname = fs.getFile();
    		Save (fname, path = fs.getDirectory());
    	}
    }
	//-------------------------------------------------
    public void Load () {
     	HTMLFileSelector fs = new HTMLFileSelector (getFrame(),  TGlobals.getTranslation("Load_prompt"), "", FileDialog.LOAD);
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
					Enumeration<TExp> e = ((TArrayExp)res).getArray().elements();
					for (; e.hasMoreElements() && (i<n); i++) {
						exp = e.nextElement();
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
		if (title.charAt(title.length()-1)=='*')
		title = title.substring(0, title.length()-1);
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

class ConfirmDialog extends Dialog
{
	public ConfirmDialog(Frame owner, final Stock s) {
		super(owner, "Elody", true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { dispose(); }
		});
		
		setSize(280, 80);
		setResizable(false);
		setBackground(Color.white);
		Rectangle r = owner.getBounds();
		setLocation((int)(r.x+(r.width-280)/2), (int)(r.y+(r.height-130)/2));
		setLayout(new BorderLayout());
		Panel p = new Panel(new GridLayout(1,3,10,10));
		Button yesBtn = new Button(TGlobals.getTranslation("Yes"));
		yesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			   	String title = s.getFrame().getTitle();
				if (title.charAt(title.length()-1)=='*')
				title = title.substring(0, title.length()-1);
				s.SaveAs(title);
				s.getFrame().dispose();
				dispose();
			}	
		});
		p.add(yesBtn);
		Button noBtn = new Button(TGlobals.getTranslation("No"));
		noBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				s.getFrame().dispose();
				dispose();
			}	
		});
		p.add(noBtn);
		Button cancelBtn = new Button(TGlobals.getTranslation("Cancel"));
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}	
		});
		p.add(cancelBtn);
		add(new Label(TGlobals.getTranslation("wanna_save"), Label.CENTER), BorderLayout.NORTH);
		add(p, BorderLayout.SOUTH);
	    setVisible(true);
	}
}