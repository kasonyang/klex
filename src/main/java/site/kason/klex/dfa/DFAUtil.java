package site.kason.klex.dfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import site.kason.klex.match.Matcher;
import site.kason.klex.nfa.NFA;
import site.kason.klex.nfa.NFAState;
import site.kason.klex.nfa.NFAStateUtil;

/**
 *
 * @author Kason Yang
 */
public class DFAUtil {

  public static DFA buildFromNFA(NFA nfa) {
    NFAState startState = nfa.getStartState();
    Set<NFAState> nfaStates = new HashSet();
    nfaStates.add(startState);
    Map<Set<NFAState>, DFAState> nfaStatesToDFAState = new HashMap();
    NFAState[] acceptedNfaStates = nfa.getAcceptedStates();
    Set<DFAState> acceptedDFAStates = new HashSet();
    DFAState dfaState = getOrCreateDFAState(nfaStates, nfaStatesToDFAState, acceptedNfaStates, acceptedDFAStates);
    return new DFA(dfaState, acceptedDFAStates);
  }

  private static DFAState getOrCreateDFAState(Set<NFAState> nfaStates, Map<Set<NFAState>, DFAState> nfaStatesToDFAState, NFAState[] acceptedNfaStates, Set<DFAState> acceptedDFAStates) {
    nfaStates = NFAStateUtil.getLambdaClosureStates(nfaStates);
    DFAState dfaState = nfaStatesToDFAState.get(nfaStates);
    if (dfaState == null) {
      dfaState = new DFAState();
      nfaStatesToDFAState.put(nfaStates, dfaState);
      for (NFAState s : acceptedNfaStates) {
        if (nfaStates.contains(s)) {
          acceptedDFAStates.add(dfaState);
          break;
        }
      }
      Map<Matcher, Set<NFAState>> nextStatesByMatcher = new HashMap();
      for (NFAState ns : nfaStates) {
        for (Map.Entry<Matcher, Set<NFAState>> e : ns.getNextStates().entrySet()) {
          Matcher matcher = e.getKey();
          Set<NFAState> nextStates = nextStatesByMatcher.get(matcher);
          if (nextStates == null) {
            nextStates = new HashSet();
            nextStatesByMatcher.put(matcher, nextStates);
          }
          nextStates.addAll(e.getValue());
        }
      }
      for (Map.Entry<Matcher, Set<NFAState>> e : nextStatesByMatcher.entrySet()) {
        dfaState.pushNextState(e.getKey(), getOrCreateDFAState(e.getValue(), nfaStatesToDFAState, acceptedNfaStates, acceptedDFAStates));
      }
    }
    return dfaState;
  }

}
