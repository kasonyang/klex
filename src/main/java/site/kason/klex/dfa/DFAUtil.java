package site.kason.klex.dfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import site.kason.klex.nfa.Matcher;
import site.kason.klex.nfa.NFA;
import site.kason.klex.nfa.State;
import site.kason.klex.nfa.StateUtil;

/**
 *
 * @author Kason Yang
 */
public class DFAUtil {

  public static DFA buildFromNFA(NFA nfa) {
    State startState = nfa.getStartState();
    Set<State> nfaStates = new HashSet();
    nfaStates.add(startState);
    Map<Set<State>, DFAState> nfaStatesToDFAState = new HashMap();
    State[] acceptedNfaStates = nfa.getAcceptedStates();
    Set<DFAState> acceptedDFAStates = new HashSet();
    DFAState dfaState = getOrCreateDFAState(nfaStates, nfaStatesToDFAState, acceptedNfaStates, acceptedDFAStates);
    return new DFA(dfaState, acceptedDFAStates);
  }

  private static DFAState getOrCreateDFAState(Set<State> nfaStates, Map<Set<State>, DFAState> nfaStatesToDFAState, State[] acceptedNfaStates, Set<DFAState> acceptedDFAStates) {
    nfaStates = StateUtil.getLambdaClosureStates(nfaStates);
    DFAState dfaState = nfaStatesToDFAState.get(nfaStates);
    if (dfaState == null) {
      dfaState = new DFAState();
      nfaStatesToDFAState.put(nfaStates, dfaState);
      for (State s : acceptedNfaStates) {
        if (nfaStates.contains(s)) {
          acceptedDFAStates.add(dfaState);
          break;
        }
      }
      Map<Matcher, Set<State>> nextStatesByMatcher = new HashMap();
      for (State ns : nfaStates) {
        for (Map.Entry<Matcher, Set<State>> e : ns.getNextStates().entrySet()) {
          Matcher matcher = e.getKey();
          Set<State> nextStates = nextStatesByMatcher.get(matcher);
          if (nextStates == null) {
            nextStates = new HashSet();
            nextStatesByMatcher.put(matcher, nextStates);
          }
          nextStates.addAll(e.getValue());
        }
      }
      for (Map.Entry<Matcher, Set<State>> e : nextStatesByMatcher.entrySet()) {
        dfaState.pushNextState(e.getKey(), getOrCreateDFAState(e.getValue(), nfaStatesToDFAState, acceptedNfaStates, acceptedDFAStates));
      }
    }
    return dfaState;
  }

}
