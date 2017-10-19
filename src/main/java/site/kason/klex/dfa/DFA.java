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

  public DFAState getStartState() {
    return startState;
  }

  public DFAState[] getAcceptedStates() {
    return acceptedStates.toArray(new DFAState[acceptedStates.size()]);
  }

}
