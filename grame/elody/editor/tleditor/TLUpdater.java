/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.tleditor;

import grame.elody.lang.texpression.expressions.TExp;

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
		if (fSelectionChanged)					scrollSelection();
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
		TExp e = TLConverter.exp(fTarget.getFMultiTracks());
		fTarget.notifier.notifyObservers (e);
	}
	
	public final void scrollSelection()
	{
		boolean change = false;
		if (((fTarget.getTimePosEnd()-fTarget.fTimePos)*4/5)>fTarget.getFSelection().duration())
		// blocage de l'autoscroll pour les grands objets ( > 4/5 de l'écran)
		{
			if (fTarget.getFSelection().start()<fTarget.fTimePos)
			{
				fTarget.fTimePos = fTarget.getFSelection().start();
				change = true;
			}
			if (fTarget.getFSelection().end()>fTarget.getTimePosEnd())
			{
				int marginX = fTarget.time2x(fTarget.getFSelection().end())-fTarget.getWidth()+50;
				fTarget.fTimePos = fTarget.x2time(marginX);
				change = true;
			}

			if (fTarget.getFSelection().topline()<=fTarget.fLinePos)
			{
				fTarget.fLinePos = fTarget.getFSelection().topline();
				change = true;
			}
			if (fTarget.getFSelection().topline()>=fTarget.getLinePosEnd())
			{
				fTarget.fLinePos += fTarget.getFSelection().topline()-fTarget.getLinePosEnd()+1;
				change = true;
			}
			if (change)
			{
				contentChanged();
				fTarget.adjustScrollbars();
			}
		}
	}
	
	public final void scrollDrop(int time, int line, int dur)
	{
		boolean change = false;

		if (((fTarget.getTimePosEnd()-fTarget.fTimePos)*4/5)>dur)
		// blocage de l'autoscroll pour les grands objets ( > 4/5 de l'écran)
		{
			if (time<fTarget.fTimePos)
			{
				fTarget.fTimePos = time;
				change = true;
			}
			if ((time+dur)>fTarget.getTimePosEnd())
			{
				int marginX = fTarget.time2x(time+dur)-fTarget.getWidth()+50;
				fTarget.fTimePos = fTarget.x2time(marginX);
				change = true;
			}

			if ((line==fTarget.fLinePos)&&(fTarget.fLinePos>0))
			{
				fTarget.fLinePos--;
				change = true;
			}
			if (line>=fTarget.getLinePosEnd())
			{
				fTarget.fLinePos ++;
				change = true;
			}


			if (change)
			{
				contentChanged();
				fTarget.adjustScrollbars();
				doUpdates();
			}
		}
	}
	
	public final void scrollCursor(int time)
	{
		if ( (time<fTarget.fTimePos)||(time>fTarget.getTimePosEnd()) )
		{
			fTarget.fTimePos = time;
			contentChanged();
			fTarget.adjustScrollbars();
			doUpdates();
		}
	}
}
