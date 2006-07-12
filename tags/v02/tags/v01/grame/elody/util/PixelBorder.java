package grame.elody.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Panel;

public class PixelBorder extends Panel {
	int size = 5;
	
	public  PixelBorder (Component borderme) {
		setLayout(new BorderLayout());
		add("Center", borderme);
	}
	
	public  PixelBorder (Component borderme, int size) {
		setLayout(new BorderLayout());
		add("Center", borderme);
		this.size = size;
	}

	public Insets getInsets() {return new Insets (size,size,size,size);}
}
