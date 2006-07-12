package grame.elody.editor.treeeditor.simplelayouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

//===========================================================================
//la classe IndentedLayout, layout en colonne verticale avec indentation
//===========================================================================

public class IndentedLayout implements LayoutManager, Serializable {
	int vgap; // espace vertical séparant les composants

	int hindent; // indentation horizontale de tous les composants sauf le premier

	//----------------------------
	// constructeur

	public IndentedLayout(int vgap, int indent) {
		this.vgap = vgap;
		hindent = indent;
	}

	//----------------------------
	// preferredLayoutSize : somme des hauteurs et plus grande largeur des preferredSize

	public Dimension preferredLayoutSize(Container target) {
		int v = 0; // compte des composants visibles
		int h = 0; // total des hauteurs
		int w = 0; // largeur maximale
		int chi = 0; // indentation horizontale courante (0 puis hindent)

		// calcul du nombre et de l'espace des composants visibles
		for (int i = 0, n = target.getComponentCount(); i < n; i++) {
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				Dimension d = m.getPreferredSize();
				v++;
				h = h + d.height;
				w = Math.max(w, d.width + chi);
			}
			chi = hindent;
		}

		// ajout de l'inset et du vgap
		Insets insets = target.getInsets();
		w = w + insets.left + insets.right;
		h = h + insets.top + insets.bottom + ((v == 0) ? 0 : (v + 1) * vgap);
		return new Dimension(w, h);
	}

	//----------------------------
	// minimumLayoutSize : somme des hauteurs et plus grande largeur des minimumSize

	public Dimension minimumLayoutSize(Container target) {
		int v = 0; // compte des composants visibles
		int h = 0; // total des hauteurs
		int w = 0; // largeur maximale
		int chi = 0; // indentation horizontale courante (0 puis hindent)

		// calcul du nombre et de l'espace des composants visibles
		for (int i = 0, n = target.getComponentCount(); i < n; i++) {
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				Dimension d = m.getMinimumSize();
				v++;
				h = h + d.height;
				w = Math.max(w, d.width + chi);
			}
			chi = hindent;
		}

		// ajout de l'inset et du vgap
		Insets insets = target.getInsets();
		w = w + insets.left + insets.right;
		h = h + insets.top + insets.bottom + ((v == 0) ? 0 : (v + 1) * vgap);
		return new Dimension(w, h);
	}

	//----------------------------
	// layoutContainer : mise en colonne des composants (indentés à gauche)
	// sans modification de la taille actuelle des composants.

	public void layoutContainer(Container target) {
		Insets insets = target.getInsets();

		int x = insets.left; // pos horz pour le premier
		int x2 = insets.left + hindent; // pos horz pour les suivants
		int y = insets.top + vgap; // pos vert

		for (int i = 0, n = target.getComponentCount(); i < n; i++) {
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				Dimension d = m.getPreferredSize();
				m.setBounds(x, y, d.width, d.height);
				y += d.height + vgap;
			}
			x = x2; // pour indenter tous les suivants
		}
	}

	public void addLayoutComponent(String name, Component comp) {
	}

	public void removeLayoutComponent(Component comp) {
	}

	public String toString() {
		return getClass().getName() + "[vgap=" + vgap + "]";
	}
}
