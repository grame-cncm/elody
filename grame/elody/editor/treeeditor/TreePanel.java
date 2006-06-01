package grame.elody.editor.treeeditor;

import grame.elody.editor.treeeditor.simplelayouts.IndentedLayout;
import grame.elody.editor.treeeditor.texpeditor.TExpEditor;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Panel;

//===========================================================================
//TreePanel : affiche le Header et, à la demande, les fils
//===========================================================================

public class TreePanel extends Panel  implements Openable, ExpDropObserver
{
	static LayoutManager gIndentedLayout = new IndentedLayout(4, 24);

	TreePanel 	fFather;
	int			fSonNumber;
	HeaderPanel fHeader;
	TExpEditor	fEditor;
	Color		fColor;
	
	public TreePanel(TreePanel father, int sonNumber, TExp exp)
	{
		this(father, sonNumber, exp, Color.black);
	}
	public TreePanel(TreePanel father, int sonNumber, TExp exp, Color color)
	{
		setLayout(gIndentedLayout);
		fFather = father;
		fSonNumber = sonNumber;
		fColor = color;
		try {
			installExpression(exp);
		} catch (Exception e) {
			System.err.println("TreePanel construction error : " + e );
		}
	}
	
	void installExpression(TExp exp) 
	{
		removeAll();
		fEditor = TExpEditor.makeEditor(exp);
		fHeader = new HeaderPanel(this, false, exp, fEditor.getKindName(), fColor);
		//fHeader = new HeaderPanel(this, false, exp, TGlobals.renderer.getText(exp), fColor);
		add(fHeader);
	}
			
	final public void updateFather(TExp exp) 
		{ if (fFather != null) fFather.updateSubExpression(exp, fSonNumber); }
		
		
	final void ValidateContainers()
	{
		Container src = this;
		while ( src != null ) {
			src.validate();
			src = src.getParent();
		}
	}

    public void paint(Graphics g) 
    {
		Dimension d = getSize();
		g.drawRect(0, 0, d.width-1, d.height-1);
		super.paint(g);
    }
    
    public Insets getInsets() {
		return new Insets(2, 2, 2, 2);
    }

	//---------------------------
	// methodes appelés par le header pour controler l'ouverture, 
	// la fermeture et le changement d'expression
	
	final public void open() 						// ouverture
	{ 
		try {
			fEditor.addSonsTo(this); 
			ValidateContainers(); 
		} catch (Exception e) {
			System.err.println("TreePanel open error : " + e );
		}
	}
		
	final public void close() 						// fermeture
	{ 
		try {
			removeAll(); 
			add(fHeader); 
			ValidateContainers(); 
		} catch (Exception e) {
			System.err.println("TreePanel close error : " + e );
		}
	}
		
	final public void dropExpression(TExp exp) 		// drop d'une nouvelle expression
	{ 
		try {
			removeAll(); 
			installExpression(exp); 
			updateFather(exp); 
			ValidateContainers(); 
		} catch (Exception e) {
			System.err.println("TreePanel close error : " + e );
		}
	}

	
	//---------------------------
	// méthodes appelées par les sous arbres de celui-ci pour l'updater

	final public void updateSubExpression(TExp subexp, int norder)
	{
		try {
			TExp exp = fEditor.modifySubExpression(subexp, norder);
			fHeader.updateHeader(exp, fEditor.getKindName());
			updateFather(exp);
		} catch (Exception e) {
			System.err.println("TreePanel updateSubExpression error : " + e );
		}
	}

	final public void updateSubString(String str, int norder)
	{
		try {
			TExp exp = fEditor.modifySubString(str, norder);
			fHeader.updateHeader(exp, fEditor.getKindName());
			updateFather(exp);
		} catch (Exception e) {
			System.err.println("TreePanel updateSubString error : " + e );
		}
	}

	final public void updateSubColor(Color c, int norder)
	{
		try {
			TExp exp = fEditor.modifySubColor(c, norder);
			fHeader.updateHeader(exp, fEditor.getKindName());
			updateFather(exp);
		} catch (Exception e) {
			System.err.println("TreePanel updateSubString error : " + e );
		}
	}
}

