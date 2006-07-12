package grame.elody.editor.treeeditor.simplelayouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

//===========================================================================
//la classe HorizontalLayout, layout en colonne ligne centré
//===========================================================================

public class HorizontalLayout implements LayoutManager, Serializable {
    int hgap;
    
	//----------------------------
    // constructeur
    public HorizontalLayout(int hgap)	{ this.hgap = hgap;}
    
    
	//----------------------------
    // preferredLayoutSize : somme des largeurs et plus grande hauteur des preferredSize
	public Dimension preferredLayoutSize(Container target) {

		Dimension dim = new Dimension(0, 0);

		int n = target.getComponentCount();

		for (int i = 0 ; i < n ; i++) {
		    Component m = target.getComponent(i);
		    if (m.isVisible()) {
				Dimension d = m.getPreferredSize();
				dim.width += d.width + hgap;
				dim.height 	= Math.max(dim.height, d.height);
		    }
		}
		Insets insets = target.getInsets();
		dim.width += insets.left + insets.right + hgap;
		dim.height += insets.top + insets.bottom;
		return dim;
	}


	//----------------------------
    // minimumLayoutSize : somme des largeurs et plus grande hauteur des minimumSize
	public Dimension minimumLayoutSize(Container target) {
		//System.out.println("BEGIN minimumLayoutSize (HL)");

		Dimension dim = new Dimension(0, 0);

		int n = target.getComponentCount();

		for (int i = 0 ; i < n ; i++) {
		    Component m = target.getComponent(i);
		    if (m.isVisible()) {
				Dimension d = m.getMinimumSize();
				dim.width += d.width + hgap;
				dim.height 	= Math.max(dim.height, d.height);
		    }
		}
		Insets insets = target.getInsets();
		dim.width += insets.left + insets.right + hgap;
		dim.height += insets.top + insets.bottom;
		//System.out.println("END minimumLayoutSize (HL)");
		return dim;
	}


	//----------------------------
	// layoutContainer : mise en ligne des composants (centrés sur la ligne)
	public void layoutContainer(Container target) {
		Insets insets = target.getInsets();
		int x = insets.left + hgap;
		int y = insets.top;

		int n = target.getComponentCount();
		int h = preferredLayoutSize(target).height;

		for (int i = 0 ; i < n ; i++) {
		    Component m = target.getComponent(i);
		    if (m.isVisible()) {
				Dimension d = m.getPreferredSize();
				m.setSize(d.width, d.height);
				m.setLocation(x, y + (h - d.height) / 2);
				x += d.width + hgap;
			}
		}
	}
	
	public void addLayoutComponent(String name, Component comp) { }
    public void removeLayoutComponent(Component comp) { }

    public String toString() {
		return getClass().getName() + "[hgap=" + hgap + "]";
    }
}
