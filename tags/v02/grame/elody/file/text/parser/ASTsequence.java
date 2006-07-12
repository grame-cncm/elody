package grame.elody.file.text.parser;

public class ASTsequence extends SimpleNode {
	public ASTsequence(int id) {
		super(id);
	}

	public ASTsequence(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
