package grame.elody.file.text.parser;

public class ASTtransposition extends SimpleNode {

	float value;

	public void setVal(float val) {
		value = val;
	}

	public ASTtransposition(int id) {
		super(id);
	}

	public ASTtransposition(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
