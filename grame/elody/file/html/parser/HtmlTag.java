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

import java.util.Enumeration;
import java.util.Hashtable;

public final class HtmlTag {
	public HtmlTag(String tag) throws HtmlException {
		try {
			if (tag.charAt(0) == '/') {
				m_endtag = true;
				tag = tag.substring(1);
			}

			m_tag = tag.toUpperCase();
			Object value = m_tags.get(m_tag);
			if (value != null)
				m_ttype = ((Integer) value).intValue();
			else {
				// System.out.println("unknown tag: " + m_tag);
			}
		} catch (StringIndexOutOfBoundsException e) {
			throw new HtmlException("invalid tag");
		}
	}

	public final int GetTagType() {
		return m_ttype;
	}

	public String GetTagString() {
		return m_tag;
	}

	// param name must be lowercase, try to use the predefined P_ constants
	// e.g.
	// <img src="image.gif">
	// String value = tag.GetParam(HtmlTag.P_SRC);
	// if (value != null)
	// process value == image.gif
	public String GetParam(String name) {
		return m_params.get(name);
	}

	public static HtmlTag ParseTag(StringBuffer sbuf) throws HtmlException {
		String buf = sbuf.toString();
		int len = buf.length();
		int idx = 0;
		int begin = 0;

		// parse tag
		while (idx < len && HtmlStreamTokenizer.IsSpace(buf.charAt(idx)))
			idx++;

		if (idx == len)
			throw new HtmlException("parse empty tag");

		begin = idx;
		while (idx < len && !HtmlStreamTokenizer.IsSpace(buf.charAt(idx)))
			idx++;
		String token = buf.substring(begin, idx);

		HtmlTag tag = new HtmlTag(token);

		while (idx < len && HtmlStreamTokenizer.IsSpace(buf.charAt(idx)))
			idx++;

		if (idx == len)
			return tag;

		tag.ParseParams(buf, idx);

		return tag;
	}

	public String toString() {
		StringBuffer tag = new StringBuffer();

		tag.append('<');
		if (m_endtag)
			tag.append('/');
		tag.append(m_tag);

		Enumeration<String> keys = m_params.keys();
		Enumeration<String> elements = m_params.elements();
		while (keys.hasMoreElements()) {
			String name = keys.nextElement();
			String value = elements.nextElement();
			tag.append(" " + name + "=\"" + value + "\"");
		}
		tag.append('>');

		return tag.toString();
	}

	public static final int T_UNKNOWN = 0;
	
	public static final int T_A = 1;
	
	public static final int T_ABBREV = 2;
	
	public static final int T_ACRONYM = 3;
	
	public static final int T_ADDRESS = 4;
	
	public static final int T_APPLET = 5;
	
	public static final int T_AREA = 6;
	
	public static final int T_AU = 7;
	
	public static final int T_B = 8;
	
	public static final int T_BANNER = 9;

	public static final int T_BASE = 10;

	public static final int T_BASEFONT = 11;

	public static final int T_BGSOUND = 12;

	public static final int T_BIG = 13;

	public static final int T_BLINK = 14;

	public static final int T_BLOCKQUOTE = 15;

	public static final int T_BODY = 16;

	public static final int T_BR = 17;

	public static final int T_CAPTION = 18;

	public static final int T_CENTER = 19;

	public static final int T_CITE = 20;

	public static final int T_CODE = 21;

	public static final int T_COL = 22;

	public static final int T_COLGROUP = 23;

	public static final int T_CREDIT = 24;

	public static final int T_DD = 25;

	public static final int T_DEL = 26;

	public static final int T_DFN = 27;

	public static final int T_DIR = 28;

	public static final int T_DIV = 29;

	public static final int T_DL = 30;

	public static final int T_DT = 31;

	public static final int T_EM = 32;

	public static final int T_EMBED = 33;

	public static final int T_FIG = 34;

	public static final int T_FN = 35;

	public static final int T_FONT = 36;

	public static final int T_FORM = 37;

	public static final int T_FRAME = 38;

	public static final int T_FRAMESET = 39;

	public static final int T_H1 = 40;

	public static final int T_H2 = 41;

	public static final int T_H3 = 42;

	public static final int T_H4 = 43;

	public static final int T_H5 = 44;

	public static final int T_H6 = 45;

	public static final int T_HEAD = 46;

	public static final int T_HTML = 47;

