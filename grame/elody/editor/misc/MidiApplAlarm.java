package grame.elody.editor.misc;

import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiException;

import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MidiApplAlarm extends MidiAppl {

	protected Led midiLed;
	protected Checkbox thru;
	
	public MidiApplAlarm() {
		super();
		midiLed = new Led();
		thru = new Checkbox("thru");
		thru.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Midi.Connect(0, 0, (thru.getState() ? 1 : 0) ); 
			}			
		});

	}
	
	public void Open (String name) throws MidiException
	{
		super.Open(name);
		Midi.Connect(0, refnum, 1);
		for (int i=0; i<256; i++)
			Midi.AcceptType(filter, i, 0);
		Midi.AcceptType(filter, Midi.typeNote, 1);
		Midi.AcceptType(filter, Midi.typeCtrlChange, 1);
		Midi.AcceptType(filter, Midi.typeProgChange, 1);
		Midi.AcceptType(filter, Midi.typeKeyOn, 1);
		Midi.AcceptType(filter, Midi.typeKeyOff, 1);
		Midi.AcceptType(filter, Midi.typePrivate, 1);	
	}
	
	public Led getMidiLed() { return midiLed; }
	public Checkbox getThru() { return thru; }
	
	public void ReceiveAlarm (int event)
	{
		switch (Midi.GetType(event))
		{
			case Midi.typeKeyOn:
				midiLed.setLedOn(true);
				break;
			case Midi.typeKeyOff:
			case Midi.typePrivate:
				midiLed.setLedOn(false);
				break;
			default:
				midiLed.setLedOn(true);
				int privEv = Midi.NewEv(Midi.typePrivate);
				Midi.SendAt(refnum, privEv, Midi.GetDate(event)+100);
				break;
		}
		Midi.FreeEv(event);
	}
}

class Led extends Canvas {
	
	protected Image midiLedOn;
	protected Image midiLedOff;
	
	protected boolean ledOn = false;

	public Led() {
		super();
		setVisible(true);
		midiLedOn = Toolkit.getDefaultToolkit().createImage("Images/key_on.png");
		midiLedOff = Toolkit.getDefaultToolkit().createImage("Images/key_off.png");
	}
	
	public void setLedOn(boolean b) {
		ledOn = b;
		update(getGraphics());
	}
	
    public void paint(Graphics g) {update(g);}
    
    public void update(Graphics g) 
	{
    	g.drawImage( (ledOn ? midiLedOn : midiLedOff),getSize().width-21,0,this);
	}

	
}
