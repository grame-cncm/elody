package grame.elody.file.text.parser;

public class ASTmodified extends SimpleNode {
	public ASTmodified(int id) {
		super(id);
	}

	public ASTmodified(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
