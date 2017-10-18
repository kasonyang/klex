package site.kason.klex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import site.kason.klex.dfa.DFA;
import site.kason.klex.dfa.DFASimulator;
import site.kason.klex.dfa.DFAUtil;
import site.kason.klex.nfa.NFAMatchResult;
import site.kason.klex.nfa.NFA;
import site.kason.klex.nfa.NFAState;

/**
 *
 * @author Kason Yang
 */
public class Klexer<TOKEN, TOKEN_RULE extends TokenRule> {

  
  private Map<DFA, TOKEN_RULE> stateToTokenRule = new HashMap();
  
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
    DFA dfa = DFAUtil.buildFromNFA(theNfa);
    this.stateToTokenRule.put(dfa, rule);
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
    
    Set<DFA> dfaList = this.stateToTokenRule.keySet();
    
    List<DFASimulator> simulators = new ArrayList(dfaList.size());
    for(DFA dfa:dfaList){
      simulators.add(new DFASimulator(dfa));
    }
    
    DFASimulator[] simulatorsBuffer = new DFASimulator[simulators.size()];

    int matchedLen = 0;
    TOKEN_RULE matchedRule = null;
    int matchedCharCount = 0;
    while(!simulators.isEmpty()){
      int simulatorSize = simulators.size();
      simulators.toArray(simulatorsBuffer);
      simulators.clear();
      int ch = charStream.lookAhead(++matchedCharCount);
      for(int i=0;i<simulatorSize;i++){
        DFASimulator sm = simulatorsBuffer[i];
        sm.next(ch);
        if(sm.nextable()){
          simulators.add(sm);
        }
      }
      TOKEN_RULE bestMatchedRule = this.selectBestMatchedTokenRule(simulatorsBuffer,0,simulatorSize);
      if(bestMatchedRule!=null){
        matchedRule = bestMatchedRule;
        matchedLen = matchedCharCount;
      }      
    }
    if (matchedRule == null) {
      throw new LexException(
              new OffsetRange(startOffset, startOffset, startLine, startColumn, startLine, startColumn),
              "unexcepted input");
    }
    int[] matchedChars = charStream.consume(matchedLen);
    int stopOffset = charStream.getCurrentOffset() - 1;
    int stopLine = charStream.getCurrentLine();
    int stopColumn = charStream.getCurrentColumn() - 1;
    TOKEN tk = this.tokenFactory.createToken(matchedRule,
            new OffsetRange(startOffset, stopOffset, startLine, startColumn, stopLine, stopColumn), matchedChars);
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
  
  @Nullable
  private TOKEN_RULE selectBestMatchedTokenRule(DFASimulator[] simulators,int offset,int count){
    TOKEN_RULE result = null;
    for(int i=offset;i<offset+count;i++){
      DFASimulator s = simulators[i];
      if(s.isAccepted()){
        DFA dfa = s.getDFA();
        TOKEN_RULE rule = this.stateToTokenRule.get(dfa);
        if(result==null || rule.getPriority() < result.getPriority()){
          result = rule;
        }
      }
    }
    return result;
  }

}
