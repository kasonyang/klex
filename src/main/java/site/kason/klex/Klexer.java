package site.kason.klex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import site.kason.klex.nfa.MatchResult;
import site.kason.klex.nfa.NFA;
import site.kason.klex.nfa.State;

/**
 *
 * @author Kason Yang
 */
public class Klexer<TOKEN, TOKEN_RULE extends TokenRule> {

  private NFA nfa;
  private Map<State, TOKEN_RULE> stateToTokenRule = new HashMap();
  private CharStream charStream;
  //private int offset = 0;

  private TokenFactory<TOKEN, TOKEN_RULE> tokenFactory;

  public Klexer(CharStream charStream, TOKEN_RULE[] tokenRules, TokenFactory<TOKEN, TOKEN_RULE> tokenFactory) {
    this.charStream = charStream;
    for (int i = 0; i < tokenRules.length; i++) {
      this.addTokenRule(tokenRules[i]);
    }
    this.tokenFactory = tokenFactory;
  }

  private void addTokenRule(TOKEN_RULE rule) {
    NFA theNfa = rule.getNFA();
    if (theNfa != null) {
      for (State s : rule.getNFA().getAcceptedStates()) {
        stateToTokenRule.put(s, rule);
      }
      if (this.nfa == null) {
        nfa = rule.getNFA();
      } else {
        nfa.or(rule.getNFA());
      }
    }
  }

  /**
   * select the best token by token value
   *
   * @param states
   * @return
   */
  private TOKEN_RULE selectBestToken(State[] states) {
    TOKEN_RULE bestTokenInfo = null;
    for (State s : states) {
      TOKEN_RULE tk = this.stateToTokenRule.get(s);
      if (bestTokenInfo == null || tk.getPriority() < bestTokenInfo.getPriority()) {
        bestTokenInfo = tk;
      }
    }
    return bestTokenInfo;
  }

  private boolean hasNextToken() {
    return charStream.lookAhead(1) != CharStream.EOF;
  }

  public TOKEN nextToken() throws LexException {
    if (!this.hasNextToken()) {
      return this.tokenFactory.createEOFToken(
              new OffsetRange(
                      charStream.getCurrentOffset(), charStream.getCurrentOffset() + 1, charStream.getCurrentLine(), charStream.getCurrentColumn(), charStream.getCurrentLine(), charStream.getCurrentColumn() + 1
              )
      );
    }
    int startOffset = charStream.getCurrentOffset();
    int startLine = charStream.getCurrentLine();
    int startColumn = charStream.getCurrentColumn();
    MatchResult match = nfa.match(charStream);
    if (match == null) {
      throw new LexException(
              new OffsetRange(startOffset, startOffset, startLine, startColumn, startLine, startColumn),
              "unexcepted input");
    }
    State[] matchedStates = match.getMatchedState();
    TOKEN_RULE tokenType = this.selectBestToken(matchedStates);
    int stopOffset = charStream.getCurrentOffset() - 1;
    int stopLine = charStream.getCurrentLine();
    int stopColumn = charStream.getCurrentColumn() - 1;
    TOKEN tk = this.tokenFactory.createToken(tokenType,
            new OffsetRange(startOffset, stopOffset, startLine, startColumn, stopLine, stopColumn), match.getMatchedChars());
    return tk;
  }

  public List<TOKEN> nextTokens() throws LexException {
    LinkedList<TOKEN> resultTokens = new LinkedList();
    TOKEN token;
    while (this.hasNextToken()) {
      token = this.nextToken();
      resultTokens.add(token);
    }
    return resultTokens;
  }

  public int getRecognizedLength() {
    return charStream.getCurrentOffset();
  }

  public void skip(int count) {
    this.charStream.consume(count);
  }

  public int getCaret() {
    return this.charStream.getCurrentOffset();
  }

}
