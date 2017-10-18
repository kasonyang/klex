package site.kason.klex.nfa;

import site.kason.klex.match.RangeCharMatcher;
import site.kason.klex.match.ExcludeCharMatcher;
import site.kason.klex.match.AnyCharMatcher;
import java.util.Arrays;

/**
 *
 * @author Kason Yang
 */
public class NFAUtil {

  public static NFA range(int firstAcceptedChar, int lastAcceptedChar) {
    NFAState startState = new NFAState();
    NFAState acceptedState = new NFAState();
    startState.pushNextState(new RangeCharMatcher(firstAcceptedChar, lastAcceptedChar), acceptedState);
    return new NFA(startState, Arrays.asList(acceptedState));
  }

  public static NFA oneOf(String... str) {
    NFA nfa = null;
    for (int i = 0; i < str.length; i++) {
      NFA theNfa = of(str[i]);
      nfa = (nfa == null) ? theNfa : nfa.or(theNfa);
    }
    return nfa;
  }

  public static NFA of(String str) {
    NFAState startState = new NFAState();
    NFAState currentState = startState;
    int strLen = str.length();
    int offset = 0;
    while (offset < strLen) {
      char nextInput = str.charAt(offset++);
      NFAState nextState = new NFAState();
      currentState.pushNextState(nextInput, nextState);
      currentState = nextState;
    }
    return new NFA(startState, Arrays.asList(currentState));
  }

  public static NFA exclude(int... excludes) {
    NFAState startState = new NFAState();
    NFAState acceptedState = new NFAState();
    startState.pushNextState(new ExcludeCharMatcher(excludes), acceptedState);
    return new NFA(startState, Arrays.asList(acceptedState));
  }

  public static NFA oneOf(int... chars) {
    NFAState startState = new NFAState();
    NFAState acceptedState = new NFAState();
    for (int i = 0; i < chars.length; i++) {
      startState.pushNextState(chars[i], acceptedState);
    }
    return new NFA(startState, Arrays.asList(acceptedState));
  }

  public static NFA anyChar() {
    NFAState startState = new NFAState();
    NFAState acceptedState = new NFAState();
    startState.pushNextState(new AnyCharMatcher(), acceptedState);
    return new NFA(startState, Arrays.asList(acceptedState));
  }

}
