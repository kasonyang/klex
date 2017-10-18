package site.kason.klex.match;

/**
 *
 * @author Kason Yang
 */
public class AnyCharMatcher implements Matcher {

  @Override
  public boolean isMatched(int character) {
    return true;
  }

}
