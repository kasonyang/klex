package site.kason.klex.util;

import javax.annotation.Nullable;
import site.kason.klex.CharStream;
import site.kason.klex.dfa.DFA;
import site.kason.klex.dfa.DFASimulator;
import site.kason.klex.dfa.DFAState;

/**
 *
 * @author Kason Yang
 */
public class DFAMatchUtil {

  @Nullable
  public static DFAMatchResult match(DFA dfa, CharStream charStream) {
    return match(dfa, charStream, true);
  }

  @Nullable
  public static DFAMatchResult match(DFA dfa, CharStream charStream, boolean consume) {
    DFASimulator simulator = new DFASimulator(dfa);
    DFAState matchedState = null;
    int matchedLen = 0;
    int pos = 1;
    int ch;
    while (simulator.nextable() && (ch = charStream.lookAhead(pos)) != CharStream.EOF) {
      simulator.next(ch);
      if (simulator.isAccepted()) {
        matchedState = simulator.getCurrentState();
        matchedLen = pos;
      }
      pos++;
    }
    if (matchedState != null) {
      int[] matchedChars = new int[matchedLen];
      for (int i = 0; i < matchedChars.length; i++) {
        matchedChars[i] = charStream.lookAhead(i + 1);
      }
      if (consume) {
        charStream.skip(matchedLen);
      }
      return new DFAMatchResult(matchedState, matchedLen, matchedChars);
    } else {
      return null;
    }
  }
}
