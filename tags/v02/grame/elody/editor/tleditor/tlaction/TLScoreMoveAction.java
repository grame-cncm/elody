package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.editor.tleditor.TLConverter;
import grame.elody.editor.tleditor.TLExportDrag;
import grame.elody.editor.tleditor.TLPane;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
//action de déplacement du multitracks
//-------------------------------------------------

public class TLScoreMoveAction extends TLDragAction implements TExpContent {
	TLExportDrag	fExport;
	
	public TLScoreMoveAction(TLPane pane)
	{
		super(pane);
		fExport = new TLExportDrag(pane, this);
		fPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	public void mouseDragged(MouseEvent m) 
	{
		fExport.mouseDragged(m);
	}
	public void mouseReleased(MouseEvent m) 
	{
		fExport.mouseReleased(m);
	}
	public void drawVisualFeedback(Graphics g) 
	{
	}
	public void mouseClicked(MouseEvent m) 
	{
	}
	public TExp getExpression()
	{
		TExp exp = TLConverter.exp (fPane.getFMultiTracks());
		String name = fPane.getFName().getText();
		if (! name.equals("")) { 		
			//System.out.println( "name : " + name + ", size = " + name.length() );
			exp = TExpMaker.gExpMaker.createNamed(exp, name); 
		}
		return exp;
	}
}
