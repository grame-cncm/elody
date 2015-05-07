/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.misc;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TDialog extends Dialog implements ActionListener {
	Label DialogMessageLine1 = new Label("", Label.CENTER);
	Label DialogMessageLine2 = new Label("", Label.CENTER);
	Button OKButton;
	String OKCommand = "  OK  ";

	public TDialog (Frame Win, String Line1, String Line2)	// constructeur 1
	{	
		super(Win, "Dialog Box", true);
		setSize(350,100);
		
		DialogMessageLine1.setText(Line1);
		DialogMessageLine2.setText(Line2);
		
		affich();	
	}	

	public TDialog (Frame Win, String Line1)	// constructeur 2
	{	
		super(Win, "Dialog Box", true);
		setSize(350,100);
		
		DialogMessageLine1.setText(Line1);
	
		affich();	
	}	

		
	public void actionPerformed (ActionEvent e)	// détecte les actions
	{
    	String action = e.getActionCommand();
    	    	    	
    	if (action.equals(OKCommand))
    	{
    		setVisible(false);
    	}
    }
	
	private Button CreateCommand (String command)
	{
    	Button button = new Button (command);
    	button.setActionCommand (command);
    	button.addActionListener (this);
    	return button;
    }

	private void affich()
	{
		setLayout(new GridLayout(3,1));

		OKButton = CreateCommand(OKCommand);
		
		Panel OKPanel = new Panel();
		OKPanel.setLayout(new GridLayout(1,3));
		OKPanel.add(new Label(""));
		OKPanel.add(OKButton);
		OKPanel.add(new Label(""));
	
		add(DialogMessageLine1);
		add(DialogMessageLine2);
		add(OKPanel);
	}
}

class TDialogEntry extends Dialog implements ActionListener
{

	Label DialogMessageLine1 = new Label("", Label.CENTER);
	TextArea DialogMessageLine2 = new TextArea("", 3,10);
	Button OKButton;
	String OKCommand = "  OK  ";

	
	public TDialogEntry (Frame Win, String Line1, String Line2) // constructeur 1
	{
		super(Win, "Dialog Box", true);
		setSize(320,150);
		
		DialogMessageLine1.setText(Line1);
		DialogMessageLine2.setText(Line2);
		
		affich();	
	}	

	public TDialogEntry (Frame Win, String Line1)	// constructeur 2
	{
		super(Win, "Dialog Box", true);
		setSize(320,150);
		
		DialogMessageLine1.setText(Line1);
	
		affich();	
	}	

	public void actionPerformed (ActionEvent e)	// détecte les actions
	{
    	String action = e.getActionCommand();
    	    	    	
    	if (action.equals(OKCommand))
    	{
    		setVisible(false);
    	}
    	
    }
	
	
	public String GetEntry()	// retourne le texte saisi par l'utilisateur
	{
		return DialogMessageLine2.getText();
	}
	
	
	private Button CreateCommand (String command)
	{
    	Button button = new Button (command);
    	button.setActionCommand (command);
    	button.addActionListener (this);
    	return button;
    }

	private void affich()
	{
		setLayout(new GridLayout(3,1,10,10));

		OKButton = CreateCommand(OKCommand);
		
		Panel OKPanel = new Panel();
		OKPanel.setLayout(new GridLayout(1,3));
		OKPanel.add(new Label(""));
		OKPanel.add(OKButton);
		OKPanel.add(new Label(""));
	
		add(DialogMessageLine1);
		add(DialogMessageLine2);
		add(OKPanel);
	}
}

class TDialogQuestion extends Dialog implements ActionListener
{

	Label DialogMessageLine1 = new Label("", Label.CENTER);
	Label DialogMessageLine2 = new Label("", Label.CENTER);
	Button YesButton;
	Button NoButton;
	String YesCommand = "  Yes  ";
	String NoCommand = "  No  ";
	boolean AnswerFlag = false;
	
	
	public TDialogQuestion (Frame Win, String Line1, String Line2)	// constructeur 1
	{
		super(Win, "Dialog Box", true);
		setSize(350,100);
	
		DialogMessageLine1.setText(Line1);
		DialogMessageLine2.setText(Line2);
	
		affich();
	}

	public TDialogQuestion (Frame Win, String Line1)	// constructeur 2
	{
		super(Win, "Dialog Box", true);
		setSize(350,100);
	
		DialogMessageLine1.setText(Line1);
		affich();
	}

	private void affich()
	{
		setLayout(new GridLayout(3,1));

		YesButton = CreateCommand(YesCommand);
		NoButton = CreateCommand(NoCommand);
		
		Panel AnswerPanel = new Panel();
		AnswerPanel.setLayout(new GridLayout(1,5));
		AnswerPanel.add(new Label(""));
		AnswerPanel.add(YesButton);
		AnswerPanel.add(new Label(""));
		AnswerPanel.add(NoButton);
		AnswerPanel.add(new Label(""));
		
		add(DialogMessageLine1);
		add(DialogMessageLine2);
		add(AnswerPanel);
	}

	public void actionPerformed (ActionEvent e)	//détecte les actions
	{
    	String action = e.getActionCommand();
    	    	    	
    	if (action.equals(YesCommand))
    	{
    		AnswerFlag = true;	
		}
		else
		{
    		AnswerFlag = false;	
		}
    	setVisible(false);
    }
	
	public boolean GetAnswer() 	//renvoie la réponse de l'utilisateur 	
	{
		return AnswerFlag;	// true : yes, false : no
	}
		
	private Button CreateCommand (String command)
	{
    	Button button = new Button (command);
    	button.setActionCommand (command);
    	button.addActionListener (this);
    	return button;
    }
}