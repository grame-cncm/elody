package grame.elody.file.html.parser;

import java.io.IOException;
import java.io.InputStream;

/*******************************************************************************************
*
*	 HtmlStreamTokenizer (classe) : HTML tokeniser
* 
*******************************************************************************************/

public final class HtmlStreamTokenizer {
    public static final int TT_EOF = -1;
    public static final int TT_TEXT = -2;
    public static final int TT_TAG = -3;
	public static final int TT_COMMENT = -4;

	public HtmlStreamTokenizer(InputStream in)
	{
		m_in = in;
		m_state = STATE_TEXT;
	}

	public final int GetTokenType()
	{
		return m_ttype;
	}

	public final StringBuffer GetStringValue()
	{
		return m_buf;
	}

	public final StringBuffer GetWhiteSpace()
	{
		return m_whitespace;
	}

	public int NextToken()
		throws IOException
	{
		m_escbuf.setLength(0);
		m_buf.setLength(0);
		m_whitespace.setLength(0);
		InputStream is = m_in;

		while (true)
		{
			int c;
			
			if (m_pushback != 0)
			{
				c = m_pushback;
				m_pushback = 0;
			}
			else
			{
				c = is.read();
			}

			if (c < 0)
			{
				int state = m_state;
				m_state = STATE_EOF;

				if (m_buf.length() > 0 && state == STATE_TEXT)
					return m_ttype = TT_TEXT;
				else
					return m_ttype = TT_EOF;
			}

			if (c == '\n')
				m_lineno++;

			if (c == '&' && m_state != STATE_ESCAPE && m_state != STATE_COMMENT)
			{
				m_escSaveState = m_state;
				m_state = STATE_ESCAPE;
				m_escbuf.append((char)c);
				continue;
			}

			switch (m_state)
			{
			case STATE_TEXT:
				{
					if (c == '<')
					{
						m_state = STATE_TAG;
						if (m_buf.length() > 0)
							return m_ttype = TT_TEXT;
					}
					else if (IsSpace(c))
					{
						m_pushback = c;
						m_state = STATE_WS;
						if (m_buf.length() > 0)
							return m_ttype = TT_TEXT;
					}
					else
					{
						m_buf.append((char)c);
					}
				}
				break;

			case STATE_WS:
				{
					if (!IsSpace(c))
					{
						m_pushback = c;
						m_state = STATE_TEXT;
					}
					else
					{
						m_whitespace.append((char)c);
					}
				}
				break;

			case STATE_TAG:
				{
					int buflen = m_buf.length();

					if (c == '>')
					{
						m_state = STATE_TEXT;
						return m_ttype = TT_TAG;
					}
					else if (c == '<' && buflen == 0)
					{
						// handle <<, some people use it in <pre>
						m_buf.append("<<");
						m_state = STATE_TEXT;
					}
					else if (c == '-' && buflen == 2 && m_buf.charAt(1) == '-' && m_buf.charAt(0) == '!')
					{
						// handle <!--
						m_buf.setLength(0);
						m_state = STATE_COMMENT;
					}
					else
					{
						m_buf.append((char)c);
					}
				}
				break;

			case STATE_COMMENT:
				{
					if (c == '>' && m_comment >= 2)
					{
						m_buf.setLength(m_buf.length() - 2);
						m_comment = 0;
						m_state = STATE_TEXT;
						return m_ttype = TT_COMMENT;
					}
					else if (c == '-')
					{
						m_comment++;
					}
					else
					{
						m_comment = 0;
					}

					m_buf.append((char)c);
				}
				break;

			case STATE_ESCAPE:
				{
					// TODO
					m_buf.append(m_escbuf);
					m_escbuf.setLength(0);
					m_buf.append((char)c);
					if (m_escSaveState == STATE_WS)
						m_state = STATE_TEXT;
					else
						m_state = m_escSaveState;
				}
				break;
			}
		}
	}
	
	
	public static boolean IsSpace(int c)
	{
		 return c >=0 && c < CTYPE_LEN ? (m_ctype[c] & CT_WHITESPACE) != 0: false;
	}

    public int m_ttype;
	public StringBuffer m_escbuf = new StringBuffer();
	public StringBuffer m_buf = new StringBuffer();
	public StringBuffer m_whitespace = new StringBuffer();
	public int m_pushback = 0;
	public int m_escSaveState = 0;
	public int m_lineno = 1;
	public int m_comment = 0;

    public static final int STATE_EOF = -1;
    public static final int STATE_COMMENT = -2;
    public static final int STATE_TEXT = -3;
    public static final int STATE_TAG = -4;
    public static final int STATE_WS = -5;
    public static final int STATE_ESCAPE = -6;		// TODO

	public int m_state = STATE_TEXT;

	public InputStream m_in;

	public static final int CTYPE_LEN = 256;
    public static byte m_ctype[] = new byte[CTYPE_LEN];
    public static final byte CT_WHITESPACE = 1;
    public static final byte CT_DIGIT = 2;
    public static final byte CT_ALPHA = 4;
    public static final byte CT_QUOTE = 8;
    public static final byte CT_COMMENT = 16;

	static
	{
		int len = m_ctype.length;
		for (int i = 0; i < len; i++)
			m_ctype[i] = 0;

		m_ctype[' '] = CT_WHITESPACE;
		m_ctype['\r'] = CT_WHITESPACE;
		m_ctype['\n'] = CT_WHITESPACE;
		m_ctype['\t'] = CT_WHITESPACE;
		for (int i = 0x0E; i <= 0x1F; i++)
			m_ctype[i] = CT_WHITESPACE;
	}
}
