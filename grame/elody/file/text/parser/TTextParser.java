package grame.elody.file.text.parser;

import grame.elody.file.guido.parser.ASCII_CharStream;
import grame.elody.file.guido.parser.ParseException;
import grame.elody.file.guido.parser.Token;
import grame.elody.lang.texpression.expressions.TExp;

import java.util.Vector;

public class TTextParser /* @bgen(jjtree) */implements TTextParserTreeConstants,
		TTextParserConstants {/* @bgen(jjtree) */
	protected JJTTTextParserState jjtree = new JJTTTextParserState();

	public TExp readTextFile() throws ParseException {
		SimpleNode node = expression();
		TTextExpBuilder builder = new TTextExpBuilder();
		TExp res = (TExp) node.jjtAccept(builder, null);
		return res;
	}

	/*
	 * TEXT grammar definition
	 */
	final public SimpleNode expression() throws ParseException {
		/* @bgen(jjtree) expression */
		ASTexpression jjtn000 = new ASTexpression(JJTEXPRESSION);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			applications();
			jjtree.closeNodeScope(jjtn000, true);
			jjtc000 = false;
			{
				if (true)
					return jjtn000;
			}
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public void applications() throws ParseException {
		/* @bgen(jjtree) applications */
		ASTapplications jjtn000 = new ASTapplications(JJTAPPLICATIONS);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			lambda();
			label_1: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case LACO:
				case LPAR:
				case LBRA:
				case ABSTR:
				case BMUTE:
				case NOTE_ID:
				case VAR_ID:
					;
					break;
				default:
					jj_la1[0] = jj_gen;
					break label_1;
				}
				lambda();
			}
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void lambda() throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case ABSTR:
			abstraction();
			break;
		case LACO:
		case LPAR:
		case LBRA:
		case BMUTE:
		case NOTE_ID:
		case VAR_ID:
			transformed();
			break;
		default:
			jj_la1[1] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final public void transformed() throws ParseException {
		/* @bgen(jjtree) transformed */
		ASTtransformed jjtn000 = new ASTtransformed(JJTTRANSFORMED);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			modified();
			label_2: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case BEGIN:
				case REST:
				case EXPAND:
					;
					break;
				default:
					jj_la1[2] = jj_gen;
					break label_2;
				}
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case BEGIN:
					begin();
					break;
				case REST:
					rest();
					break;
				case EXPAND:
					expand();
					break;
				default:
					jj_la1[3] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void begin() throws ParseException {
		/* @bgen(jjtree) begin */
		ASTbegin jjtn000 = new ASTbegin(JJTBEGIN);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			jj_consume_token(BEGIN);
			modified();
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void rest() throws ParseException {
		/* @bgen(jjtree) rest */
		ASTrest jjtn000 = new ASTrest(JJTREST);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			jj_consume_token(REST);
			modified();
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void expand() throws ParseException {
		/* @bgen(jjtree) expand */
		ASTexpand jjtn000 = new ASTexpand(JJTEXPAND);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			jj_consume_token(EXPAND);
			modified();
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void modified() throws ParseException {
		/* @bgen(jjtree) modified */
		ASTmodified jjtn000 = new ASTmodified(JJTMODIFIED);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			simple();
			modifiers();
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void simple() throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case LBRA:
			sequence();
			break;
		case LACO:
			mix();
			break;
		case NOTE_ID:
			note();
			break;
		case VAR_ID:
			var();
			break;
		case BMUTE:
			mute();
			break;
		case LPAR:
			jj_consume_token(LPAR);
			expression();
			jj_consume_token(RPAR);
			break;
		default:
			jj_la1[4] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final public void mute() throws ParseException {
		/* @bgen(jjtree) mute */
		ASTmute jjtn000 = new ASTmute(JJTMUTE);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			jj_consume_token(BMUTE);
			expression();
			jj_consume_token(EMUTE);
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void abstraction() throws ParseException {
		/* @bgen(jjtree) abstraction */
		ASTabstraction jjtn000 = new ASTabstraction(JJTABSTRACTION);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			jj_consume_token(ABSTR);
			identifier();
			jj_consume_token(41);
			lambda();
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	//
	final public void var() throws ParseException {
		/* @bgen(jjtree) var */
		ASTvar jjtn000 = new ASTvar(JJTVAR);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		Token t;
		try {
			t = jj_consume_token(VAR_ID);
			jjtree.closeNodeScope(jjtn000, true);
			jjtc000 = false;
			jjtn000.setName(t.image);
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	/*
	 * void identifier () #void : {} { var () ":" simple() }
	 */
	final public void identifier() throws ParseException {
		var();
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case 42:
			jj_consume_token(42);
			expression();
			break;
		default:
			jj_la1[5] = jj_gen;
			;
		}
	}

	final public void sequence() throws ParseException {
		/* @bgen(jjtree) sequence */
		ASTsequence jjtn000 = new ASTsequence(JJTSEQUENCE);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			jj_consume_token(LBRA);
			expression();
			label_3: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case VIR:
					;
					break;
				default:
					jj_la1[6] = jj_gen;
					break label_3;
				}
				jj_consume_token(VIR);
				expression();
			}
			jj_consume_token(RBRA);
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void mix() throws ParseException {
		/* @bgen(jjtree) mix */
		ASTmix jjtn000 = new ASTmix(JJTMIX);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		try {
			jj_consume_token(LACO);
			expression();
			label_4: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case VIR:
					;
					break;
				default:
					jj_la1[7] = jj_gen;
					break label_4;
				}
				jj_consume_token(VIR);
				expression();
			}
			jj_consume_token(RACO);
		} catch (Throwable jjte000) {
			if (jjtc000) {
				jjtree.clearNodeScope(jjtn000);
				jjtc000 = false;
			} else {
				jjtree.popNode();
			}
			if (jjte000 instanceof ParseException) {
				{
					if (true)
						throw (ParseException) jjte000;
				}
			}
			if (jjte000 instanceof RuntimeException) {
				{
					if (true)
						throw (RuntimeException) jjte000;
				}
			}
			{
				if (true)
					throw (Error) jjte000;
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void modifiers() throws ParseException {
		label_5: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case SHARP:
			case FLAT:
			case STAR:
			case FRAC:
			case PLUS:
			case MINUS:
			case DOT:
			case LTRI:
			case RTRI:
			case NUM:
			case 43:
				;
				break;
			default:
				jj_la1[8] = jj_gen;
				break label_5;
			}
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case SHARP:
			case FLAT:
			case PLUS:
			case MINUS:
			case NUM:
				transposition();
				break;
			case LTRI:
			case RTRI:
				attenuation();
				break;
			case STAR:
			case FRAC:
			case DOT:
				duration();
				break;
			case 43:
				channel();
				break;
			default:
				jj_la1[9] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
		}
	}

	final public void transposition() throws ParseException {
		/* @bgen(jjtree) transposition */
		ASTtransposition jjtn000 = new ASTtransposition(JJTTRANSPOSITION);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		Token t;
		try {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case SHARP:
				jj_consume_token(SHARP);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal((float) 1.0);
				break;
			case FLAT:
				jj_consume_token(FLAT);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal((float) -1.0);
				break;
			case NUM:
				t = jj_consume_token(NUM);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal(Float.valueOf(t.image).floatValue() * 12);
				break;
			case PLUS:
				jj_consume_token(PLUS);
				t = jj_consume_token(NUM);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal(Float.valueOf(t.image).floatValue());
				break;
			case MINUS:
				jj_consume_token(MINUS);
				t = jj_consume_token(NUM);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal(-Float.valueOf(t.image).floatValue());
				break;
			default:
				jj_la1[10] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void attenuation() throws ParseException {
		/* @bgen(jjtree) attenuation */
		ASTattenuation jjtn000 = new ASTattenuation(JJTATTENUATION);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		Token t;
		try {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case RTRI:
				jj_consume_token(RTRI);
				t = jj_consume_token(NUM);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal(Float.valueOf(t.image).floatValue());
				break;
			case LTRI:
				jj_consume_token(LTRI);
				t = jj_consume_token(NUM);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal(-Float.valueOf(t.image).floatValue());
				break;
			default:
				jj_la1[11] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void channel() throws ParseException {
		/* @bgen(jjtree) channel */
		ASTchannel jjtn000 = new ASTchannel(JJTCHANNEL);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		Token t;
		try {
			jj_consume_token(43);
			t = jj_consume_token(NUM);
			jjtree.closeNodeScope(jjtn000, true);
			jjtc000 = false;
			jjtn000.setVal(Float.valueOf(t.image).floatValue());
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void duration() throws ParseException {
		/* @bgen(jjtree) duration */
		ASTduration jjtn000 = new ASTduration(JJTDURATION);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		Token t;
		try {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case FRAC:
				jj_consume_token(FRAC);
				t = jj_consume_token(NUM);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal((float) 1.0
						/ Float.valueOf(t.image).floatValue());
				break;
			case STAR:
				jj_consume_token(STAR);
				t = jj_consume_token(NUM);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal(Float.valueOf(t.image).floatValue());
				break;
			case DOT:
				jj_consume_token(DOT);
				jjtree.closeNodeScope(jjtn000, true);
				jjtc000 = false;
				jjtn000.setVal((float) 1.5);
				break;
			default:
				jj_la1[12] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	final public void note() throws ParseException {
		/* @bgen(jjtree) note */
		ASTnote jjtn000 = new ASTnote(JJTNOTE);
		boolean jjtc000 = true;
		jjtree.openNodeScope(jjtn000);
		Token t;
		try {
			t = jj_consume_token(NOTE_ID);
			jjtree.closeNodeScope(jjtn000, true);
			jjtc000 = false;
			jjtn000.setName(t.image);
		} finally {
			if (jjtc000) {
				jjtree.closeNodeScope(jjtn000, true);
			}
		}
	}

	public TTextParserTokenManager token_source;

	ASCII_CharStream jj_input_stream;

	public Token token, jj_nt;

	private int jj_ntk;

	private int jj_gen;

	final private int[] jj_la1 = new int[13];

	final private int[] jj_la1_0 = { 0x54100000, 0x54100000, 0x80000000,
			0x80000000, 0x14100000, 0x0, 0x400000, 0x400000, 0x38fc000,
			0x38fc000, 0xcc000, 0x3000000, 0x830000, };

	final private int[] jj_la1_1 = { 0x184, 0x184, 0x3, 0x3, 0x184, 0x400, 0x0,
			0x0, 0x810, 0x810, 0x10, 0x0, 0x0, };

	public TTextParser(java.io.InputStream stream) {
		jj_input_stream = new ASCII_CharStream(stream, 1, 1);
		token_source = new TTextParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 13; i++)
			jj_la1[i] = -1;
	}

	public void ReInit(java.io.InputStream stream) {
		jj_input_stream.ReInit(stream, 1, 1);
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jjtree.reset();
		jj_gen = 0;
		for (int i = 0; i < 13; i++)
			jj_la1[i] = -1;
	}

	public TTextParser(java.io.Reader stream) {
		jj_input_stream = new ASCII_CharStream(stream, 1, 1);
		token_source = new TTextParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 13; i++)
			jj_la1[i] = -1;
	}

	public void ReInit(java.io.Reader stream) {
		jj_input_stream.ReInit(stream, 1, 1);
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jjtree.reset();
		jj_gen = 0;
		for (int i = 0; i < 13; i++)
			jj_la1[i] = -1;
	}

	public TTextParser(TTextParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 13; i++)
			jj_la1[i] = -1;
	}

	public void ReInit(TTextParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jjtree.reset();
		jj_gen = 0;
		for (int i = 0; i < 13; i++)
			jj_la1[i] = -1;
	}

	final private Token jj_consume_token(int kind) throws ParseException {
		Token oldToken;
		if ((oldToken = token).next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		if (token.kind == kind) {
			jj_gen++;
			return token;
		}
		token = oldToken;
		jj_kind = kind;
		throw generateParseException();
	}

	final public Token getNextToken() {
		if (token.next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		jj_gen++;
		return token;
	}

	final public Token getToken(int index) {
		Token t = token;
		for (int i = 0; i < index; i++) {
			if (t.next != null)
				t = t.next;
			else
				t = t.next = token_source.getNextToken();
		}
		return t;
	}

	final private int jj_ntk() {
		if ((jj_nt = token.next) == null)
			return (jj_ntk = (token.next = token_source.getNextToken()).kind);
		else
			return (jj_ntk = jj_nt.kind);
	}

	private Vector<int[]> jj_expentries = new Vector<int[]>();

	private int[] jj_expentry;

	private int jj_kind = -1;

	final public ParseException generateParseException() {
		jj_expentries.removeAllElements();
		boolean[] la1tokens = new boolean[44];
		for (int i = 0; i < 44; i++) {
			la1tokens[i] = false;
		}
		if (jj_kind >= 0) {
			la1tokens[jj_kind] = true;
			jj_kind = -1;
		}
		for (int i = 0; i < 13; i++) {
			if (jj_la1[i] == jj_gen) {
				for (int j = 0; j < 32; j++) {
					if ((jj_la1_0[i] & (1 << j)) != 0) {
						la1tokens[j] = true;
					}
					if ((jj_la1_1[i] & (1 << j)) != 0) {
						la1tokens[32 + j] = true;
					}
				}
			}
		}
		for (int i = 0; i < 44; i++) {
			if (la1tokens[i]) {
				jj_expentry = new int[1];
				jj_expentry[0] = i;
				jj_expentries.addElement(jj_expentry);
			}
		}
		int[][] exptokseq = new int[jj_expentries.size()][];
		for (int i = 0; i < jj_expentries.size(); i++) {
			exptokseq[i] = jj_expentries.elementAt(i);
		}
		return new ParseException(token, exptokseq, tokenImage);
	}

	final public void enable_tracing() {
	}

	final public void disable_tracing() {
	}
}
