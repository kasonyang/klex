package site.kason.klex.dfa;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import site.kason.klex.nfa.Matcher;

/**
 *
 * @author Kason Yang
 */
public class DFAState {

  private Map<Matcher, DFAState> nextStateMap = new HashMap();

  public void pushNextState(Matcher matcher, DFAState state) {
    this.nextStateMap.put(matcher, state);
  }

  @Nullable
  public DFAState getNextState(int nextChar) {
    for (Map.Entry<Matcher, DFAState> e : nextStateMap.entrySet()) {
      if (e.getKey().isMatched(nextChar)) {
        return e.getValue();
      }
    }
    return null;
  }

}
