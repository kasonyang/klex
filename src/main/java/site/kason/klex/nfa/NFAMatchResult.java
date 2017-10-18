package site.kason.klex.nfa;

import site.kason.klex.nfa.NFAState;

/**
 *
 * @author Kason Yang
 */
public class NFAMatchResult {

  private NFAState[] matchedState;

  private int matchedLength;

  private int[] matchedChars;

  public NFAMatchResult(NFAState[] matchedState, int matchedLength, int[] matchedChars) {
    this.matchedState = matchedState;
    this.matchedLength = matchedLength;
    this.matchedChars = matchedChars;
  }

  public NFAState[] getMatchedState() {
    return matchedState;
  }

  public int getMatchedLength() {
    return matchedLength;
  }

  public int[] getMatchedChars() {
    return matchedChars;
  }

}
