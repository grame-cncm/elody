package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.tleditor.TLActionItem;
import grame.elody.editor.tleditor.TLConverter;
import grame.elody.editor.tleditor.TLPane;
import grame.elody.editor.tleditor.TLZone;
import grame.elody.editor.tleditor.TLActionItem.Action;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action de chg du canal
//-------------------------------------------------

public class TLChannelAction extends TLDragAction {
	int 	oldx;
	int 	oldy;
	int 	trp;
	String	msg;
	TExp	exp;

	public TLChannelAction(TLPane pane, int x, int y)
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
		trp =  (oldx - m.getX())/3;
		if (trp != oldtrp) {
			msg = (trp != 0) ? new String("Channel : " + trp) : "";
			TGlobals.player.startPlayer ( TExpMaker.gExpMaker.createTrsch(exp,trp) );
		}
	}
	public void mouseReleased(MouseEvent m)
	{ 
		if ( trp != 0)
		{
			fPane.toUndoStack(Action.TRACK);
			fPane.getFSelection().cmdChannel(trp);
			fPane.multiTracksChanged();
		}
	}
	public void drawVisualFeedback(Graphics g)
	{
		//Graphics g = getGraphics();
		g.setXORMode(TLPane.getFArgColorBkg()); 
			g.setColor(TLPane.getFTraitColor()); 			
			g.drawString(msg, oldx + 5, oldy + 12);
		g.setPaintMode();
	}
	public void mouseClicked(MouseEvent m) {}
}
