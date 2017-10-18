package site.kason.klex.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Kason Yang
 */
public class State {

  private Map<Matcher, Set<State>> nextStates = new HashMap();

  private Set<State> closureStates = new HashSet();

  public void pushNextState(int input, State nextState) {
    pushNextState(new CharMatcher(input), nextState);
  }

  public void pushNextState(Matcher matcher, State nextState) {
    Set<State> list = this.nextStates.get(matcher);
    if (list == null) {
      list = new HashSet();
      nextStates.put(matcher, list);
    }
    list.add(nextState);
  }

  public State[] getNextStates(int input) {
    Set<State> result = new HashSet();
    if (!this.nextStates.isEmpty()) {
      for (Map.Entry<Matcher, Set<State>> e : this.nextStates.entrySet()) {
        if (e.getKey().isMatched(input)) {
          result.addAll(e.getValue());
        }
      }
    }
    return result.toArray(new State[result.size()]);
  }

  public State[] getLambdaClosureStates() {
    return closureStates.toArray(new State[closureStates.size()]);
  }

  public void pushLambdaClosureState(State state) {
    this.closureStates.add(state);
  }

  public Map<Matcher, Set<State>> getNextStates() {
    return new HashMap(nextStates);
  }

}
