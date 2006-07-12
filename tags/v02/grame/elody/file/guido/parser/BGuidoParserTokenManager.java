package grame.elody.file.guido.parser;

public class BGuidoParserTokenManager implements BGuidoParserConstants {
	  static int commentCounter;
	  private final int jjStopAtPos(int pos, int kind)
	  {
	     jjmatchedKind = kind;
	     jjmatchedPos = pos;
	     return pos + 1;
	  }
	  private final int jjMoveStringLiteralDfa0_1()
	  {
	     switch(curChar)
	     {
	        case 10:
	           return jjStopAtPos(0, 8);
	        case 13:
	           return jjStopAtPos(0, 9);
	        default :
	           return 1;
	     }
	  }
	  private final int jjStopStringLiteralDfa_0(int pos, long active0)
	  {
	     switch (pos)
	     {
	        default :
	           return -1;
	     }
	  }
	  private final int jjStartNfa_0(int pos, long active0)
	  {
	     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
	  }
	/*  private final int jjStartNfaWithStates_0(int pos, int kind, int state)
	  {
	     jjmatchedKind = kind;
	     jjmatchedPos = pos;
	     try { curChar = input_stream.readChar(); }
	     catch(java.io.IOException e) { return pos + 1; }
	     return jjMoveNfa_0(state, pos + 1);
	  }  */
	  private final int jjMoveStringLiteralDfa0_0()
	  {
	     switch(curChar)
	     {
	        case 35:
	           return jjStopAtPos(0, 14);
	        case 37:
	           return jjStopAtPos(0, 6);
	        case 38:
	           return jjStopAtPos(0, 15);
	        case 40:
	           jjmatchedKind = 26;
	           return jjMoveStringLiteralDfa1_0(0x80L);
	        case 41:
	           return jjStopAtPos(0, 27);
	        case 42:
	           return jjStopAtPos(0, 16);
	        case 43:
	           return jjStopAtPos(0, 18);
	        case 44:
	           return jjStopAtPos(0, 22);
	        case 45:
	           return jjStopAtPos(0, 19);
	        case 46:
	           return jjStopAtPos(0, 23);
	        case 47:
	           return jjStopAtPos(0, 17);
	        case 60:
	           return jjStopAtPos(0, 24);
	        case 62:
	           return jjStopAtPos(0, 25);
	        case 91:
	           return jjStopAtPos(0, 28);
	        case 93:
	           return jjStopAtPos(0, 29);
	        case 123:
	           return jjStopAtPos(0, 20);
	        case 125:
	           return jjStopAtPos(0, 21);
	        default :
	           return jjMoveNfa_0(1, 0);
	     }
	  }
	  private final int jjMoveStringLiteralDfa1_0(long active0)
	  {
	     try { curChar = input_stream.readChar(); }
	     catch(java.io.IOException e) {
	        jjStopStringLiteralDfa_0(0, active0);
	        return 1;
	     }
	     switch(curChar)
	     {
	        case 42:
	           if ((active0 & 0x80L) != 0L)
	              return jjStopAtPos(1, 7);
	           break;
	        default :
	           break;
	     }
	     return jjStartNfa_0(0, active0);
	  }
	  private final void jjCheckNAdd(int state)
	  {
	     if (jjrounds[state] != jjround)
	     {
	        jjstateSet[jjnewStateCnt++] = state;
	        jjrounds[state] = jjround;
	     }
	  }
	  private final void jjAddStates(int start, int end)
	  {
	     do {
	        jjstateSet[jjnewStateCnt++] = jjnextStates[start];
	     } while (start++ != end);
	  }
	  private final void jjCheckNAddTwoStates(int state1, int state2)
	  {
	     jjCheckNAdd(state1);
	     jjCheckNAdd(state2);
	  }
	  private final void jjCheckNAddStates(int start, int end)
	  {
	     do {
	        jjCheckNAdd(jjnextStates[start]);
	     } while (start++ != end);
	  }
	/*  private final void jjCheckNAddStates(int start)
	  {
	     jjCheckNAdd(jjnextStates[start]);
	     jjCheckNAdd(jjnextStates[start + 1]);
	  } */
	  static final long[] jjbitVec0 = {
	     0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
	  };
	  private final int jjMoveNfa_0(int startState, int curPos)
	  {
	     int startsAt = 0;
	     jjnewStateCnt = 14;
	     int i = 1;
	     jjstateSet[0] = startState;
	     int kind = 0x7fffffff;
	     for (;;)
	     {
	        if (++jjround == 0x7fffffff)
	           ReInitRounds();
	        if (curChar < 64)
	        {
	           long l = 1L << curChar;
	          /* MatchLoop: */ do
	           {
	              switch(jjstateSet[--i])
	              {
	                 case 1:
	                    if ((0x3ff000000000000L & l) != 0L)
	                    {
	                       if (kind > 30)
	                          kind = 30;
	                       jjCheckNAddStates(0, 2);
	                    }
	                    else if (curChar == 34)
	                       jjCheckNAddStates(3, 5);
	                    break;
	                 case 4:
	                    if ((0x3ff000000000000L & l) == 0L)
	                       break;
	                    if (kind > 35)
	                       kind = 35;
	                    jjstateSet[jjnewStateCnt++] = 4;
	                    break;
	                 case 5:
	                    if (curChar == 34)
	                       jjCheckNAddStates(3, 5);
	                    break;
	                 case 7:
	                    if ((0xfffffffbffffffffL & l) != 0L)
	                       jjCheckNAddStates(3, 5);
	                    break;
	                 case 8:
	                    if (curChar == 34 && kind > 36)
	                       kind = 36;
	                    break;
	                 case 9:
	                    if ((0x3ff000000000000L & l) == 0L)
	                       break;
	                    if (kind > 30)
	                       kind = 30;
	                    jjCheckNAddStates(0, 2);
	                    break;
	                 case 10:
	                    if ((0x3ff000000000000L & l) == 0L)
	                       break;
	                    if (kind > 30)
	                       kind = 30;
	                    jjCheckNAdd(10);
	                    break;
	                 case 11:
	                    if ((0x3ff000000000000L & l) != 0L)
	                       jjCheckNAddTwoStates(11, 12);
	                    break;
	                 case 12:
	                    if (curChar == 46)
	                       jjCheckNAdd(13);
	                    break;
	                 case 13:
	                    if ((0x3ff000000000000L & l) == 0L)
	                       break;
	                    if (kind > 32)
	                       kind = 32;
	                    jjCheckNAdd(13);
	                    break;
	                 default : break;
	              }
	           } while(i != startsAt);
	        }
	        else if (curChar < 128)
	        {
	           long l = 1L << (curChar & 077);
	          /* MatchLoop: */ do
	           {
	              switch(jjstateSet[--i])
	              {
	                 case 1:
	                    if ((0x7fffffe00000000L & l) != 0L)
	                    {
	                       if (kind > 34)
	                          kind = 34;
	                       jjCheckNAdd(0);
	                    }
	                    else if (curChar == 92)
	                       jjstateSet[jjnewStateCnt++] = 3;
	                    else if (curChar == 95)
	                    {
	                       if (kind > 34)
	                          kind = 34;
	                    }
	                    break;
	                 case 0:
	                    if ((0x7fffffe00000000L & l) == 0L)
	                       break;
	                    if (kind > 34)
	                       kind = 34;
	                    jjCheckNAdd(0);
	                    break;
	                 case 2:
	                    if (curChar == 92)
	                       jjstateSet[jjnewStateCnt++] = 3;
	                    break;
	                 case 3:
	                 case 4:
	                    if ((0x7fffffe87fffffeL & l) == 0L)
	                       break;
	                    if (kind > 35)
	                       kind = 35;
	                    jjCheckNAdd(4);
	                    break;
	                 case 6:
	                    if (curChar == 92)
	                       jjstateSet[jjnewStateCnt++] = 5;
	                    break;
	                 case 7:
	                    jjAddStates(3, 5);
	                    break;
	                 default : break;
	              }
	           } while(i != startsAt);
	        }
	        else
	        {
	           int i2 = (curChar & 0xff) >> 6;
	           long l2 = 1L << (curChar & 077);
	          /* MatchLoop: */ do
	           {
	              switch(jjstateSet[--i])
	              {
	                 case 7:
	                    if ((jjbitVec0[i2] & l2) != 0L)
	                       jjAddStates(3, 5);
	                    break;
	                 default : break;
	              }
	           } while(i != startsAt);
	        }
	        if (kind != 0x7fffffff)
	        {
	           jjmatchedKind = kind;
	           jjmatchedPos = curPos;
	           kind = 0x7fffffff;
	        }
	        ++curPos;
	        if ((i = jjnewStateCnt) == (startsAt = 14 - (jjnewStateCnt = startsAt)))
	           return curPos;
	        try { curChar = input_stream.readChar(); }
	        catch(java.io.IOException e) { return curPos; }
	     }
	  }
	  private final int jjMoveStringLiteralDfa0_2()
	  {
	     switch(curChar)
	     {
	        case 40:
	           return jjMoveStringLiteralDfa1_2(0x800L);
	        case 42:
	           return jjMoveStringLiteralDfa1_2(0x1000L);
	        default :
	           return 1;
	     }
	  }
	  private final int jjMoveStringLiteralDfa1_2(long active0)
	  {
	     try { curChar = input_stream.readChar(); }
	     catch(java.io.IOException e) {
	        return 1;
	     }
	     switch(curChar)
	     {
	        case 41:
	           if ((active0 & 0x1000L) != 0L)
	              return jjStopAtPos(1, 12);
	           break;
	        case 42:
	           if ((active0 & 0x800L) != 0L)
	              return jjStopAtPos(1, 11);
	           break;
	        default :
	           return 2;
	     }
	     return 2;
	  }
	  static final int[] jjnextStates = {
	     10, 11, 12, 6, 7, 8, 
	  };
	  public static final String[] jjstrLiteralImages = {
	  "", null, null, null, null, null, null, null, null, null, null, null, null, 
	  null, "\43", "\46", "\52", "\57", "\53", "\55", "\173", "\175", "\54", "\56", "\74", 
	  "\76", "\50", "\51", "\133", "\135", null, null, null, null, null, null, null, };
	  public static final String[] lexStateNames = {
	     "DEFAULT", 
	     "IN_LINE_COMMENT", 
	     "IN_COMMENT", 
	  };
	  public static final int[] jjnewLexState = {
	     -1, -1, -1, -1, -1, -1, 1, 2, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
	     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
	  };
	  static final long[] jjtoToken = {
	     0x1d7fffc001L, 
	  };
	  static final long[] jjtoSkip = {
	     0x3ffeL, 
	  };
	  private ASCII_CharStream input_stream;
	  private final int[] jjrounds = new int[14];
	  private final int[] jjstateSet = new int[28];
	  StringBuffer image;
	  int jjimageLen;
	  int lengthOfMatch;
	  protected char curChar;
	  public BGuidoParserTokenManager(ASCII_CharStream stream)
	  {
	     if (ASCII_CharStream.staticFlag)
	        throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
	     input_stream = stream;
	  }
	  public BGuidoParserTokenManager(ASCII_CharStream stream, int lexState)
	  {
	     this(stream);
	     SwitchTo(lexState);
	  }
	  public void ReInit(ASCII_CharStream stream)
	  {
	     jjmatchedPos = jjnewStateCnt = 0;
	     curLexState = defaultLexState;
	     input_stream = stream;
	     ReInitRounds();
	  }
	  private final void ReInitRounds()
	  {
	     int i;
	     jjround = 0x80000001;
	     for (i = 14; i-- > 0;)
	        jjrounds[i] = 0x80000000;
	  }
	  public void ReInit(ASCII_CharStream stream, int lexState)
	  {
	     ReInit(stream);
	     SwitchTo(lexState);
	  }
	  public void SwitchTo(int lexState)
	  {
	     if (lexState >= 3 || lexState < 0)
	        throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
	     else
	        curLexState = lexState;
	  }

