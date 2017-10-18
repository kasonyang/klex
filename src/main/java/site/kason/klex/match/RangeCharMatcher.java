package site.kason.klex.match;

/**
 *
 * @author Kason Yang
 */
public class RangeCharMatcher implements Matcher {

  private int firstAcceptedChar;

  private int lastAcceptedChar;

  public RangeCharMatcher(int firstAcceptedChar, int lastAcceptedChar) {
    this.firstAcceptedChar = firstAcceptedChar;
    this.lastAcceptedChar = lastAcceptedChar;
  }

  @Override
  public boolean isMatched(int character) {
    return character >= firstAcceptedChar && character <= lastAcceptedChar;
  }

}
