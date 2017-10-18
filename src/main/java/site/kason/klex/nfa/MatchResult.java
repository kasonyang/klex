package site.kason.klex.nfa;

/**
 *
 * @author Kason Yang
 */
public class MatchResult {

  private State[] matchedState;

  private int matchedLength;

  private int[] matchedChars;

  public MatchResult(State[] matchedState, int matchedLength, int[] matchedChars) {
    this.matchedState = matchedState;
    this.matchedLength = matchedLength;
    this.matchedChars = matchedChars;
  }

  public State[] getMatchedState() {
    return matchedState;
  }

  public int getMatchedLength() {
    return matchedLength;
  }

  public int[] getMatchedChars() {
    return matchedChars;
  }

}
