/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.treeeditor.simplelayouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

//===========================================================================
//la classe VerticalLayout, layout en colonne verticale (justifiée à gauche)
//===========================================================================

public class VerticalLayout implements LayoutManager, Serializable {
    int vgap;	// espace vertical séparant les composants
    
	//----------------------------
    // constructeur
    
    public VerticalLayout(int vgap)	{ this.vgap = vgap;}
    
	//----------------------------
    // preferredLayoutSize : somme des hauteurs et plus grande largeur des preferredSize
    
	public Dimension preferredLayoutSize(Container target) 
	{
		int v = 0;			// compte des composants visibles
		int h = 0;			// total des hauteurs
		int w = 0;			// largeur maximale
		int n = target.getComponentCount();
		
		// calcul du nombre et de l'espace des composants visibles
		for (int i = 0; i < n ; i++) {
		    Component m = target.getComponent(i);
		    if (m.isVisible()) {
				Dimension d = m.getPreferredSize();
		    	v++;
				h = h + d.height;
				w = Math.max(w, d.width);
		    }
		}
		
		// ajout de l'inset et du vgap
		Insets insets = target.getInsets();
		w = w + insets.left + insets.right;
		h = h + insets.top + insets.bottom + ((v==0) ? 0 : (v+1)*vgap);
		return new Dimension(w, h);
	}

	//----------------------------
    // minimumLayoutSize : somme des hauteurs et plus grande largeur des minimumSize

	public Dimension minimumLayoutSize(Container target) 
	{
		int v = 0;			// compte des composants visibles
		int h = 0;			// total des hauteurs
		int w = 0;			// largeur maximale
		
		// calcul du nombre et de l'espace occupé par les composants visibles
		for (int i = 0, n = target.getComponentCount(); i < n ; i++) {
		    Component m = target.getComponent(i);
		    if (m.isVisible()) {
				Dimension d = m.getMinimumSize();
		    	v++;
				h = h + d.height;
				w = Math.max(w, d.width);
		    }
		}
		
		// ajout de l'inset et du vgap
		Insets insets = target.getInsets();
		w = w + insets.left + insets.right;
		h = h + insets.top + insets.bottom + ((v==0) ? 0 : (v+1)*vgap);
		return new Dimension(w, h);
	}

	//----------------------------
	// layoutContainer : mise en colonne des composants (justifiée à gauche)
	// sans modification de la taille actuelle des composants.

	public void layoutContainer(Container target) 
	{
		Insets insets = target.getInsets();

		int x = insets.left;
		int y = insets.top + vgap;

		for (int i = 0, n = target.getComponentCount() ; i < n ; i++) 
		{
		    Component m = target.getComponent(i);
		    if (m.isVisible()) {
				Dimension d = m.getPreferredSize();
				m.setBounds(x, y, d.width, d.height);
				y += d.height + vgap;
			}
		}
	}
	
	public void addLayoutComponent(String name, Component comp) { }
    public void removeLayoutComponent(Component comp) { }

    public String toString() {
		return getClass().getName() + "[vgap=" + vgap + "]";
    }
}
