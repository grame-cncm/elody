package grame.elody.file.text.parser;

public class ASTduration extends SimpleNode {

	float value;

	public void setVal(float val) {
		value = val;
	}

	public ASTduration(int id) {
		super(id);
	}

	public ASTduration(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
