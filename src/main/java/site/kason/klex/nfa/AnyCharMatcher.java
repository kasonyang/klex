package site.kason.klex.nfa;

/**
 *
 * @author Kason Yang
 */
public class AnyCharMatcher implements CharMatcher {

  @Override
  public boolean isMatched(int character) {
    return true;
  }

}
