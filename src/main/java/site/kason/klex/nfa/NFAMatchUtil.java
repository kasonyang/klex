package site.kason.klex.nfa;

import java.util.Set;
import site.kason.klex.CharStream;

/**
 *
 * @author Kason Yang
 */
public class NFAMatchUtil {

  public static NFAMatchResult match(NFA nfa, CharStream inputStream) {
    NFASimulator simulator = new NFASimulator(nfa);
    Set<NFAState> matchedStates = null;//this.findAcceptedState(currentStates);
    int inputOffset = 1;
    int matchedLen = 0;
    while (simulator.nextable() && inputStream.lookAhead(inputOffset) != CharStream.EOF) {
      int input = inputStream.lookAhead(inputOffset++);
      simulator.next(input);
      Set<NFAState> found = simulator.getAcceptedStates();
      if (!found.isEmpty()) {
        matchedStates = found;
        matchedLen = inputOffset - 1;
      }
    }
    int[] matchedChars = inputStream.consume(matchedLen);
    return matchedStates != null
            ? new NFAMatchResult(matchedStates.toArray(new NFAState[matchedStates.size()]), matchedLen, matchedChars)
            : null;
  }

}
