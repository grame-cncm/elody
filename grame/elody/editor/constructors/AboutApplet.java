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

import grame.elody.editor.main.Elody;
import grame.elody.editor.misc.applets.Singleton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Panel;

public class AboutApplet extends Singleton {
	public AboutApplet() {
		super("About Elody");
	} 
    public void init() {
		Image img;
		try {
			MediaTracker mTrk = new MediaTracker(this);
			mTrk.addImage (img = getImage (getDocumentBase(), "Images/About.jpg"), 1);
			mTrk.waitForID (1);
			if (mTrk.isErrorID(1)) { img = null; }
		}
		catch (Exception e) { img = null; }				
		int w = 120, h = 100;
		if (img != null) {
			do { w = img.getWidth(null);  } while (w < 0);
			do { h = img.getHeight(null); } while (h < 0);
			h += 32; //50;
			w += 12;
		}
		setSize (w, h);
		setLayout (new BorderLayout());
		add ("Center", new AboutPanel (img));
		add ("South", new VersionPanel ());
		setResizable (false);
		moveFrame (200, 240);
	}   

	public Insets getInsets () {
		return new Insets (0,0,0,0);
	}
}

class VersionPanel extends Panel
{
	public VersionPanel () { 
		super();
		setLayout(new BorderLayout());
    	setFont (new Font("Verdana", Font.PLAIN, 12));
//		add ("North", new Label ("Interface version " + Elody.version(), Label.LEFT));
//		add ("South", new Label ("Engine version " + TGlobals.version(), Label.LEFT));
		add ("West", new Label ("version " + Elody.version(), Label.LEFT));
		add ("East", new Label ("http://www.grame.fr/Elody/", Label.LEFT));
	}
	public Insets getInsets () {
		return new Insets (0,15,0,0);
	}	
}

class AboutPanel extends Panel
{
	Image image; int width, height;

	public AboutPanel (Image img) {
		image = img;
    	if (image == null) {
			setLayout(new BorderLayout());
    		setFont (new Font("Times", Font.BOLD, 24));
			add ("Center", new Label ("Elody", Label.CENTER));
    	}
    	else {
			do { width = image.getWidth(null);  } while (width < 0);
			do { height = image.getHeight(null); } while (height < 0);
    	}
	}
    public void paint (Graphics g) {
    	if (image != null) {
    		Dimension d = getSize();
			int w = width, h = height;
			if (w > d.width)  w = d.width;
			if (h > d.height) h = d.height;
			g.drawImage (image, (d.width-w)/2, (d.height-h)/2, w, h, this);
		}
    }
	
}