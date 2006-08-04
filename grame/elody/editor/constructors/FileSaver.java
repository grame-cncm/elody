package grame.elody.editor.constructors;

import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.file.parser.TFileContent;
import grame.elody.file.parser.TFileParser;
import grame.elody.file.saver.TFileSaver;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TUrlExp;
import grame.elody.util.PixelBorder;
import grame.elody.util.fileselector.FileSelector;
import grame.elody.util.fileselector.GUIDOFileSelector;
import grame.elody.util.fileselector.HTMLFileSelector;
import grame.elody.util.fileselector.MIDIFileSelector;
import grame.elody.util.fileselector.OBJECTSelector;
import grame.elody.util.fileselector.TEXTSelector;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

public final class FileSaver extends BasicApplet implements ActionListener {
	static String saveCommand = TGlobals.getTranslation("Save");
	static String loadCommand = TGlobals.getTranslation("Load");
	static String fetchCommand = TGlobals.getTranslation("Fetch");
	static String url = "http://java.grame.fr/Elody/Contributions/Contrib0.html";
 
	FileOutputStream outfile = null;
	FileInputStream infile = null;
	Button save,load,fetch;
   
	TextField filename;
	Choice  filetype;
	ExprHolder  displayer;
 
  	public FileSaver (){	
  		super (TGlobals.getTranslation("File_Manager"));
  		displayer = new ExprHolder(null, new TNotesVisitor(),true);
  	}
  	
    private Button CreateCommand (String command) {
    	Button button = new Button (command);
    	button.setActionCommand (command);
    	button.addActionListener (this);
    	return button;
    }
    
    public void init() {
		setSize (430, 150);
    	setLayout(new BorderLayout());
     	save = CreateCommand (saveCommand);
    	load = CreateCommand (loadCommand);
    	fetch = CreateCommand (fetchCommand);
    	filename = new TextField(url);
    	filetype = new Choice()  ;
    	filetype.add("HTML");
    	filetype.add("MIDI");
    	//Steph le 7/02/98
    	filetype.add("GUIDO");
    	filetype.add("TEXT");
    	filetype.add("OBJECT");
    	
    	Panel buttonpanel = new Panel();
    	Panel filepanel = new Panel();
    	Panel transportpanel = new Panel();
    	
    	filepanel.setLayout(new BorderLayout(5,0));
    	filepanel.add("West",new PixelBorder (new Label( TGlobals.getTranslation("URL")),3));
    	filepanel.add("Center", new PixelBorder(filename,3));
    	filepanel.add("East", new PixelBorder(fetch,3));
    	
    	transportpanel.setLayout(new GridLayout(2,1,0,0));
    	buttonpanel.setLayout(new GridLayout(1,3,0,0));
    	
    	buttonpanel.add(new PixelBorder(save));
    	buttonpanel.add(new PixelBorder(load));
    	buttonpanel.add(new PixelBorder(filetype));
    	
    	transportpanel.add(filepanel);
    	transportpanel.add(buttonpanel);
    	
		add("Center",displayer);
		add("South",transportpanel);
	}   
	
    
    public void actionPerformed (ActionEvent e) {
    	String action = e.getActionCommand();
    	try {
	    	if (action.equals(fetchCommand)) {
	      		 displayer.setExpression(loadUrl(new URL(filename.getText())));
	    	}
	    	else if (action.equals(saveCommand)) {
	    		FileSelector fs = getSelector (FileDialog.SAVE);
	    		if (fs.select()) saveFile (fs.file()) ;
	    	}
	    	else if (action.equals(loadCommand)) {
	    		FileSelector fs = getSelector (FileDialog.LOAD);
	    		if (fs.select()) loadFile(new URL("file", "", fs.fullName())) ;
	    	}
    	}
    	catch (Throwable ex) {
			System.err.println("FileSaver actionPerformed : " + ex);
			ex.printStackTrace();
		}
    }

	//--------------------------------------------------------
	/*
    public FileSelector getSelector (int mode) {
		if (filetype.getSelectedItem().equals ("HTML") )
			return new HTMLFileSelector (getFrame(), "", "", mode);
		if (filetype.getSelectedItem().equals ("MIDI") )
			return new MIDIFileSelector (getFrame(), "", "", mode);
		return new GUIDOFileSelector (getFrame(), "", "", mode);
    }
    */
    
     public FileSelector getSelector (int mode) {
		if (filetype.getSelectedItem().equals ("HTML") )
			return new HTMLFileSelector (getFrame(), "", "", mode);
		if (filetype.getSelectedItem().equals ("MIDI") )
			return new MIDIFileSelector (getFrame(), "", "", mode);
		if (filetype.getSelectedItem().equals ("OBJECT") )
			return new OBJECTSelector (getFrame(), "", "", mode);
		if (filetype.getSelectedItem().equals ("TEXTE") )
			return new TEXTSelector (getFrame(), "", "", mode);
		return new GUIDOFileSelector (getFrame(), "", "", mode);
    }
	
	//-------------------------------------------------	
	public void saveFile (File file) {	
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		String item = filetype.getSelectedItem ();		
		try {
			TFileSaver saver = new TFileSaver();
			saver.writeFile(new TFileContent("","","",displayer.getExpression()), file, item);
			
		}catch (Exception e){
			System.err.println("FileSaver saveFile : " + e);	
		}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}	
	
	//-------------------------------------------------		
	public void saveUrl (TExp exp, String url) {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		String item = filetype.getSelectedItem ();
		
		try {
			TFileSaver saver = new TFileSaver();
			String name = url.substring(7);
			saver.writeFile( new TFileContent("","","",exp), new File(name), item);
			
			if (!(exp instanceof TUrlExp))
				 displayer.setExpression(TExpMaker.gExpMaker.createUrl(exp,url));
		
		} catch (Exception e) { 
 			System.err.println("FileSaver saveUrl : " + url + " : " + e);
 		} 
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	//-------------------------------------------------		
	public void loadFile (File file) {		
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			TFileParser parser = new TFileParser();
			displayer.setExpression(parser.readFile(file).getExp());
			
		} catch (Exception e) { 
 			System.err.println("FileSaver loadFile : " + file + " : " + e);
 		} 
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
		
	//-------------------------------------------------	
	public void loadFile (URL url) {
		
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			TFileParser parser = new TFileParser();
			displayer.setExpression(parser.readFile(url).getExp());
		} catch (Exception e) { 
 			System.err.println("FileSaver loadFile url : " + url + " : " + e);
 		} 
 		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	//-------------------------------------------------		
	public TExp loadUrl (URL url) {
		TExp res = TExpMaker.gExpMaker.createNull();
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		try {
			TFileParser parser = new TFileParser();
			res = parser.readFile(url).getExp();
			if (!(res instanceof TUrlExp))
				res = TExpMaker.gExpMaker.createUrl(res,filename.getText());
		} catch (Exception e) { 
 			System.err.println("FileSaver loadUrl : " + url + " : " + e);
 		} 
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
 		return res;
	}
}
