package grame.elody.file.text.parser;

public class ASTtransformed extends SimpleNode {
	public ASTtransformed(int id) {
		super(id);
	}

	public ASTtransformed(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
