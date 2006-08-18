package grame.elody.editor.constructors;

import grame.elody.editor.constructors.parametrer.ParamExprHolder;
import grame.elody.editor.constructors.parametrer.ParamPanel;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.BorderLayout;

public class Parametrer extends BasicApplet {
	static final String appletName = "Parametrer";
	ParamPanel param;
	
	public Parametrer() {
		super(TGlobals.getTranslation("Parametrer"));
		setLayout(new BorderLayout());
		setSize (180, 260);
	} 
    public void init() {
		Define.getButtons(this);
    	param = new ParamPanel(true);
    	param.init (new ParamExprHolder(), 60, 60);
		add("Center", param);
		moveFrame (200, 240);
	}   
	public void decompose (TExp exp) {
		param.decompose (exp);
	}
}
