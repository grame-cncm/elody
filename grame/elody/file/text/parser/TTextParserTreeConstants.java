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

public interface TTextParserTreeConstants {
	  public int JJTEXPRESSION = 0;
	  public int JJTAPPLICATIONS = 1;
	  public int JJTVOID = 2;
	  public int JJTTRANSFORMED = 3;
	  public int JJTBEGIN = 4;
	  public int JJTREST = 5;
	  public int JJTEXPAND = 6;
	  public int JJTMODIFIED = 7;
	  public int JJTMUTE = 8;
	  public int JJTABSTRACTION = 9;
	  public int JJTVAR = 10;
	  public int JJTSEQUENCE = 11;
	  public int JJTMIX = 12;
	  public int JJTTRANSPOSITION = 13;
	  public int JJTATTENUATION = 14;
	  public int JJTCHANNEL = 15;
	  public int JJTDURATION = 16;
	  public int JJTNOTE = 17;


	  public String[] jjtNodeName = {
	    "expression",
	    "applications",
	    "void",
	    "transformed",
	    "begin",
	    "rest",
	    "expand",
	    "modified",
	    "mute",
	    "abstraction",
	    "var",
	    "sequence",
	    "mix",
	    "transposition",
	    "attenuation",
	    "channel",
	    "duration",
	    "note",
	  };
}
