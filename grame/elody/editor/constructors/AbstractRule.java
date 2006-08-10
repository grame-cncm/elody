package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.AbstractionOp;
import grame.elody.editor.misc.TGlobals;

public class AbstractRule extends RuleApplet {
    public AbstractRule () { super(TGlobals.getTranslation("Abstraction")); }
    public void init() {
    	super.init();
		add (new RulePanel(new AbstractionOp (getRuleImage("abstr.png")),  
					result = new AbstrResultHolder()));
		moveFrame (10, 10);
    }
}
