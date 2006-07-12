package grame.elody.file.text.parser;

public class ASTabstraction extends SimpleNode {
	public ASTabstraction(int id) {
		super(id);
	}

	public ASTabstraction(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
