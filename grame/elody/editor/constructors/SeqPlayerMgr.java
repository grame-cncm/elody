package grame.elody.editor.constructors;

import grame.elody.editor.constructors.parametrer.ParamExprHolder;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.player.ExpObserver;
import grame.elody.editor.player.TSeqRealTimePlayer;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MidiUtils;
import grame.elody.util.MsgNotifier;
import grame.midishare.player.MidiPlayer;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class SeqPlayerMgr implements Observer, ExpObserver, ActionListener {
	static String playCommand = TGlobals.getTranslation("Play");
	static String stopCommand = TGlobals.getTranslation("Stop");
	private TSeqRealTimePlayer player;
	private int playIndex;
	Button button;
	String name;

	ParamExprHolder exprHolders[];

    public SeqPlayerMgr (ParamExprHolder e[]) {
   		player = new TSeqRealTimePlayer (this, e.length);
		exprHolders = e;
		playIndex = -1;
    }
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		if (mn.message() == Define.ExprHolderMsg) {
			if (isNull())	stop ();
		}
  	}	
	public boolean playing () { return player.getState().state == MidiPlayer.kPlaying; }
	public boolean isNull () {
		for (int i=0; i<exprHolders.length; i++)
			if ((exprHolders[i]!=null) && !exprHolders[i].isNull()) return false;
		return true;
	}
	public TExp getExpression (int i) 	{ return exprHolders[i].getExpression (); }
	public void startExpression (int i) { if (playing()) drawIndicator (i); }
	public Button buildControl () {
		button = new Button (playCommand);
     	button.setActionCommand (playCommand);
     	button.addActionListener (this);
		return button;
	}
    public void start() { 
		if (!isNull()) {
			player.startPlayer();
			button.setLabel (stopCommand);
     		button.setActionCommand (stopCommand);
		}
	}
    public void stop() {
		if (playing()) {
			player.stopPlayer(); 
			button.setLabel (playCommand);
     		button.setActionCommand (playCommand);
			drawIndicator (-1);
		}
	}
    public void actionPerformed (ActionEvent e) {
    	String action = e.getActionCommand();
    	if (action.equals(playCommand)) {
			start();
    	}
    	else if (action.equals(stopCommand)) {
			stop();
    	}
	}

    public void open () {
    	try { 
    		name = MidiUtils.availableName("ElodySequencer");
    		player.Open (name);
    		TGlobals.context.restoreConnections(name);
      	}
    	catch (Exception e) { System.err.println( "SeqPlayerMgr open : " + e); }
    }
    public void close() {
    	stop();
    	TGlobals.context.saveConnections(name);
    	player.Close();
   }
	public void drawIndicator (int i) {
		if (playIndex >= 0)	exprHolders[playIndex].frame (false);
		if (i >= 0)			exprHolders[i].frame (true);
		playIndex = i;
	}
}
