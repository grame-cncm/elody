package grame.elody.editor.tleditor;

import grame.elody.lang.texpression.expressions.TExp;

public class TLActionItem {
	public enum Action {
		MUTESELTRACK,
		UNMUTESELTRACK,
		NORMALMODESELTRACK,
		SEQFUNMODESELTRACK,
		MIXFUNMODESELTRACK,
		PARMODESELTRACK,
		ABSMODESELTRACK,
		SIMPLEDROP,
		RESIZE,
		CLEARSELEVENT,
		APPLICATION,
		DUPLICATION,
		MOVE,
		CUT,
		COPY,
		PASTE,
		STYLE
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
		case MUTESELTRACK:
			Integer n = (Integer) data[0];
			fPane.fTrackNum = n.intValue();
			fPane.unmuteSelTrack(true);
			break;
		case UNMUTESELTRACK:
			n = (Integer) data[0];
			fPane.fTrackNum = n.intValue();
			fPane.muteSelTrack(true);
			break;
		case NORMALMODESELTRACK:
		case SEQFUNMODESELTRACK:
		case MIXFUNMODESELTRACK:
		case PARMODESELTRACK:
		case ABSMODESELTRACK:
			n = (Integer) data[0];
			fPane.fTrackNum = n.intValue();
			Integer mode = (Integer) data[1];
			switch (mode.intValue()) {
			case TLTrack.NORMAL:		fPane.normalModeSelTrack(true);		break;
			case TLTrack.SEQFUNCTION:	fPane.seqFunModeSelTrack(true);		break;
			case TLTrack.ABSTRACTION:	fPane.absModeSelTrack(true);		break;
			case TLTrack.PARAMETER:		fPane.parModeSelTrack(true);		break;
			case TLTrack.MIXFUNCTION:	fPane.mixFunModeSelTrack(true);		break;
			}
			break;
		case SIMPLEDROP:
			TLZone selection = (TLZone) data[0];
			Integer margin = (Integer) data[1];
			fs.selectEvent(selection.start(), selection.topline());
			fPane.clearSelEvent(true);
			fs.suppressRestTime(margin.intValue());
			break;
		case RESIZE:
			selection = (TLZone) data[0];
			Integer end = (Integer) data[1];
			margin = (Integer) data[2];
			fs.set(selection);
			fs.cmdResize(end);
			TLZone newSelection = new TLZone(fs);
			fs.selectDstPoint(fs.end()+1, fs.topline());
			fs.suppressRestTime(margin.intValue());
			fs.set(newSelection);
			fPane.multiTracksChanged();
			break;
		case PASTE:
			selection = (TLZone) data[0];
			margin = (Integer) data[1];
			TLTrack replace = (TLTrack) data[2];
			fs.set(selection);
			fPane.clearSelEvent(true);
			fs.suppressRestTime(margin.intValue());
			if (replace!=null)
			{
				fs.suppressRestTime(replace.getFullDur());
				fs.insertTrackToContent(replace);
			}
			break;
		case COPY:
			TLTrack prevScrap = (TLTrack) data[0];
			fs.cmdSetScrap(prevScrap);
			break;
		case CUT:
			prevScrap = (TLTrack) data[2];
			fs.cmdSetScrap(prevScrap);
		case CLEARSELEVENT:
			selection = (TLZone) data[0];
			TLTrack t = (TLTrack) data[1];
			fs = selection;
			fs.suppressRestTime(t.getFullDur());
			fs.insertTrackToContent(t);
			fPane.multiTracksChanged();
			break;
		case APPLICATION:
			TLZone origin = (TLZone) data[0];
			selection = (TLZone) data[1];
			t = (TLTrack) data[2];
			margin = (Integer) data[3];
			fs.set(selection);
			fPane.clearSelEvent(true);
			fs.suppressRestTime(t.getFullDur());
			fs.suppressRestTime(margin.intValue());
			fs.insertTrackToContent(t);
			fs.set(origin);
			fPane.multiTracksChanged();
			break;
		case DUPLICATION:
			origin = (TLZone) data[0];
			selection = (TLZone) data[1];
			margin = (Integer) data[2];
			fs.set(selection);
			fPane.clearSelEvent(true);
			fs.suppressRestTime(margin.intValue());
			fs.set(origin);
			break;
		case MOVE:
			origin = (TLZone) data[0];
			selection = (TLZone) data[1];
			margin = (Integer) data[2];
			fs.set(selection);
			TLTrack buffer = fs.copyContentToTrack(); 
			fPane.clearSelEvent(true);
			fs.suppressRestTime(margin.intValue());
			fs.selectDstPoint(origin.start(), origin.topline());
			fs.suppressRestTime(buffer.getFullDur());
			fs.insertTrackToContent(buffer);
			fPane.multiTracksChanged();
			break;

		default:
			break;
		}
		fPane.fUpdater.doUpdates();
		
	}
	
	public void print()
	{
		//System.out.println(type);
	}
}
