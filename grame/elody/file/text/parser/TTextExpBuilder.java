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

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.util.Hashtable;


public class TTextExpBuilder implements TTextParserVisitor {
	Hashtable<String, TExp> identTable = new Hashtable<String, TExp>();
	Hashtable<String, String> noteTable = new Hashtable<String, String>();

	public TTextExpBuilder() {
		noteTable.put("c", "0");
		noteTable.put("d", "2");
		noteTable.put("e", "4");
		noteTable.put("f", "5");
		noteTable.put("g", "7");
		noteTable.put("a", "9");
		noteTable.put("b", "11");
	}

  	public Object visit(SimpleNode node, Object data){
  		//System.out.println("SimpleNode " + node.jjtGetNumChildren());
  		return node.jjtGetChild(0).jjtAccept(this,data);
   	}
   
   	public Object visit(ASTexpression node, Object data){
  		////System.out.println("ASTexpression " + node.jjtGetNumChildren());
  		return node.jjtGetChild(0).jjtAccept(this,data);
   	}
     
  	public Object visit(ASTapplications node, Object data){
  		//System.out.println("ASTapplication " + node.jjtGetNumChildren());
  		
  		TExp result = TExpMaker.gExpMaker.createNull();
  		for (int i = node.jjtGetNumChildren() - 1; i >= 0 ; i--) {
  			result = TExpMaker.gExpMaker.createAppl((TExp)node.jjtGetChild(i).jjtAccept(this,data),result);
  		} 
  		
  		return result;
  	}
  
  	public Object visit(ASTbegin node, Object data){
  		//System.out.println("ASTbegin " + node.jjtGetNumChildren());
  		return node.jjtGetChild(0).jjtAccept(this,data);
  	}
  
  
  	public Object visit(ASTrest node, Object data){
  		//System.out.println("ASTrest " + node.jjtGetNumChildren());
  		return node.jjtGetChild(0).jjtAccept(this,data);
  	}
  
  	public Object visit(ASTexpand node, Object data){
  		//System.out.println("ASTexpand " + node.jjtGetNumChildren());
  		return node.jjtGetChild(0).jjtAccept(this,data);
  	}
  
  	public Object visit(ASTmodified node, Object data){
  		//System.out.println("ASTmodified " + node.jjtGetNumChildren());
  		TExp result = (TExp)node.jjtGetChild(0).jjtAccept(this,data);
  		//System.out.println(result);
  		for (int i = 1 ; i < node.jjtGetNumChildren(); i++) {   // associativité à gauche
  			Node node1 = node.jjtGetChild(i);
  			//System.out.println(node1);
  			if (node1 instanceof ASTtransposition) result =  TExpMaker.gExpMaker.createTrsp(result,((ASTtransposition)node1).value);
  			if (node1 instanceof ASTattenuation) result =  TExpMaker.gExpMaker.createAttn(result,((ASTattenuation)node1).value);
  			if (node1 instanceof ASTchannel) result =  TExpMaker.gExpMaker.createSetch(result,((ASTchannel)node1).value);
  			if (node1 instanceof ASTduration) {
  				//System.out.println ("duration " + ((ASTduration)node1).value);
  				 result =  TExpMaker.gExpMaker.createExpv(result,((ASTduration)node1).value);
  			}
  		} 
  		return result;
  	}
  	
  	public Object visit(ASTtransformed node, Object data){
  		//System.out.println("ASTtransformed " + node.jjtGetNumChildren());
  		TExp result = (TExp)node.jjtGetChild(0).jjtAccept(this,data);
  		//System.out.println(result);
  		for (int i = 1 ; i < node.jjtGetNumChildren(); i++) {  // associativité à gauche
  			Node node1 = node.jjtGetChild(i);
  			TExp result1 = (TExp)node1.jjtAccept(this,data);
  			////System.out.println(node1);
  			if (node1 instanceof ASTbegin) result =  TExpMaker.gExpMaker.createBegin(result,result1);
  			if (node1 instanceof ASTrest) result =  TExpMaker.gExpMaker.createRest(result,result1);
  			if (node1 instanceof ASTexpand) result =  TExpMaker.gExpMaker.createDilate(result,result1);
  		} 
  		return result;
  	}

  	
  	 public Object visit(ASTabstraction node, Object data){
  		//System.out.println("ASTabstraction " + node.jjtGetNumChildren());
  		TExp arg, body, ident = (TExp)node.jjtGetChild(0).jjtAccept(this,data);
  		
  		if (node.jjtGetNumChildren() == 2 ) { // pas d'argument spécifié
  			arg = TExpMaker.gExpMaker.createNote(Color.blue, 60,100,0,1000);
  			body = (TExp)node.jjtGetChild(1).jjtAccept(this,data);
  		}else {
  			arg = (TExp)node.jjtGetChild(1).jjtAccept(this,data);
  			body = (TExp)node.jjtGetChild(2).jjtAccept(this,data);
  		}
  		
  		return TExpMaker.gExpMaker.createLambda(ident,arg,body);
  	}

  
  	public Object visit(ASTvar node, Object data){
  		TExp ident;
  		if ((ident = identTable.get(node.name)) != null) {
  			return ident;
  		}else {
  			ident = TExpMaker.gExpMaker.createIdent(null);
  			identTable.put(node.name,ident);
  			return ident;
  		}
  	}
   
  	public Object visit(ASTsequence node, Object data){
  		//System.out.println("Sequence " + node.jjtGetNumChildren());
  		TExp result = TExpMaker.gExpMaker.createNull();
  		for (int i = node.jjtGetNumChildren() - 1; i >= 0 ; i--) {  // associativité à droite
  			result = TExpMaker.gExpMaker.createSeq((TExp)node.jjtGetChild(i).jjtAccept(this,data),result);
  		} 
  		return result;
  	}
  
  	public Object visit(ASTmix node, Object data){
  		//System.out.println("Mix " + node.jjtGetNumChildren());
  		TExp result = TExpMaker.gExpMaker.createNull();
  		for (int i = node.jjtGetNumChildren() - 1; i >= 0 ; i--) {   // associativité à droite
  			result = TExpMaker.gExpMaker.createMix((TExp)node.jjtGetChild(i).jjtAccept(this,data),result);
  		} 
  		return result;
  	}
  
 	public Object visit(ASTtransposition node, Object data){ return null;}
  	public Object visit(ASTattenuation node, Object data){ return null;}
  	public Object visit(ASTchannel node, Object data){ return null;}
  	public Object visit(ASTduration node, Object data){ return null;}
  
  	public Object visit(ASTnote node, Object data){
  		//System.out.println("Note " + node.jjtGetNumChildren());
  		if (node.name.equals("_")) /* SILENCE */
  			return TExpMaker.gExpMaker.createSilence(Color.blue,0,0,0,4000);
  		else
  			return TExpMaker.gExpMaker.createNote( Color.blue,Integer.parseInt(noteTable.get(node.name)),100,0,4000);
  	}
  	
  	public Object visit(ASTmute node, Object data){
  		//System.out.println("ASTmute " + node.jjtGetNumChildren());
  		return TExpMaker.gExpMaker.createMute( (TExp)node.jjtGetChild(0).jjtAccept(this,data));
   	}
}
