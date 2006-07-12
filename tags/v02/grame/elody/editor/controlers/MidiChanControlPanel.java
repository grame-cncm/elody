package grame.elody.editor.controlers;

import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.util.MsgNotifier;
import grame.midishare.Midi;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.util.Observable;
import java.util.Observer;

public class MidiChanControlPanel extends Panel implements Observer {
	final int ProgMsg		= 5000;
	final int VoluMsg		= 5001;
	final int PanMsg		= 5002;
	int channel; 
	EditControler program, volume, panoramic; 
	Label label;
	int vol, prog, pan;

	//----------------------------------------------
	public MidiChanControlPanel (int chan) {
		setLayout(new GridBagLayout());
   		GridBagLayout gbl = (GridBagLayout)getLayout();
		GridBagConstraints c = new GridBagConstraints();
		int eol = GridBagConstraints.REMAINDER;
		channel = chan;
		label = new Label(Integer.toString(channel+1), Label.CENTER);
		setConstraints (c, eol, GridBagConstraints.NONE, GridBagConstraints.CENTER, 20, 10, 1,1, 2,0,0,2);
		gbl.setConstraints (label, c);
		add (label);

		program = new EditControler(new JamButtonControler(1,128,0,Color.orange,Define.pitchButton), Define.TextCtrlSize);
		program.setValue (prog = 1);
		program.setMessage (ProgMsg);
		setConstraints (c, eol, GridBagConstraints.NONE, GridBagConstraints.CENTER, 30, 30, 1,1, 1,1,1,1);
		gbl.setConstraints (program, c);
		add (program);

		volume = new EditControler(new BarControler(0,127,Controler.kVertical, Color.green), Define.TextCtrlSize);
		volume.setValue (vol = 100);
		volume.setMessage (VoluMsg);
		setConstraints (c, eol, GridBagConstraints.NONE, GridBagConstraints.CENTER, 30,60, 1,1, 5,1,1,1);
		gbl.setConstraints (volume, c);
		add (volume);
		
		panoramic = new EditControler(new BarControler(0,127,Controler.kVertical, Color.orange), Define.TextCtrlSize);
		panoramic.setValue (pan = 64);
		panoramic.setMessage (PanMsg);
		setConstraints (c, eol, GridBagConstraints.NONE, GridBagConstraints.CENTER, 30,60, 1,1, 10,1,1,1);
		gbl.setConstraints (panoramic, c);
		add (panoramic);
		addObserver (this);
	}
	//----------------------------------------------
	public int GetPan ()		{ return panoramic.getValue(); }
	public int GetVolume ()		{ return volume.getValue(); }
	public int GetProgram ()	{ return program.getValue(); }
	public int GetChannel ()	{ return channel; }
	public void SetPan (int val)		{ panoramic.setValue(val); }
	public void SetVolume (int val)		{ volume.setValue(val); }
	public void SetProgram (int val)	{ program.setValue(val); }
	public void SetChannel (int chan) { 
		channel = chan;
		label.setText (Integer.toString(channel+1));
	}
	public void addObserver (Observer o) {
		program.addObserver (o);
		volume.addObserver (o);
		panoramic.addObserver (o);
	}

	//----------------------------------------------
	protected void setConstraints (GridBagConstraints c, int gridw, int fill, int anchor, 
	                  				int ipadx, int ipady, double wgtx, double wgty,
	                  				int top, int left, int right, int bottom )
	{
		c.gridx = c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = gridw; c.gridheight = 1;
		c.fill = fill; c.anchor = anchor;
		c.ipadx = ipadx; c.ipady = ipady;
		c.weightx = wgtx; c.weighty = wgty;
		c.insets = new Insets(top, left, bottom, right);
	}
	//----------------------------------------------
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		switch (mn.message()) {
  			case ProgMsg:
  				int newProg = ((Integer)arg).intValue();
  				if (newProg != prog) 
  					SendProg (prog = newProg);
  				break;
  			case VoluMsg:
  				int newVol = ((Integer)arg).intValue();
  				if (newVol != vol)
  					SendVol (vol = newVol);
  				break;
  			case PanMsg:
  				int newPan = ((Integer)arg).intValue();
  				if (newPan != pan)
  					SendPan (pan = newPan);
  				break;
  		}
  	}
	//----------------------------------------------
	public void SendPan (int val) {
		int ev = Midi.NewEv(Midi.typeCtrlChange);
		if (ev!=0) {
			Midi.SetPort (ev, TGlobals.context.getPort());
			Midi.SetChan (ev, channel);
			Midi.SetField (ev, 0, 10);
			Midi.SetField (ev, 1, val);
			Midi.SendIm (TGlobals.player.getRefnum(), ev);
		}
	}
	//----------------------------------------------
	public void SendVol (int val) {
		int ev = Midi.NewEv(Midi.typeCtrlChange);
		if (ev!=0) {
			Midi.SetPort (ev, TGlobals.context.getPort());
			Midi.SetChan (ev, channel);
			Midi.SetField (ev, 0, 7);
			Midi.SetField (ev, 1, val);
			Midi.SendIm (TGlobals.player.getRefnum(), ev);
		}
	}
	//----------------------------------------------
	public void SendProg (int val) {
		int ev = Midi.NewEv (Midi.typeProgChange);
		if (ev!=0) {
			Midi.SetPort (ev, TGlobals.context.getPort());
			Midi.SetChan(ev, channel);
			Midi.SetField (ev, 0, val-1);
			Midi.SendIm (TGlobals.player.getRefnum(), ev);
		}
	}
}
