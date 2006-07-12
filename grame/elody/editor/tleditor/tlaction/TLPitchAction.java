package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.tleditor.TLConverter;
import grame.elody.editor.tleditor.TLPane;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action de transposition
//-------------------------------------------------
public class TLPitchAction extends TLDragAction
{
	int 	oldx;
	int 	oldy;
	int 	trp;
	String	msg;
	TExp	exp;
	
	public TLPitchAction(TLPane pane, int x, int y)
	{
		super(pane);
		oldx = x;
		oldy = y;
		trp = 0;
		msg = "";
		exp =  TLConverter.exp(fPane.getFSelection().copyContentToTrack()); 

	}
	public void mouseDragged(MouseEvent m) 
	{ 
		int oldtrp = trp;
		trp =  (oldy - m.getY())/3;
		if (trp != oldtrp) {
			msg = (trp != 0) ? new String("Pitch : " + trp) : "";
			TGlobals.player.startPlayer ( TExpMaker.gExpMaker.createTrsp(exp,trp) );
		}
	}
	public void mouseReleased(MouseEvent m)
	{ 
		if ( trp != 0) { fPane.getFSelection().cmdPitch(trp); fPane.multiTracksChanged();}
	}
	public void drawVisualFeedback(Graphics g)
	{
		//Graphics g = getGraphics();
		g.setXORMode(fPane.getFArgColorBkg()); g.setColor(fPane.getFTraitColor()); 			
		g.drawString(msg, oldx + 5, oldy - 10);
		g.setPaintMode();
	}
	public void mouseClicked(MouseEvent m) {}
}
