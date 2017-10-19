package site.kason.klex.nfa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import site.kason.klex.CharStream;

/**
 *
 * @author Kason Yang
 */
public class NFA {

  private NFAState startState;

  private List<NFAState> acceptedStates;

  public NFA(NFAState startState, List<NFAState> acceptedStates) {
    this.startState = startState;
    this.acceptedStates = acceptedStates;
  }

  public NFAMatchResult match(CharStream inputStream) {
    NFASimulator simulator = new NFASimulator(this);
    Set<NFAState> matchedStates = null;//this.findAcceptedState(currentStates);
    int inputOffset = 1;
    int matchedLen = 0;
    while (simulator.nextable() && inputStream.lookAhead(inputOffset) != CharStream.EOF) {      
      int input = inputStream.lookAhead(inputOffset++);
      simulator.next(input);
      Set<NFAState> found = simulator.getAcceptedStates();
      if (!found.isEmpty()) {
        matchedStates = found;
        matchedLen = inputOffset - 1;
      }
    }
    int[] matchedChars = inputStream.consume(matchedLen);
    return matchedStates != null 
            ? new NFAMatchResult(matchedStates.toArray(new NFAState[matchedStates.size()]), matchedLen, matchedChars) 
            : null;
  }

  public NFAState[] getAcceptedStates() {
    return acceptedStates.toArray(new NFAState[acceptedStates.size()]);
  }

  public NFAState getStartState() {
    return startState;
  }

  public NFA or(NFA nfa2) {
    NFAState newStartState = new NFAState();
    newStartState.pushLambdaClosureState(this.getStartState());
    newStartState.pushLambdaClosureState(nfa2.getStartState());
    NFAState[] acList1 = this.getAcceptedStates();
    NFAState[] acList2 = nfa2.getAcceptedStates();
    List<NFAState> newAcceptedStates = new ArrayList(acList1.length + acList2.length);
    newAcceptedStates.addAll(Arrays.asList(acList1));
    newAcceptedStates.addAll(Arrays.asList(acList2));
    this.startState = newStartState;
    this.acceptedStates = newAcceptedStates;
    return this;
  }

  public NFA concat(NFA nfa2) {
    NFAState newStartState = this.getStartState();
    NFAState[] accpetedState = this.getAcceptedStates();
    for (NFAState ac : accpetedState) {
      ac.pushLambdaClosureState(nfa2.getStartState());
    }
    this.startState = newStartState;
    this.acceptedStates = Arrays.asList(nfa2.getAcceptedStates());
    return this;
  }

  public NFA closure() {
    NFAState[] acList = this.getAcceptedStates();
    for (NFAState s : acList) {
      s.pushLambdaClosureState(startState);
      startState.pushLambdaClosureState(s);
    }
    return this;
  }

}
