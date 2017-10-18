package site.kason.klex.match;

/**
 *
 * @author Kason Yang
 */
public class CharMatcher implements Matcher {

  private final int character;

  public CharMatcher(int character) {
    this.character = character;
  }

  @Override
  public boolean isMatched(int character) {
    return this.character == character;
  }

}
