package site.kason.klex.nfa;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
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
    Set<NFAState> currentStates = new HashSet();
    currentStates.add(startState);
    currentStates = NFAStateUtil.getLambdaClosureStates(currentStates);
    NFAState[] matchedState = null;//this.findAcceptedState(currentStates);
    int inputOffset = 1;
    int matchedLen = 0;
    while (!currentStates.isEmpty() && inputStream.lookAhead(inputOffset) != CharStream.EOF) {
      Set<NFAState> nextStates = new HashSet();
      int input = inputStream.lookAhead(inputOffset++);
      for (NFAState s : currentStates) {
        NFAState[] nexts = s.getNextStates(input);
        nextStates.addAll(Arrays.asList(nexts));
      }
      nextStates = NFAStateUtil.getLambdaClosureStates(nextStates);
      NFAState[] found = this.findAcceptedState(nextStates);
      if (found != null && found.length > 0) {
        matchedState = found;
        matchedLen = inputOffset - 1;
      }
      currentStates = nextStates;
    }
    int[] matchedChars = inputStream.consume(matchedLen);
    return matchedState != null ? new NFAMatchResult(matchedState, matchedLen, matchedChars) : null;
  }

  private NFAState[] findAcceptedState(Set<NFAState> states) {
    List<NFAState> accepteds = new LinkedList();
    for (NFAState s : states) {
      if (this.acceptedStates.contains(s)) {
        accepteds.add(s);
      }
    }
    return accepteds.toArray(new NFAState[accepteds.size()]);
  }

  public NFAState[] getAcceptedStates() {
    NFAState[] res = (NFAState[]) Array.newInstance(NFAState.class, acceptedStates.size());
    return acceptedStates.toArray(res);
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
