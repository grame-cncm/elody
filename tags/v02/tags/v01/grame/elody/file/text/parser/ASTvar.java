package grame.elody.file.text.parser;

public class ASTvar extends SimpleNode {

	String name;

	public void setName(String val) {
		name = val;
	}

	public ASTvar(int id) {
		super(id);
	}

	public ASTvar(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
