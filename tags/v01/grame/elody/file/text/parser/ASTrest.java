package grame.elody.file.text.parser;

public class ASTrest extends SimpleNode {
	public ASTrest(int id) {
		super(id);
	}

	public ASTrest(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
