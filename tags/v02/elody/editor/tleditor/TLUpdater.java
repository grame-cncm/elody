package grame.elody.editor.tleditor;

import java.awt.Graphics;

//--------------------------------------------------------------------------
//	TLUpdater : garde trace des updates à faire
//--------------------------------------------------------------------------

public final class TLUpdater {
	
	final TLPane fTarget;
	boolean	fContentChanged;
	boolean	fDurationChanged;
	boolean	fViewChanged;
	boolean	fSelectionChanged;
	
	boolean fNeedSBUpdate;
	
	public TLUpdater (TLPane target)
	{
		fTarget = target;
		fContentChanged = true;
		fDurationChanged = true;
		fViewChanged = true;
		fSelectionChanged = true;
		fNeedSBUpdate = true;
	}
	
	public final void contentChanged()		{ fContentChanged 	= true; }
	public final void durationChanged()		{ fDurationChanged 	= true; }
	public final void viewChanged()			{ fViewChanged 		= true; }
	public final void selectionChanged()	{ fSelectionChanged = true; }
	
	public final void doUpdates()
	{
		doUpdates(true);
	}
	public final void doUpdates(boolean wantSBUpdate)
	{
		if (fContentChanged) 					fTarget.notifyContentChanged();
		if (fContentChanged | fViewChanged) 	fTarget.prepareOffscreen();
		if (fDurationChanged | fViewChanged) 	fNeedSBUpdate = true;
		
		if (fContentChanged | fViewChanged | fSelectionChanged) {
			Graphics g = fTarget.getGraphics();
			fTarget.update(g);
			g.dispose();
		}
		
		if (fNeedSBUpdate & wantSBUpdate) 		{ fTarget.adjustScrollbars(); fNeedSBUpdate = false; }

		fContentChanged 	= false;
		fDurationChanged 	= false;
		fViewChanged 		= false;
		fSelectionChanged 	= false;
	}
}
