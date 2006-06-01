package grame.elody.editor.treeeditor;

import grame.elody.editor.treeeditor.simplelayouts.HorizontalLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class StringPanel extends Panel implements ActionListener, FocusListener {
	static LayoutManager 	gHorizontalLayout = new HorizontalLayout(4);
	static Font				gFont = new Font("Monospaced", Font.PLAIN, 10);
	static Color			gBackground = Color.white;
	
	static String	gTextBuffer;

	TreePanel 		fFather;
	int				fSonNumber;
	Label			fLabel;
	TextField		fVal;
	
	public StringPanel (TreePanel father, int sonNumber, String kind, String val)
	{
		setLayout(gHorizontalLayout);
		setFont(gFont);
		fFather 	= father;
		fSonNumber 	= sonNumber;
		fLabel		= new Label(kind);
		fVal		= new TextField (val, 4);
		fVal.setBackground(gBackground); 
		fVal.addActionListener(this); 
		fVal.addFocusListener(this);
		add(fLabel);
		add(fVal);
	}
	public void actionPerformed(ActionEvent e)
	{
		//System.out.println("Action event " + e + " (" + e.getActionCommand() + ")");
		TextComponent tc = (TextComponent) e.getSource();
		String newtext = tc.getText();
		if (!newtext.equals(gTextBuffer)) {
			gTextBuffer = newtext;
			tc.setCaretPosition(0);
			fFather.updateSubString (gTextBuffer, fSonNumber);
		}
	}
	public void focusGained(FocusEvent e)
	{
		TextComponent tc = (TextComponent) e.getSource();
		gTextBuffer = tc.getText();
		tc.setSelectionStart(0);
		tc.setSelectionEnd(gTextBuffer.length());
		//System.out.println("Focus Gained " + e );
	}
	public void focusLost(FocusEvent e)
	{
		TextComponent tc = (TextComponent) e.getSource();
		String newtext = tc.getText();
		if (!newtext.equals(gTextBuffer)) {
			gTextBuffer = newtext;
			tc.setCaretPosition(0);
			tc.setSelectionStart(0);
			tc.setSelectionEnd(0);
			fFather.updateSubString (gTextBuffer, fSonNumber);
		}
		//System.out.println("Focus Lost " + e );
	}
}
