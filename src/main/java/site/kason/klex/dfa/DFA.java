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
  
  @Deprecated
  @Nullable
  public DFAMatchResult match(CharStream charStream){
    return match(charStream,true);
  }
  
  @Deprecated
  @Nullable
  public DFAMatchResult match(CharStream charStream,boolean consume){
    DFASimulator simulator = new DFASimulator(this);
    DFAState matchedState = null;
    int matchedLen = 0;
    int pos = 1;
    int ch;
    while(simulator.nextable() && (ch=charStream.lookAhead(pos))!=CharStream.EOF){
      simulator.next(ch);
      if(simulator.isAccepted()){
        matchedState = simulator.getCurrentState();
        matchedLen = pos;
      }
      pos++;
    }
    if(matchedState!=null){
      int[] matchedChars = new int[matchedLen];
      for(int i=0;i<matchedChars.length;i++){
        matchedChars[i] = charStream.lookAhead(i+1);
      }
      if(consume){
        charStream.skip(matchedLen);
      }
      return new DFAMatchResult(matchedState, matchedLen, matchedChars);
    }else{
      return null;
    }
  }

  public DFAState getStartState() {
    return startState;
  }

  public DFAState[] getAcceptedStates() {
    return acceptedStates.toArray(new DFAState[acceptedStates.size()]);
  }

}
