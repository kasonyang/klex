package site.kason.klex.nfa;

import site.kason.klex.match.Matcher;
import site.kason.klex.match.CharMatcher;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Kason Yang
 */
public class NFAState {

  private Map<Matcher, Set<NFAState>> nextStates = new HashMap();

  private Set<NFAState> closureStates = new HashSet();

  public void pushNextState(int input, NFAState nextState) {
    pushNextState(new CharMatcher(input), nextState);
  }

  public void pushNextState(Matcher matcher, NFAState nextState) {
    Set<NFAState> list = this.nextStates.get(matcher);
    if (list == null) {
      list = new HashSet();
      nextStates.put(matcher, list);
    }
    list.add(nextState);
  }

  public NFAState[] getNextStates(int input) {
    Set<NFAState> result = new HashSet();
    if (!this.nextStates.isEmpty()) {
      for (Map.Entry<Matcher, Set<NFAState>> e : this.nextStates.entrySet()) {
        if (e.getKey().isMatched(input)) {
          result.addAll(e.getValue());
        }
      }
    }
    return result.toArray(new NFAState[result.size()]);
  }

  public NFAState[] getLambdaClosureStates() {
    return closureStates.toArray(new NFAState[closureStates.size()]);
  }

  public void pushLambdaClosureState(NFAState state) {
    this.closureStates.add(state);
  }

  public Map<Matcher, Set<NFAState>> getNextStates() {
    return new HashMap(nextStates);
  }

}
