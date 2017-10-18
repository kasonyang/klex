package site.kason.klex.nfa;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Kason Yang
 */
public class NFAStateUtil {

  public static Set<NFAState> getLambdaClosureStates(Set<NFAState> states) {
    Queue<NFAState> todos = new LinkedList();
    todos.addAll(states);
    Set<NFAState> results = new HashSet();
    while (!todos.isEmpty()) {
      NFAState s = todos.poll();
      if (results.add(s)) {
        NFAState[] lambdaStates = s.getLambdaClosureStates();
        todos.addAll(Arrays.asList(lambdaStates));
      }
    }
    return results;
  }
}
