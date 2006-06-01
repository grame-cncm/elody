package grame.elody.file.text.parser;

public class ASTnote extends SimpleNode {

	String name;

	public void setName(String val) {
		name = val;
	}

	public ASTnote(int id) {
		super(id);
	}

	public ASTnote(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
