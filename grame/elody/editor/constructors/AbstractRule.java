package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.AbstractionOp;

public class AbstractRule extends RuleApplet {
    public AbstractRule () { super("Abstraction"); }
    public void init() {
    	super.init();
		add (new RulePanel(new AbstractionOp (getRuleImage("abstr.jpg")),  
					result = new AbstrResultHolder()));
		moveFrame (10, 10);
    }
}
