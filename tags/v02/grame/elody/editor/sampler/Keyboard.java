package grame.elody.editor.sampler;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.swtdesigner.SWTResourceManager;

public class Keyboard extends Canvas {
/***** DESCRIPTION ************************************
 * Each "Keyboard" instance (identified by its parent channel)
 * provides a canvas with the representation of a MIDI keyboard (127 keys)
 * and all the keygroups. Each keygroup representation has a border,
 * a filling color excepted for the reference key that is in a different
 * color and holds the keygroup number. Note that this class has no public
 * method: the keyboard viewer is indirectly modified when any channel's
 * keygroup is modified.
 ******************************************************/	
	final private Color LIGHT_RED = new Color(this.getDisplay(),255,128,128);
	final private Color DARK_RED = new Color(this.getDisplay(),128,0,0);
	final private Color LIGHT_BLUE = new Color(this.getDisplay(),192,192,255);
	final private Color DARK_BLUE = new Color(this.getDisplay(),64,64,100);
	final private Color YELLOW = new Color(this.getDisplay(),255,255,0);
	final private Color WHITE = new Color(this.getDisplay(),255,255,255);
	final private Color BLACK = new Color(this.getDisplay(),0,0,0);
	
	final private Color WHITE_KEY_SELECTED = LIGHT_BLUE;
	final private Color BLACK_KEY_SELECTED = DARK_BLUE;
	final private Color WHITE_KEY_ISREF = LIGHT_RED;
	final private Color BLACK_KEY_ISREF = DARK_RED;
	final private Color TEXT_AND_BORDER = YELLOW;
	
	final short SELECTED = 0;
	final short ISREF = 1;
	
	private Channel ch;
	private Image image;
	
