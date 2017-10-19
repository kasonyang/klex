package site.kason.klex.util;

import site.kason.klex.dfa.DFAState;

/**
 *
 * @author Kason Yang
 */
public class DFAMatchResult {

  private final DFAState matchedState;

  private final int matchedLength;

  private final int[] matchedChars;

  public DFAMatchResult(DFAState matchedState, int matchedLength, int[] matchedChars) {
    this.matchedState = matchedState;
    this.matchedLength = matchedLength;
    this.matchedChars = matchedChars;
  }

  public DFAState getMatchedState() {
    return matchedState;
  }

  public int getMatchedLength() {
    return matchedLength;
  }

  public int[] getMatchedChars() {
    return matchedChars;
  }

}
