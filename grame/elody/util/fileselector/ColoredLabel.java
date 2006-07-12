package grame.elody.util.fileselector;

import java.awt.Color;
import java.awt.Label;

public class ColoredLabel extends Label {
	public ColoredLabel (String text, int align, Color color) {
		super (text, align);
		setForeground (color);
	}
}
