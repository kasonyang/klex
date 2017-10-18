package site.kason.klex.nfa;

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
