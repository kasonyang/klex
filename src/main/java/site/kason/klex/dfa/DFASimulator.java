package site.kason.klex.dfa;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import site.kason.klex.CharStream;

/**
 *
 * @author Kason Yang
 */
public class DFASimulator {

  private DFAState currentState;

  private Set<DFAState> acceptedStates;
  
  private final DFA dfa;

  public DFASimulator(DFA dfa) {
    this.dfa = dfa;
    this.currentState = dfa.getStartState();
    this.acceptedStates = new HashSet(Arrays.asList(dfa.getAcceptedStates()));
  }

  public void next(int input) {
    currentState = currentState.getNextState(input);
  }

  public boolean isAccepted() {
    return acceptedStates.contains(currentState);
  }

  public DFAState getCurrentState() {
    return currentState;
  }

  public boolean nextable() {
    return currentState != null;
  }
  
  public DFA getDFA(){
    return dfa;
  }

}
