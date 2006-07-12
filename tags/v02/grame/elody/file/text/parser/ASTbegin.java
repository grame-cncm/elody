package grame.elody.file.text.parser;

public class ASTbegin extends SimpleNode {
	public ASTbegin(int id) {
		super(id);
	}

	public ASTbegin(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
