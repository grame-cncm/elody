package grame.elody.editor.treeeditor;

import grame.elody.editor.treeeditor.simplelayouts.HorizontalLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;

public class ColorPanel extends Panel implements ColorObserver {
	static LayoutManager 	gHorizontalLayout = new HorizontalLayout(4);
	static Font				gFont = new Font("Monospaced", Font.PLAIN, 10);
	
	TreePanel 		fFather;
	int				fSonNumber;
	Label			fLabel;
	ColorZone		fColorZone;
	
	public ColorPanel (TreePanel father, int sonNumber, String kind, Color color)
	{
		setLayout(gHorizontalLayout);
		setFont(gFont);
		fFather 	= father;
		fSonNumber 	= sonNumber;
		fLabel		= new Label(kind);
		fColorZone	= new ColorZone (this, color);
		add(fLabel);
		add(fColorZone);
	}
	public void dropColor(Color c)
	{
		fFather.updateSubColor (c, fSonNumber);
	}
}
