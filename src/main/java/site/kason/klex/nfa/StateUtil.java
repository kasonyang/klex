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
public class StateUtil {

  public static Set<State> getLambdaClosureStates(Set<State> states) {
    Queue<State> todos = new LinkedList();
    todos.addAll(states);
    Set<State> results = new HashSet();
    while (!todos.isEmpty()) {
      State s = todos.poll();
      if (results.add(s)) {
        State[] lambdaStates = s.getLambdaClosureStates();
        todos.addAll(Arrays.asList(lambdaStates));
      }
    }
    return results;
  }
}
