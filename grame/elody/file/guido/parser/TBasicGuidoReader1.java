/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.guido.parser;

public class TBasicGuidoReader1 extends BasicGuidoReader {
	public void GD_INIT_SEGM() { System.out.println("GD_INIT_SEGM");}
	public void GD_EXIT_SEGM(){ System.out.println("GD_EXIT_SEGM");}
	public void GD_APP_SEQ(){ System.out.println("GD_APP_SEQ");}

	public void GD_INIT_SEQ(){ System.out.println("GD_INIT_SEQ");}
	public void GD_EXIT_SEQ(){ System.out.println("GD_EXIT_SEQ");}

	public void GD_NT(String s){ System.out.println("GD_NT " + s);}
	public void GD_SH_NT(){ System.out.println("GD_SH_NT");}
	public void GD_FL_NT(){ System.out.println("GD_FL_NT");}
	public void GD_OCT_NT(String s){ System.out.println("GD_OCT_NT " + s);}
	public void GD_ENUM_NT(String s){ System.out.println("GD_ENUM_NT " + s);}
	public void GD_DENOM_NT(String s){ System.out.println("GD_DENOM_NT " + s);}
	public void GD_DOT_NT(){ System.out.println("GD_DOT_NT");}
	public void GD_DDOT_NT(){ System.out.println("GD_DDOT_NT");}

	public void GD_APP_NT(){ System.out.println("GD_APP_NT");}

	public void GD_INIT_CH(){ System.out.println("GD_INIT_CH");}
	public void GD_CH_APP_NT(){ System.out.println("GD_CH_APP_NT");}
	public void GD_APP_CH(){ System.out.println("GD_APP_CH");}

	public void GD_TAG_START(String s){ System.out.println("GD_TAG_START");}
	public void GD_TAG_END(){ System.out.println("GD_TAG_END");}
	public void GD_TAG_NARG(String s){ System.out.println("GD_TAG_NARG");}
	public void GD_TAG_RARG(String s){ System.out.println("GD_TAG_RARG");}
	public void GD_TAG_SARG(String s){ System.out.println("GD_TAG_SARG");}
	public void GD_TAG_TARG(String s){ System.out.println("GD_TAG_TARG");}  /* don't use this! */
	public void GD_TAG_ADD_ARG(){ System.out.println("GD_TAG_ADD_ARG");}     /* don't use this! */
	public void GD_TAG_ADD(){ System.out.println("GD_TAG_ADD");}
}
