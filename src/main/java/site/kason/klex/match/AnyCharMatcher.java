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

  @Override
  public boolean equals(Object obj) {
    return obj instanceof AnyCharMatcher;
  }

  @Override
  public int hashCode() {
    return 7;
  }
  
  

}
