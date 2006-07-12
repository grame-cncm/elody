package grame.elody.file.text.parser;

public class ASTexpression extends SimpleNode {
	public ASTexpression(int id) {
		super(id);
	}

	public ASTexpression(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