	public static final int T_HR = 48;

	public static final int T_I = 49;

	public static final int T_IMG = 50;

	public static final int T_INPUT = 51;

	public static final int T_INS = 52;

	public static final int T_ISINDEX = 53;

	public static final int T_KBD = 54;

	public static final int T_LANG = 55;

	public static final int T_LH = 56;

	public static final int T_LI = 57;

	public static final int T_LINK = 58;

	public static final int T_MAP = 59;

	public static final int T_MARQUEE = 60;

	public static final int T_MENU = 61;

	public static final int T_META = 62;

	public static final int T_NEXTID = 63;

	public static final int T_NOBR = 64;

	public static final int T_NOEMBED = 65;

	public static final int T_NOFRAME = 66;

	public static final int T_NOFRAMES = 67;

	public static final int T_NOTE = 68;

	public static final int T_OBJECT = 69;

	public static final int T_OL = 70;

	public static final int T_OPTION = 71;

	public static final int T_OVERLAY = 72;

	public static final int T_P = 73;

	public static final int T_PARAM = 74;

	public static final int T_PERSON = 75;

	public static final int T_PRE = 76;

	public static final int T_Q = 77;

	public static final int T_RANGE = 78;

	public static final int T_S = 79;

	public static final int T_SAMP = 80;

	public static final int T_SCRIPT = 81;

	public static final int T_SELECT = 82;

	public static final int T_SMALL = 83;

	public static final int T_SPOT = 84;

	public static final int T_STRONG = 85;

	public static final int T_STYLE = 86;

	public static final int T_SUB = 87;

	public static final int T_SUP = 88;

	public static final int T_TAB = 89;

	public static final int T_TABLE = 90;

	public static final int T_TBODY = 91;

	public static final int T_TD = 92;

	public static final int T_TEXTAREA = 93;

	public static final int T_TFOOT = 94;

	public static final int T_TH = 95;

	public static final int T_THEAD = 96;

	public static final int T_TITLE = 97;

	public static final int T_TR = 98;

	public static final int T_TT = 99;

	public static final int T_U = 100;

	public static final int T_UL = 101;

	public static final int T_VAR = 102;

	public static final int T_WBR = 103;

	public static final String P_ALIGN = "align";

	public static final String P_BORDER = "border";

	public static final String P_CHECKED = "checked";

	public static final String P_CLEAR = "clear";

	public static final String P_COLS = "cols";

	public static final String P_COLSPAN = "colspan";

	public static final String P_FACE = "face";

	public static final String P_HEIGHT = "height";

	public static final String P_HREF = "href";

	public static final String P_LANGUAGE = "language";

	public static final String P_MAXLENGTH = "maxlength";

	public static final String P_MULTIPLE = "multiple";

	public static final String P_NAME = "name";

	public static final String P_ROWS = "rows";

	public static final String P_ROWSPAN = "rowspan";

	public static final String P_SIZE = "size";

	public static final String P_SRC = "src";

	public static final String P_TYPE = "type";

	public static final String P_VALUE = "value";

	public static final String P_WIDTH = "width";

	public static final char C_SINGLEQUOTE = '\'';

	public static final char C_DOUBLEQUOTE = '"';

	public String m_tag = null;

	public int m_ttype = T_UNKNOWN;

	public boolean m_endtag = false;

	public Hashtable<String, String> m_params = new Hashtable<String, String>();

	public static Hashtable<String, Integer> m_tags = new Hashtable<String, Integer>();