	public Keyboard(Composite parent, int style, Channel channel) {
		super(parent, style);
		ch = channel;
		image = new Image(this.getDisplay(), "Images/clavierMidi.bmp");
		setSize(978, 80);
		setLocation(0, 0);
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, 0, 0);
				Vector keygroups = ch.getKeygroups();
					for(int i=0; i<keygroups.size(); i++)
					{
						Keygroup k = (Keygroup) keygroups.get(i);
						drawKeygroup(k, e.gc);
						
					}
			}
		});
	}
	
	protected void drawKeygroup(Keygroup k, GC gc)
	{
		int index = k.getIndex();
		int ref = k.getRef();
		int plus = k.getPlus();
		int minus = k.getMinus();
		for (int i = ref-minus; i<=ref+plus; i++)
		{
			int mode = SELECTED;
			if (i==ref) { mode = ISREF; }
			fillKey(i,mode,gc);
		}
		borderGroup(ref-minus, ref+plus, gc);
		textGroup(ref, index, gc);
	}
	
	protected void textGroup(int ref, int keygroupIndex, GC gc)
	{
		Transform t = new Transform(this.getDisplay());
		int x = getKeyX(ref)-2;
		int y = 60;
		if (getKeyType(ref)==3)	{ y = 35; }
		else if (getKeyType(ref)==0) { x += 2; }
		t.translate(x,y);
		t.rotate(-90);
		gc.setTransform(t);
		gc.setFont(SWTResourceManager.getFont("", 7, SWT.NORMAL));
		gc.setForeground(TEXT_AND_BORDER);
		gc.drawString(String.valueOf(keygroupIndex),0,0, true);
		gc.setTransform(null);
		t.dispose();
	}
	
	protected void borderGroup(int minPitch, int maxPitch, GC gc)
	{
		final Color EXTERIOR = BLACK;
		final Color MIDDLE = TEXT_AND_BORDER;
		final Color INTERIOR = BLACK;
		if ((minPitch==maxPitch)&&(getKeyType(minPitch)==3))
		//particular case: keyGroup is a single black key 
		{
			gc.setForeground(EXTERIOR);
			gc.drawRectangle(getKeyX(minPitch)-2,0,10,41);
			gc.setForeground(MIDDLE);
			gc.drawRectangle(getKeyX(minPitch)-1,1,8,39);
			gc.setForeground(INTERIOR);
			gc.drawRectangle(getKeyX(minPitch),2,6,37);			
		}
		else
		{
			Point A1 = new Point(getKeyX(minPitch)-1,0);
			Point A2 = new Point(getKeyX(minPitch),1);
			Point A3 = new Point(getKeyX(minPitch)+1,2);
			Point B1 = new Point(getKeyX(minPitch)+2,65);
			Point B2 = new Point(getKeyX(minPitch)+3,64);
			Point B3 = new Point(getKeyX(minPitch)+4,63);
			switch (getKeyType(minPitch)) {
			case 0:
				Point U01 = new Point(A1.x-1,A1.y);
				Point U02 = new Point(A2.x-1,A2.y);
				Point U03 = new Point(A3.x-1,A3.y);
				Point V01 = new Point(B1.x-4, B1.y);
				Point V02 = new Point(B2.x-4, B2.y);
				Point V03 = new Point(B3.x-4, B3.y);
				int[] L01 = {A1.x, A1.y, U01.x, U01.y, V01.x, V01.y, B1.x, B1.y};
				int[] L02 = {A2.x, A2.y, U02.x, U02.y, V02.x, V02.y, B2.x, B2.y};
				int[] L03 = {A3.x, A3.y, U03.x, U03.y, V03.x, V03.y, B3.x, B3.y};
				gc.setForeground(EXTERIOR);
				gc.drawPolyline(L01);
				gc.setForeground(MIDDLE);
				gc.drawPolyline(L02);
				gc.setForeground(INTERIOR);
				gc.drawPolyline(L03);
				break;
			case 1:
			case 2:
				Point U11 = new Point(A1.x,A1.y+39);
				Point U12 = new Point(A2.x,A2.y+39);
				Point U13 = new Point(A3.x,A3.y+39);
				Point V11 = new Point(A1.x-4,A1.y+39);
				Point V12 = new Point(A2.x-4,A2.y+39);
				Point V13 = new Point(A3.x-4,A3.y+39);
				Point W11 = new Point(B1.x-7,B1.y);
				Point W12 = new Point(B2.x-7,B2.y);
				Point W13 = new Point(B3.x-7,B3.y);
				int[] L11 = {A1.x, A1.y, U11.x, U11.y, V11.x, V11.y, W11.x, W11.y, B1.x, B1.y};
				int[] L12 = {A2.x, A2.y, U12.x, U12.y, V12.x, V12.y, W12.x, W12.y, B2.x, B2.y};
				int[] L13 = {A3.x, A3.y, U13.x, U13.y, V13.x, V13.y, W13.x, W13.y, B3.x, B3.y};
				gc.setForeground(EXTERIOR);
				gc.drawPolyline(L11);
				gc.setForeground(MIDDLE);
				gc.drawPolyline(L12);
				gc.setForeground(INTERIOR);
				gc.drawPolyline(L13);
				break;
			case 3:
				Point U31 = new Point(A1.x-1,A1.y);
				Point U32 = new Point(A2.x-1,A2.y);
				Point U33 = new Point(A3.x-1,A3.y);
				Point V31 = new Point(A1.x-1,B1.y-24);
				Point V32 = new Point(A2.x-1,B2.y-24);
				Point V33 = new Point(A3.x-1,B3.y-24);
				Point W31 = new Point(A1.x+3,B1.y-24);
				Point W32 = new Point(A2.x+3,B2.y-24);
				Point W33 = new Point(A3.x+3,B3.y-24);
				Point X31 = new Point(A1.x+3,B1.y);
				Point X32 = new Point(A2.x+3,B2.y);
				Point X33 = new Point(A3.x+3,B3.y);
				int[] L31 = {A1.x, A1.y, U31.x, U31.y, V31.x, V31.y, W31.x, W31.y, X31.x, X31.y, B1.x, B1.y};
				int[] L32 = {A2.x, A2.y, U32.x, U32.y, V32.x, V32.y, W32.x, W32.y, X32.x, X32.y, B2.x, B2.y};
				int[] L33 = {A3.x, A3.y, U33.x, U33.y, V33.x, V33.y, W33.x, W33.y, X33.x, X33.y, B3.x, B3.y};
				gc.setForeground(EXTERIOR);
				gc.drawPolyline(L31);
				gc.setForeground(MIDDLE);
				gc.drawPolyline(L32);
				gc.setForeground(INTERIOR);
				gc.drawPolyline(L33);
				break;
			default:
				break;
			}
			Point C1 = new Point(getKeyX(maxPitch)+6,0);
			Point C2 = new Point(getKeyX(maxPitch)+5,1);
			Point C3 = new Point(getKeyX(maxPitch)+4,2);
			Point D1 = new Point(getKeyX(maxPitch)+4,65);
			Point D2 = new Point(getKeyX(maxPitch)+3,64);
			Point D3 = new Point(getKeyX(maxPitch)+2,63);
			switch (getKeyType(maxPitch)) {
			case 0:
				Point G01 = new Point(C1.x+3,C1.y);
				Point G02 = new Point(C2.x+3,C2.y);
				Point G03 = new Point(C3.x+3,C3.y);
				Point H01 = new Point(C1.x+3, C1.y+39);
				Point H02 = new Point(C2.x+3, C2.y+39);
				Point H03 = new Point(C3.x+3, C3.y+39);
				Point I01 = new Point(C1.x+7, C1.y+39);
				Point I02 = new Point(C2.x+7, C2.y+39);
				Point I03 = new Point(C3.x+7, C3.y+39);
				Point J01 = new Point(D1.x+9, D1.y);
				Point J02 = new Point(D2.x+9, D2.y);
				Point J03 = new Point(D3.x+9, D3.y);
				int[] M01 = {C1.x, C1.y, G01.x, G01.y, H01.x, H01.y, I01.x, I01.y, J01.x, J01.y, D1.x, D1.y};
				int[] M02 = {C2.x, C2.y, G02.x, G02.y, H02.x, H02.y, I02.x, I02.y, J02.x, J02.y, D2.x, D2.y};
				int[] M03 = {C3.x, C3.y, G03.x, G03.y, H03.x, H03.y, I03.x, I03.y, J03.x, J03.y, D3.x, D3.y};
				gc.setForeground(EXTERIOR);
				gc.drawPolyline(M01);
				gc.setForeground(MIDDLE);
				gc.drawPolyline(M02);
				gc.setForeground(INTERIOR);
				gc.drawPolyline(M03);
				break;
			case 1:
				Point G11 = new Point(C1.x+4,C1.y);
				Point G12 = new Point(C2.x+4,C2.y);
				Point G13 = new Point(C3.x+4,C3.y);
				Point H11 = new Point(D1.x+6, D1.y);
				Point H12 = new Point(D2.x+6, D2.y);
				Point H13 = new Point(D3.x+6, D3.y);
				int[] M11 = {C1.x, C1.y, G11.x, G11.y, H11.x, H11.y, D1.x, D1.y};
				int[] M12 = {C2.x, C2.y, G12.x, G12.y, H12.x, H12.y, D2.x, D2.y};
				int[] M13 = {C3.x, C3.y, G13.x, G13.y, H13.x, H13.y, D3.x, D3.y};
				gc.setForeground(EXTERIOR);
				gc.drawPolyline(M11);
				gc.setForeground(MIDDLE);
				gc.drawPolyline(M12);
				gc.setForeground(INTERIOR);
				gc.drawPolyline(M13);
				break;
			case 2:
				Point G21 = new Point(C1.x,C1.y+39);
				Point G22 = new Point(C2.x,C2.y+39);
				Point G23 = new Point(C3.x,C3.y+39);
				Point H21 = new Point(C1.x+4, C1.y+39);
				Point H22 = new Point(C2.x+4, C2.y+39);
				Point H23 = new Point(C3.x+4, C3.y+39);
				Point I21 = new Point(D1.x+6, D1.y);
				Point I22 = new Point(D2.x+6, D2.y);
				Point I23 = new Point(D3.x+6, D3.y);
				int[] M21 = {C1.x, C1.y, G21.x, G21.y, H21.x, H21.y, I21.x, I21.y, D1.x, D1.y};
				int[] M22 = {C2.x, C2.y, G22.x, G22.y, H22.x, H22.y, I22.x, I22.y, D2.x, D2.y};
				int[] M23 = {C3.x, C3.y, G23.x, G23.y, H23.x, H23.y, I23.x, I23.y, D3.x, D3.y};
				gc.setForeground(EXTERIOR);
				gc.drawPolyline(M21);
				gc.setForeground(MIDDLE);
				gc.drawPolyline(M22);
				gc.setForeground(INTERIOR);
				gc.drawPolyline(M23);
				break;
			case 3:
				Point G31 = new Point(C1.x+2,C1.y);
				Point G32 = new Point(C2.x+2,C2.y);
				Point G33 = new Point(C3.x+2,C3.y);
				Point H31 = new Point(D1.x+4, D1.y-24);
				Point H32 = new Point(D2.x+4, D2.y-24);
				Point H33 = new Point(D3.x+4, D3.y-24);
				Point I31 = new Point(D1.x, D1.y-24);
				Point I32 = new Point(D2.x, D2.y-24);
				Point I33 = new Point(D3.x, D3.y-24);
				int[] M31 = {C1.x, C1.y, G31.x, G31.y, H31.x, H31.y, I31.x, I31.y, D1.x, D1.y};
				int[] M32 = {C2.x, C2.y, G32.x, G32.y, H32.x, H32.y, I32.x, I32.y, D2.x, D2.y};
				int[] M33 = {C3.x, C3.y, G33.x, G33.y, H33.x, H33.y, I33.x, I33.y, D3.x, D3.y};
				gc.setForeground(EXTERIOR);
				gc.drawPolyline(M31);
				gc.setForeground(MIDDLE);
				gc.drawPolyline(M32);
				gc.setForeground(INTERIOR);
				gc.drawPolyline(M33);
				break;
			default:
				break;
			}
			gc.setForeground(EXTERIOR);
			gc.drawLine(A1.x, A1.y, C1.x, C1.y);
			gc.drawLine(B1.x, B1.y, D1.x, D1.y);
			gc.setForeground(MIDDLE);
			gc.drawLine(A2.x, A2.y, C2.x, C2.y);
			gc.drawLine(B2.x, B2.y, D2.x, D2.y);		
			gc.setForeground(INTERIOR);
			gc.drawLine(A3.x, A3.y, C3.x, C3.y);
			gc.drawLine(B3.x, B3.y, D3.x, D3.y);
		}
	}
	
	protected void fillKeyGroup(int ref, int plus, int minus, GC gc)
	{
		for(int i=ref-minus; i<= ref+plus; i++)
		{
			int mode = (i==ref) ? ISREF : SELECTED;
			fillKey(i,mode,gc);
		}
	}
	
	protected void fillKey(int pitch, int mode, GC gc)
	{
		int x = getKeyX(pitch);
		int type = getKeyType(pitch);
		Color bg = WHITE;
		
		switch (type) {
		case 0:
			if (mode==SELECTED)	{ bg=WHITE_KEY_SELECTED; }
			else if (mode==ISREF)	{ bg=WHITE_KEY_ISREF; }
			gc.setBackground(bg);
			gc.fillRectangle(x,2,9,62);
			gc.fillRectangle(x+9,40,3,24);
			break;
		case 1:
			if (mode==SELECTED)	{ bg=WHITE_KEY_SELECTED; }
			else if (mode==ISREF)	{ bg=WHITE_KEY_ISREF; }
			gc.setBackground(bg);
			gc.fillRectangle(x,2,9,62);
			gc.fillRectangle(x-3,40,3,24);
			break;
		case 2:
			if (mode==SELECTED)	{ bg=WHITE_KEY_SELECTED; }
			else if (mode==ISREF)	{ bg=WHITE_KEY_ISREF; }
			gc.setBackground(bg);
			gc.fillRectangle(x,2,6,38);
			gc.fillRectangle(x-3,40,12,24);			
			break;
		case 3:
			if (mode==SELECTED)	{ bg=BLACK_KEY_SELECTED; }
			else if (mode==ISREF)	{ bg=BLACK_KEY_ISREF; }
			gc.setBackground(bg);
			gc.fillRectangle(x,2,7,38);			
			break;
		default:
			break;
		}
	}
	
	protected int getKeyX(int pitch)
	/* return top-left key x coordinate */
	{
		int x=2;
		switch (pitch % 12) {
		case 11: x+=7;
		case 10: x+=6;
		case 9:  x+=7;
		case 8:  x+=6;
		case 7:  x+=7;
		case 6:  x+=9;
		case 5:  x+=10;
		case 4:  x+=7;
		case 3:	 x+=6;
		case 2:	 x+=7;
		case 1:	 x+=9;
		}
		x+=91*(pitch / 12);
		return x;
	}
	
	protected int getKeyType(int pitch)
	/* returns the shape type of the key following these rules :
	 * 0 = C or F
	 * 1 = B or E
	 * 2 = D or G or A
	 * 3 = black key
	 */
	{
		if (pitch==127)
			return 1;
		switch (pitch % 12) {
		case 0:
		case 5:
			return 0;
		case 4:
		case 11:
			return 1;
		case 2:
		case 7:
		case 9:
			return 2;
		case 1:
		case 3:
		case 6:
		case 8:
		case 10:
			return 3;
		default:
			return -1;
		}

	}
	
}
