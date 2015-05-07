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

import java.util.Vector;

public class TLActionItem {
	public enum Action {
		TEXT,
		TRACK,
		MULTITRACKS,
		CUT,
		COPY,
		MOVE
		}
	
	protected Action type;
	protected Object[] data;
	protected TLPane fPane;
	
	public TLActionItem(Action type, Object[] data, TLPane fPane)
	{
		this.type = type;
		this.data = data;
		this.fPane = fPane;
	}
	
	public void undo()
	{
		TLZone fs = fPane.getFSelection();
		switch (type) {
		case TEXT:
			String name = (String) data[0];
			fPane.getFName().setText(name);
			break;
		case TRACK:
			TLTrack t = (TLTrack) data[0];
			Integer n = (Integer) data[1];
			TLZone sel = (TLZone) data[2];
			fPane.getFMultiTracks().at(n.intValue());
			fPane.getFMultiTracks().remove();
			fPane.getFMultiTracks().insert(t);
			fs.set(sel);
			fPane.multiTracksChanged();
			break;
		case MULTITRACKS:
			Vector tracksVect = (Vector) data[0];
			name = (String) data[1];
			sel = (TLZone) data[2];
			fPane.getFName().setText(name);
			fPane.getFMultiTracks().clear();
			fPane.getFMultiTracks().begin();
			for (int i=tracksVect.size()-1; i>=0; i--)
			{
				fPane.getFMultiTracks().insert((TLTrack) tracksVect.get(i));
			}
			fs.set(sel);
			fPane.multiTracksChanged();
			break;
		case COPY:
			TLTrack prevScrap = (TLTrack) data[0];
			sel = (TLZone) data[1];
			fs.cmdSetScrap(prevScrap);
			fs.set(sel);
			break;
		case CUT:
			t = (TLTrack) data[0];
			n = (Integer) data[1];
			prevScrap = (TLTrack) data[2];
			sel = (TLZone) data[3];
			fPane.getFMultiTracks().at(n.intValue());
			fPane.getFMultiTracks().remove();
			fPane.getFMultiTracks().insert(t);
			fs.cmdSetScrap(prevScrap);
			fs.set(sel);
			fPane.multiTracksChanged();
			break;
		case MOVE:
			TLTrack t1 = (TLTrack) data[0];
			TLTrack t2 = (TLTrack) data[1];
			Integer n1 = (Integer) data[2];
			Integer n2 = (Integer) data[3];
			sel = (TLZone) data[4];
			fPane.getFMultiTracks().at(n1.intValue());
			fPane.getFMultiTracks().remove();
			fPane.getFMultiTracks().insert(t1);
			fPane.getFMultiTracks().at(n2.intValue());
			fPane.getFMultiTracks().remove();
			fPane.getFMultiTracks().insert(t2);
			fs.set(sel);
			fPane.multiTracksChanged();
			break;
		default:
			break;
		}
		fPane.fUpdater.doUpdates();
		
	}
	
	public void print()
	{
		System.out.println(type);
	}
}