	static {
		m_tags.put(new String("A"), new Integer(T_A));
		m_tags.put(new String("ABBREV"), new Integer(T_ABBREV));
		m_tags.put(new String("ACRONYM"), new Integer(T_ACRONYM));
		m_tags.put(new String("ADDRESS"), new Integer(T_ADDRESS));
		m_tags.put(new String("APPLET"), new Integer(T_APPLET));
		m_tags.put(new String("AREA"), new Integer(T_AREA));
		m_tags.put(new String("AU"), new Integer(T_AU));
		m_tags.put(new String("B"), new Integer(T_B));
		m_tags.put(new String("BANNER"), new Integer(T_BANNER));
		m_tags.put(new String("BASE"), new Integer(T_BASE));
		m_tags.put(new String("BASEFONT"), new Integer(T_BASEFONT));
		m_tags.put(new String("BGSOUND"), new Integer(T_BGSOUND));
		m_tags.put(new String("BIG"), new Integer(T_BIG));
		m_tags.put(new String("BLINK"), new Integer(T_BLINK));
		m_tags.put(new String("BLOCKQUOTE"), new Integer(T_BLOCKQUOTE));
		m_tags.put(new String("BODY"), new Integer(T_BODY));
		m_tags.put(new String("BR"), new Integer(T_BR));
		m_tags.put(new String("CAPTION"), new Integer(T_CAPTION));
		m_tags.put(new String("CENTER"), new Integer(T_CENTER));
		m_tags.put(new String("CITE"), new Integer(T_CITE));
		m_tags.put(new String("CODE"), new Integer(T_CODE));
		m_tags.put(new String("COL"), new Integer(T_COL));
		m_tags.put(new String("COLGROUP"), new Integer(T_COLGROUP));
		m_tags.put(new String("CREDIT"), new Integer(T_CREDIT));
		m_tags.put(new String("DD"), new Integer(T_DD));
		m_tags.put(new String("DEL"), new Integer(T_DEL));
		m_tags.put(new String("DFN"), new Integer(T_DFN));
		m_tags.put(new String("DIR"), new Integer(T_DIR));
		m_tags.put(new String("DIV"), new Integer(T_DIV));
		m_tags.put(new String("DL"), new Integer(T_DL));
		m_tags.put(new String("DT"), new Integer(T_DT));
		m_tags.put(new String("EM"), new Integer(T_EM));
		m_tags.put(new String("EMBED"), new Integer(T_EMBED));
		m_tags.put(new String("FIG"), new Integer(T_FIG));
		m_tags.put(new String("FN"), new Integer(T_FN));
		m_tags.put(new String("FONT"), new Integer(T_FONT));
		m_tags.put(new String("FORM"), new Integer(T_FORM));
		m_tags.put(new String("FRAME"), new Integer(T_FRAME));
		m_tags.put(new String("FRAMESET"), new Integer(T_FRAMESET));
		m_tags.put(new String("H1"), new Integer(T_H1));
		m_tags.put(new String("H2"), new Integer(T_H2));
		m_tags.put(new String("H3"), new Integer(T_H3));
		m_tags.put(new String("H4"), new Integer(T_H4));
		m_tags.put(new String("H5"), new Integer(T_H5));
		m_tags.put(new String("H6"), new Integer(T_H6));
		m_tags.put(new String("HEAD"), new Integer(T_HEAD));
		m_tags.put(new String("HTML"), new Integer(T_HTML));
		m_tags.put(new String("HR"), new Integer(T_HR));
		m_tags.put(new String("I"), new Integer(T_I));
		m_tags.put(new String("IMG"), new Integer(T_IMG));
		m_tags.put(new String("INPUT"), new Integer(T_INPUT));
		m_tags.put(new String("INS"), new Integer(T_INS));
		m_tags.put(new String("ISINDEX"), new Integer(T_ISINDEX));
		m_tags.put(new String("KBD"), new Integer(T_KBD));
		m_tags.put(new String("LANG"), new Integer(T_LANG));
		m_tags.put(new String("LH"), new Integer(T_LH));
		m_tags.put(new String("LI"), new Integer(T_LI));
		m_tags.put(new String("LINK"), new Integer(T_LINK));
		m_tags.put(new String("MAP"), new Integer(T_MAP));
		m_tags.put(new String("MARQUEE"), new Integer(T_MARQUEE));
		m_tags.put(new String("MENU"), new Integer(T_MENU));
		m_tags.put(new String("META"), new Integer(T_META));
		m_tags.put(new String("NEXTID"), new Integer(T_NEXTID));
		m_tags.put(new String("NOBR"), new Integer(T_NOBR));
		m_tags.put(new String("NOEMBED"), new Integer(T_NOEMBED));
		m_tags.put(new String("NOFRAME"), new Integer(T_NOFRAME));
		m_tags.put(new String("NOFRAMES"), new Integer(T_NOFRAMES));
		m_tags.put(new String("NOTE"), new Integer(T_NOTE));
		m_tags.put(new String("OBJECT"), new Integer(T_OBJECT));
		m_tags.put(new String("OL"), new Integer(T_OL));
		m_tags.put(new String("OPTION"), new Integer(T_OPTION));
		m_tags.put(new String("OVERLAY"), new Integer(T_OVERLAY));
		m_tags.put(new String("P"), new Integer(T_P));
		m_tags.put(new String("PARAM"), new Integer(T_PARAM));
		m_tags.put(new String("PERSON"), new Integer(T_PERSON));
		m_tags.put(new String("PRE"), new Integer(T_PRE));
		m_tags.put(new String("Q"), new Integer(T_Q));
		m_tags.put(new String("RANGE"), new Integer(T_RANGE));
		m_tags.put(new String("S"), new Integer(T_S));
		m_tags.put(new String("SAMP"), new Integer(T_SAMP));
		m_tags.put(new String("SCRIPT"), new Integer(T_SCRIPT));
		m_tags.put(new String("SELECT"), new Integer(T_SELECT));
		m_tags.put(new String("SMALL"), new Integer(T_SMALL));
		m_tags.put(new String("SPOT"), new Integer(T_SPOT));
		m_tags.put(new String("STRONG"), new Integer(T_STRONG));
		m_tags.put(new String("STYLE"), new Integer(T_STYLE));
		m_tags.put(new String("SUB"), new Integer(T_SUB));
		m_tags.put(new String("SUP"), new Integer(T_SUP));
		m_tags.put(new String("TAB"), new Integer(T_TAB));
		m_tags.put(new String("TABLE"), new Integer(T_TABLE));
		m_tags.put(new String("TBODY"), new Integer(T_TBODY));
		m_tags.put(new String("TD"), new Integer(T_TD));
		m_tags.put(new String("TEXTAREA"), new Integer(T_TEXTAREA));
		m_tags.put(new String("TFOOT"), new Integer(T_TFOOT));
		m_tags.put(new String("TH"), new Integer(T_TH));
		m_tags.put(new String("THEAD"), new Integer(T_THEAD));
		m_tags.put(new String("TITLE"), new Integer(T_TITLE));
		m_tags.put(new String("TR"), new Integer(T_TR));
		m_tags.put(new String("TT"), new Integer(T_TT));
		m_tags.put(new String("U"), new Integer(T_U));
		m_tags.put(new String("UL"), new Integer(T_UL));
		m_tags.put(new String("VAR"), new Integer(T_VAR));
		m_tags.put(new String("WBR"), new Integer(T_WBR));
	}

