package grame.elody.editor.constructors;

import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.file.guido.parser.TokenMgrError;
import grame.elody.file.text.parser.TTextParser;
import grame.elody.file.text.saver.TTextExpSaver;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Observable;
import java.util.Observer;

public class TextInput extends BasicApplet implements Observer, ActionListener {
	final static String readCommand = "Eval";
	final static Font fFont = new Font("SansSerif", Font.PLAIN, 9);
	
	Button read;
	ExprHolder  displayer;
	TextArea textArea;
	TextField errorField;
	TTextParser parser;
    TExp curExp = TExpMaker.gExpMaker.createNull();
	
	public TextInput (){	
		super ("TextInput");
		textArea = new TextArea ("", 5,40);
		textArea.setEditable(true);
		errorField = new TextField();
		read = CreateCommand ( readCommand ) ;
		
		setFont(fFont);
		setSize(500,300);
	}
	
	private Button CreateCommand (String command) {
		Button button = new Button (command);
		button.setActionCommand (command);
     	button.addActionListener (this);
     	return button;
	}

	
	public void init(){
		Panel botPanel= new Panel();
		setLayout(new BorderLayout(2, 5));
		displayer = new ExprHolder(null, new TNotesVisitor(),true);
		displayer.addObserver (this);
		add("North",displayer);  
		add("Center",textArea);
  		botPanel.setLayout(new BorderLayout());
 		botPanel.add("West",read);
  		botPanel.add("Center",errorField);  		
  		add("South",botPanel);
  	}
    
	 public  void update  (Observable o, Object arg) { 
		//MsgNotifier mn = (MsgNotifier)o;
		TExp exp = displayer.getExpression();
		if (!exp.equals(curExp)) {
			StringWriter writer = new  StringWriter();
			TTextExpSaver saver = new TTextExpSaver(writer);
			saver.writeExp(exp);
			saver.writeFileEnd();
			textArea.setText(writer.toString());
			curExp = exp;
		}
	 }
	  
	 public void actionPerformed (ActionEvent e) {
    	String action = e.getActionCommand();
    	if (action.equals (readCommand)) {
    		curExp = readText ();
    		displayer.setExpression(curExp);
		}
   	 }
   	 
   		
	public TExp readText() {
   	 	String input = textArea.getText();
   		parser = new TTextParser( new StringReader(input));
		TExp res = TExpMaker.gExpMaker.createNull();
		
		try {	
			errorField.setText("");
			res = parser.readTextFile();
			errorField.setText("OK");
		} catch (TokenMgrError  e1) {
			errorField.setText(e1.getMessage());
		}catch (Exception ex) {
			errorField.setText("Can not build expression");
		}
		
		/*
		int caretPos = (e.currentToken.next.beginLine - 1 )*textArea.getColumns() + e.currentToken.next.beginColumn;
		textArea.setCaretPosition(caretPos);
		textArea.setSelectionStart(caretPos-2);
		textArea.setSelectionEnd(caretPos+2);
		textArea.selectAll();
		textArea.select(caretPos, caretPos+1);
		*/
		
		return res;
	 }
}
