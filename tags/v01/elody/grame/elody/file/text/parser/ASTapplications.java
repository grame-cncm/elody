package grame.elody.file.text.parser;

public class ASTapplications extends SimpleNode {
	public ASTapplications(int id) {
		super(id);
	}

	public ASTapplications(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
