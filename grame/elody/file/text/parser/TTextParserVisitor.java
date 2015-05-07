/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.text.parser;

public interface TTextParserVisitor {
	  public Object visit(SimpleNode node, Object data);
	  public Object visit(ASTexpression node, Object data);
	  public Object visit(ASTapplications node, Object data);
	  public Object visit(ASTtransformed node, Object data);
	  public Object visit(ASTbegin node, Object data);
	  public Object visit(ASTrest node, Object data);
	  public Object visit(ASTexpand node, Object data);
	  public Object visit(ASTmodified node, Object data);
	  public Object visit(ASTmute node, Object data);
	  public Object visit(ASTabstraction node, Object data);
	  public Object visit(ASTvar node, Object data);
	  public Object visit(ASTsequence node, Object data);
	  public Object visit(ASTmix node, Object data);
	  public Object visit(ASTtransposition node, Object data);
	  public Object visit(ASTattenuation node, Object data);
	  public Object visit(ASTchannel node, Object data);
	  public Object visit(ASTduration node, Object data);
	  public Object visit(ASTnote node, Object data);
}
