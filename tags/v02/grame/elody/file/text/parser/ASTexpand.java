package grame.elody.file.text.parser;

public class ASTexpand extends SimpleNode {
	public ASTexpand(int id) {
		super(id);
	}

	public ASTexpand(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
