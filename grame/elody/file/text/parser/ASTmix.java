package grame.elody.file.text.parser;

public class ASTmix extends SimpleNode {
	public ASTmix(int id) {
		super(id);
	}

	public ASTmix(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