	  private final Token jjFillToken()
	  {
	     Token t = Token.newToken(jjmatchedKind);
	     t.kind = jjmatchedKind;
	     String im = jjstrLiteralImages[jjmatchedKind];
	     t.image = (im == null) ? input_stream.GetImage() : im;
	     t.beginLine = input_stream.getBeginLine();
	     t.beginColumn = input_stream.getBeginColumn();
	     t.endLine = input_stream.getEndLine();
	     t.endColumn = input_stream.getEndColumn();
	     return t;
	  }

	  int curLexState = 0;
	  int defaultLexState = 0;
	  int jjnewStateCnt;
	  int jjround;
	  int jjmatchedPos;
	  int jjmatchedKind;

	  public final Token getNextToken() 
	  {
	    Token matchedToken;
	    int curPos = 0;

	    EOFLoop :
	    for (;;)
	    {   
	     try   
	     {     
	        curChar = input_stream.BeginToken();
	     }     
	     catch(java.io.IOException e)
	     {        
	        jjmatchedKind = 0;
	        matchedToken = jjFillToken();
	        return matchedToken;
	     }
	     image = null;
	     jjimageLen = 0;

	     switch(curLexState)
	     {
	       case 0:
	         try { 
	            while ((curChar < 64 && (0x100002600L & (1L << curChar)) != 0L) || 
	                   (curChar >> 6) == 1 && (0x1000000000000000L & (1L << (curChar & 077))) != 0L)
	               curChar = input_stream.BeginToken();
	         }
	         catch (java.io.IOException e1) { continue EOFLoop; }
	         jjmatchedKind = 0x7fffffff;
	         jjmatchedPos = 0;
	         curPos = jjMoveStringLiteralDfa0_0();
	         break;
	       case 1:
	         jjmatchedKind = 0x7fffffff;
	         jjmatchedPos = 0;
	         curPos = jjMoveStringLiteralDfa0_1();
	         if (jjmatchedPos == 0 && jjmatchedKind > 10)
	         {
	            jjmatchedKind = 10;
	         }
	         break;
	       case 2:
	         jjmatchedKind = 0x7fffffff;
	         jjmatchedPos = 0;
	         curPos = jjMoveStringLiteralDfa0_2();
	         if (jjmatchedPos == 0 && jjmatchedKind > 13)
	         {
	            jjmatchedKind = 13;
	         }
	         break;
	     }
	       if (jjmatchedKind != 0x7fffffff)
	       {
	          if (jjmatchedPos + 1 < curPos)
	             input_stream.backup(curPos - jjmatchedPos - 1);
	          if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
	          {
	             matchedToken = jjFillToken();
	         if (jjnewLexState[jjmatchedKind] != -1)
	           curLexState = jjnewLexState[jjmatchedKind];
	             return matchedToken;
	          }
	          else
	          {
	             SkipLexicalActions(null);
	           if (jjnewLexState[jjmatchedKind] != -1)
	             curLexState = jjnewLexState[jjmatchedKind];
	             continue EOFLoop;
	          }
	       }
	       int error_line = input_stream.getEndLine();
	       int error_column = input_stream.getEndColumn();
	       String error_after = null;
	       boolean EOFSeen = false;
	       try { input_stream.readChar(); input_stream.backup(1); }
	       catch (java.io.IOException e1) {
	          EOFSeen = true;
	          error_after = curPos <= 1 ? "" : input_stream.GetImage();
	          if (curChar == '\n' || curChar == '\r') {
	             error_line++;
	             error_column = 0;
	          }
	          else
	             error_column++;
	       }
	       if (!EOFSeen) {
	          input_stream.backup(1);
	          error_after = curPos <= 1 ? "" : input_stream.GetImage();
	       }
	       throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
	    }
	  }

	  final void SkipLexicalActions(Token matchedToken)
	  {
	     switch(jjmatchedKind)
	     {
	        case 7 :
	           if (image == null)
	              image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
	           else
	              image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
	                    commentCounter = 0;
	           break;
	        case 11 :
	           if (image == null)
	              image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
	           else
	              image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
	                  commentCounter++;
	           break;
	        case 12 :
	           if (image == null)
	              image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
	           else
	              image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
	              if (commentCounter-- == 0) SwitchTo(DEFAULT);
	           break;
	        default :
	           break;
	     }
	  }
}
