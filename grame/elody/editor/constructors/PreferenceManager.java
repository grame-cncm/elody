/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors;

import grame.elody.editor.controlers.TextBarField;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.Singleton;
import grame.elody.util.MathUtils;
import grame.elody.util.MidiUtils;
import grame.midishare.Midi;

import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

public class PreferenceManager extends Singleton implements ItemListener,
		Observer, MouseListener {
	TextBarField port, events;

	Choice inConnection, outConnection;

	String in, out;

	public PreferenceManager() {
		super(TGlobals.getTranslation("PreferenceManager"));
		inConnection = new Choice();
		outConnection = new Choice();
		port = new TextBarField(new Integer(TGlobals.context.getPort())
				.toString(), 10);
		port.addObserver(this);
		events = new TextBarField(new Integer(TGlobals.context.getEvents())
				.toString(), 10);
		events.addObserver(this);
		setSize(300, 120);
	}

	void initMenus() {
		String name;

		String in1 = inConnection.getSelectedItem();
		String out1 = outConnection.getSelectedItem();

		inConnection.removeAll();
		outConnection.removeAll();

		int refCount = Midi.CountAppls();

		for (int i = 1; i <= refCount; i++) {
			name = Midi.GetName(Midi.GetIndAppl(i));
			if (!name.equals(TGlobals.appl))
				inConnection.addItem(name);
			if (!name.equals(TGlobals.appl))
				outConnection.addItem(name);
		}

		if (in1 != null) {
			inConnection.select(in1);
		} else {
			inConnection.select(in);
		}
		if (out1 != null) {
			outConnection.select(out1);
		} else {
			outConnection.select(out);
		}
	}

	public void init() {
		setLayout(new GridLayout(4, 2, 10, 5));

		in = TGlobals.context.getInConnection();
		out = TGlobals.context.getOutConnection();
		initMenus();

		inConnection.addItemListener(this);
		outConnection.addItemListener(this);

		inConnection.addMouseListener(this);
		outConnection.addMouseListener(this);

		add(new Label(TGlobals.getTranslation("MidiPort")));
		add(port);

		add(new Label(TGlobals.getTranslation("MidiEvents")));
		add(events);

		add(new Label(TGlobals.getTranslation("Default_Midi_Ouput")));
		add(outConnection);

		add(new Label(TGlobals.getTranslation("Default_Midi_Input")));
		add(inConnection);
	}

	public void update(Observable o, Object arg) {
		int num = Integer.parseInt(port.getText());
		int num1 = MathUtils.setRange(num, 0, 255);
		if (num1 != num)
			port.setText(new Integer(num1).toString());
		TGlobals.context.setPort(num1);

		num = Integer.parseInt(events.getText());
		num1 = MathUtils.setRange(num, 0, 500000);
		if (num1 != num)
			events.setText(new Integer(num1).toString());
		TGlobals.context.setEvents(num1);
	}

	public void itemStateChanged(ItemEvent e) {
		TGlobals.context.setOutConnection(outConnection.getSelectedItem());
		TGlobals.context.setInConnection(inConnection.getSelectedItem());

		int refCount = Midi.CountAppls();
		String name;
		for (int i = 1; i <= refCount; i++) {
			name = Midi.GetName(Midi.GetIndAppl(i));
			if (name.startsWith("Elody")) {
				MidiUtils.connect(name, outConnection.getSelectedItem(), 1);
				MidiUtils.connect(inConnection.getSelectedItem(), name, 1);
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		initMenus();
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}
