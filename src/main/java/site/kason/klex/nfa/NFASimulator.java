package site.kason.klex.nfa;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Kason Yang
 */
public class NFASimulator {

  private final NFA nfa;

  private Set<NFAState> currentStates = new HashSet();

  private final Set<NFAState> nextStates;

  private final Set<NFAState> nfaAcceptedStates;

  public NFASimulator(NFA nfa) {
    this.nfa = nfa;
    this.currentStates.addAll(NFAStateUtil.getLambdaClosureStates(Collections.singleton(nfa.getStartState())));
    this.nfaAcceptedStates = new HashSet(Arrays.asList(nfa.getAcceptedStates()));
    this.nextStates = new HashSet();
  }

  public boolean nextable() {
    return !this.currentStates.isEmpty();
  }

  public void next(int input) {
    if (currentStates.isEmpty()) {
      throw new IllegalStateException("Current states is empty.");
    }
    for (NFAState s : currentStates) {
      NFAState[] ns = s.getNextStates(input);
      nextStates.addAll(Arrays.asList(ns));
    }
    currentStates = NFAStateUtil.getLambdaClosureStates(nextStates);
    nextStates.clear();
  }

  public Set<NFAState> getAcceptedStates() {
    Set<NFAState> results = new HashSet();
    for (NFAState s : currentStates) {
      if (this.nfaAcceptedStates.contains(s)) {
        results.add(s);
      }
    }
    return results;
  }

}
