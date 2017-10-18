package site.kason.klex.dfa;

import java.util.Set;
import javax.annotation.Nullable;
import site.kason.klex.CharStream;

/**
 *
 * @author Kason Yang
 */
public class DFA {

  private DFAState startState;

  private Set<DFAState> acceptedStates;

  public DFA(DFAState startState, Set<DFAState> acceptedStates) {
    this.startState = startState;
    this.acceptedStates = acceptedStates;
  }
  
  @Nullable
  public DFAMatchResult match(CharStream charStream){
    DFAState state = this.startState;
    DFAState matchedState = null;
    int matchedLen = 0;
    int pos = 1;
    int ch;
    while(state!=null && (ch=charStream.lookAhead(pos))!=CharStream.EOF){
      state = state.getNextState(ch);
      if(acceptedStates.contains(state)){
        matchedState = state;
        matchedLen = pos;
      }
      pos++;
    }
    if(matchedState!=null){
      int[] matchedChars = charStream.consume(matchedLen);
      return new DFAMatchResult(matchedState, matchedLen, matchedChars);
    }else{
      return null;
    }
  }

}
