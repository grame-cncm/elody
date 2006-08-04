package grame.elody.editor.constructors;

import grame.elody.editor.constructors.parametrer.ParamFrame;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.misc.TGlobals;
import grame.elody.util.fileselector.ColoredLabel;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionListener;

public class SeqCommandsPanel extends ParamFrame {
	public SeqCommandsPanel(ExprHolder exprHolders[], int width, SeqPlayerMgr player, ActionListener listener) {
		super (Color.red);
		ResultHolder sh = new ResultHolder (exprHolders);
		super.init (sh, width, width);
   		GridBagLayout gbl = (GridBagLayout)getLayout();
		GridBagConstraints c = new GridBagConstraints();
		int eol = GridBagConstraints.REMAINDER;
		int align = GridBagConstraints.CENTER;

		setConstraints (c, eol, GridBagConstraints.NONE, align, 0,0, 0,0, 0,2,2,1);
		add (new ColoredLabel(TGlobals.getTranslation("Parameters"), Label.CENTER, Color.darkGray), gbl, c);
		setConstraints (c, eol, GridBagConstraints.BOTH, align, 30,30, 1,1, 0,4,4,4);
		add (new SeqHelpPanel (), gbl, c);
		setConstraints (c, eol, GridBagConstraints.NONE, align, 20,0, 1,0, 0,5,5,2);
		add (player.buildControl (), gbl, c);
		setConstraints (c, eol, GridBagConstraints.NONE, align, 20,0, 1,0, 0,5,5,4);

		Button clear = new Button (SeqConstructor.clearCommand);
     	clear.setActionCommand (SeqConstructor.clearCommand);
     	clear.addActionListener (listener);
		add (clear, gbl, c);

		for (int i=0; i < exprHolders.length; i++) {
			exprHolders[i].addObserver (sh);
		}
	}
	public void add (Component p, GridBagLayout gbl, GridBagConstraints c) {
		gbl.setConstraints (p, c);
		add (p);
	}
}
