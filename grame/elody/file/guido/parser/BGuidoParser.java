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

import java.util.Vector;

public class BGuidoParser implements BGuidoParserConstants {

        public  BasicGuidoReader reader;

        public void readGuidoFile(BasicGuidoReader reader) throws ParseException {
                this.reader = reader;
            notesegm();
        }

/*
GUIDO grammar definition
*/
  final public void notesegm() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LACO:
      jj_consume_token(LACO);
                reader.GD_INIT_SEGM();
      seqlist();
      jj_consume_token(RACO);
                                                            reader.GD_EXIT_SEGM();
      break;
    case LBRA:
                    reader.GD_INIT_SEGM();
      noteseq();
                                                         reader.GD_EXIT_SEGM();
      jj_consume_token(0);
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void seqlist() throws ParseException {
    noteseq();
                    reader.GD_APP_SEQ();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VIR:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
      jj_consume_token(VIR);
      noteseq();
                                                              reader.GD_APP_SEQ();
    }
  }

  final public void noteseq() throws ParseException {
    jj_consume_token(LBRA);
                 reader.GD_INIT_SEQ();
    cnotelist();
    jj_consume_token(RBRA);
                                                              reader.GD_EXIT_SEQ();
  }

  final public void cnotelist() throws ParseException {
    label_2:
    while (true) {
      event();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LACO:
      case NOTE_ID:
      case TAG_ID:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
    }
  }

  final public void event() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOTE_ID:
      note();
                    reader.GD_APP_NT();
      break;
    case TAG_ID:
      tag();
      break;
    case LACO:
      chord();
                reader.GD_APP_CH();
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void note() throws ParseException {
 Token n;
    n = jj_consume_token(NOTE_ID);
                         reader.GD_NT(n.image);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SHARP:
    case FLAT:
      accidental();
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PLUS:
    case MINUS:
    case NUM:
      octave();
      break;
    default:
      jj_la1[5] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STAR:
    case FRAC:
      duration();
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
  }

  final public void accidental() throws ParseException {
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SHARP:
        jj_consume_token(SHARP);
                  reader.GD_SH_NT();
        break;
      case FLAT:
        jj_consume_token(FLAT);
                                                reader.GD_FL_NT();
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SHARP:
      case FLAT:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_3;
      }
    }
  }

  final public void octave() throws ParseException {
 Token n;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUM:
      n = jj_consume_token(NUM);
                     reader.GD_OCT_NT(n.image);
      break;
    case PLUS:
      jj_consume_token(PLUS);
      n = jj_consume_token(NUM);
                                reader.GD_OCT_NT(n.image);
      break;
    case MINUS:
      jj_consume_token(MINUS);
      n = jj_consume_token(NUM);
                                reader.GD_OCT_NT("-"+ n.image);
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void duration() throws ParseException {
 Token n;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STAR:
      jj_consume_token(STAR);
      n = jj_consume_token(NUM);
                            reader.GD_ENUM_NT(n.image);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FRAC:
        jj_consume_token(FRAC);
        n = jj_consume_token(NUM);
                                                                                reader.GD_DENOM_NT(n.image);
        break;
      default:
        jj_la1[10] = jj_gen;
        ;
      }
      dot();
      break;
    case FRAC:
      jj_consume_token(FRAC);
      n = jj_consume_token(NUM);
                             reader.GD_DENOM_NT(n.image);
      dot();
      break;
    default:
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

/* prend un nombre quelconque de dot */
  final public void dot() throws ParseException {
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DOT:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_4;
      }
      jj_consume_token(DOT);
                reader.GD_DOT_NT();
    }
  }

  final public void chord() throws ParseException {
    jj_consume_token(LACO);
                 reader.GD_INIT_CH();
    ch_notelist();
    jj_consume_token(RACO);
  }

  final public void ch_notelist() throws ParseException {
    chordnote();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VIR:
        ;
        break;
      default:
        jj_la1[13] = jj_gen;
        break label_5;
      }
      jj_consume_token(VIR);
      chordnote();
    }
  }

/* voir le probleme du tag */
  final public void chordnote() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOTE_ID:
      note();
                    reader.GD_CH_APP_NT();
      break;
    case TAG_ID:
      tag();
      break;
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void tag() throws ParseException {
    tagid();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LTRI:
      jj_consume_token(LTRI);
      targ_e_list();
      jj_consume_token(RTRI);
      break;
    default:
      jj_la1[15] = jj_gen;
      ;
    }
                                                 reader.GD_TAG_ADD() ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAR:
      jj_consume_token(LPAR);
      cnotelist();
      jj_consume_token(RPAR);
      break;
    default:
      jj_la1[16] = jj_gen;
      ;
    }
                                                                                                      reader.GD_TAG_END();
  }

  final public void tagid() throws ParseException {
 Token n;
    n = jj_consume_token(TAG_ID);
                       reader.GD_TAG_START(n.image);
  }

  final public void targ_e_list() throws ParseException {
    targ();
                 reader.GD_TAG_ADD_ARG();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VIR:
        ;
        break;
      default:
        jj_la1[17] = jj_gen;
        break label_6;
      }
      jj_consume_token(VIR);
      targ();
                                                                reader.GD_TAG_ADD_ARG();
    }
  }

  final public void targ() throws ParseException {
 Token n;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUM:
      n = jj_consume_token(NUM);
                          reader.GD_TAG_NARG(n.image);
      break;
    case REAL:
      n = jj_consume_token(REAL);
                          reader.GD_TAG_RARG(n.image);
      break;
    case PLUS:
      jj_consume_token(PLUS);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUM:
        n = jj_consume_token(NUM);
                                 reader.GD_TAG_NARG(n.image);
        break;
      case REAL:
        n = jj_consume_token(REAL);
                                                                                                reader.GD_TAG_RARG(n.image);
        break;
      default:
        jj_la1[18] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    case MINUS:
      jj_consume_token(MINUS);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUM:
        n = jj_consume_token(NUM);
                                 reader.GD_TAG_NARG("-" + n.image);
        break;
      case REAL:
        n = jj_consume_token(REAL);
                                                                                        reader.GD_TAG_RARG("-" + n.image);
        break;
      default:
        jj_la1[19] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    case STRING:
      n = jj_consume_token(STRING);
                                 reader.GD_TAG_SARG(n.image);
      break;
    case TAG_ID:
      n = jj_consume_token(TAG_ID);
                                 reader.GD_TAG_TARG(n.image);
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  public BGuidoParserTokenManager token_source;
  ASCII_CharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[21];
  final private int[] jj_la1_0 = {0x10100000,0x400000,0x100000,0x100000,0xc000,0x400c0000,0x30000,0xc000,0xc000,0x400c0000,0x20000,0x30000,0x800000,0x400000,0x0,0x1000000,0x4000000,0x400000,0x40000000,0x40000000,0x400c0000,};
  final private int[] jj_la1_1 = {0x0,0x0,0xc,0xc,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0xc,0x0,0x0,0x0,0x1,0x1,0x19,};

  public BGuidoParser(java.io.InputStream stream) {
    jj_input_stream = new ASCII_CharStream(stream, 1, 1);
    token_source = new BGuidoParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public BGuidoParser(java.io.Reader stream) {
    jj_input_stream = new ASCII_CharStream(stream, 1, 1);
    token_source = new BGuidoParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public BGuidoParser(BGuidoParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  public void ReInit(BGuidoParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
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
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private Vector<int[]> jj_expentries = new Vector<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[37];
    for (int i = 0; i < 37; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 21; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 37; i++) {
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
