package grame.elody.file.text.parser;

public class ASTmute extends SimpleNode {
	public ASTmute(int id) {
		super(id);
	}

	public ASTmute(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
