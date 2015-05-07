/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.html.parser;

import java.io.IOException;
import java.io.InputStream;

public final class THtmlStreamTokenizer {
	private HtmlStreamTokenizer tok;

	public THtmlStreamTokenizer(InputStream in) {
		tok = new HtmlStreamTokenizer(in);
	}

	/*
	 * retourne <UL> ,</UL> , <HTML> et </HTML>, les chaines de caractères :
	 * fonction du language, nom d'une expression nommée, ou argument d'un event
	 */

	public String NextToken() {
		int ttype;

		try {
			tok.NextToken();
			ttype = tok.GetTokenType();

			switch (ttype) {

			case HtmlStreamTokenizer.TT_TAG:
				try {
					HtmlTag tag = HtmlTag.ParseTag(tok.GetStringValue());

					switch (tag.GetTagType()) {

					case HtmlTag.T_UL:
						return tok.GetStringValue().toString();

					case HtmlTag.T_HTML:
						return tok.GetStringValue().toString();

					case HtmlTag.T_A:
						String value = tag.GetParam(HtmlTag.P_HREF);
						if (value != null) {
							// System.out.println("ELink"+ value);
							return "ELink" + value; // URL
						} else {
							return NextToken(); // "/A" à ignorer
						}

					default:
						return NextToken();
					}
				} catch (HtmlException e) {
				}
				break;

			case HtmlStreamTokenizer.TT_TEXT:
				String res = tok.GetStringValue().toString();

				if (res.startsWith("[")) {
					if (res.endsWith("]")) { // Token du type [toototot]
						return res;
					} else { // Token de type [toto titi tutu] ; doir etre
								// parsé en plusieurs fois
						return nextNameToken(res);
					}
				}
				{
					return res;
				}

			case HtmlStreamTokenizer.TT_COMMENT:
				return NextToken();
			}

		} catch (IOException e) {
		}

		return null;
	}

	String nextNameToken(String cur) {
		int ttype;
		try {
			tok.NextToken();
			ttype = tok.GetTokenType();
			if (ttype == HtmlStreamTokenizer.TT_TEXT) {
				String res = tok.GetStringValue().toString();
				if (res.endsWith("]")) {
					return cur + " " + res;
				} else {
					return nextNameToken(cur + " " + res);
				}
			} else {
				// System.out.println("Error : nextNameToken");
			}
		} catch (IOException e) {
		}
		return null;
	}
}