	public void ParseParams(String buf, int idx) throws HtmlException {
		int len = buf.length();
		int begin = 0;

		while (idx < len) {
			while (idx < len && HtmlStreamTokenizer.IsSpace(buf.charAt(idx)))
				idx++;

			if (idx == len)
				continue;

			begin = idx;
			while (idx < len && !HtmlStreamTokenizer.IsSpace(buf.charAt(idx))
					&& buf.charAt(idx) != '=')
				idx++;

			if (idx == len)
				continue;

			// param names are stored in lower case for fast lookup
			String name = buf.substring(begin, idx).toLowerCase();

			if (HtmlStreamTokenizer.IsSpace(buf.charAt(idx))) {
				while (idx < len
						&& HtmlStreamTokenizer.IsSpace(buf.charAt(idx)))
					idx++;
			}

			if (idx == len || buf.charAt(idx) != '=')
				continue;
			idx++;

			if (idx == len)
				continue;

			if (buf.charAt(idx) == ' ') {
				// special case: if value is surrounded by quotes
				// then it can have a space after the '='
				while (idx < len
						&& HtmlStreamTokenizer.IsSpace(buf.charAt(idx)))
					idx++;

				if (idx == len || buf.charAt(idx) != C_DOUBLEQUOTE
						|| buf.charAt(idx) != C_SINGLEQUOTE)
					continue;
			}

			begin = idx;
			if (buf.charAt(idx) == C_DOUBLEQUOTE) {
				idx++;
				begin = idx;
				while (idx < len && buf.charAt(idx) != C_DOUBLEQUOTE)
					idx++;
			} else if (buf.charAt(idx) == C_SINGLEQUOTE) {
				idx++;
				begin = idx;
				while (idx < len && buf.charAt(idx) != C_SINGLEQUOTE)
					idx++;
			} else {
				while (idx < len
						&& !HtmlStreamTokenizer.IsSpace(buf.charAt(idx)))
					idx++;
			}

			String value = buf.substring(begin, idx);

			m_params.put(name, value);
		}
	}
}
