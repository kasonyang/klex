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

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 89 * hash + this.character;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CharMatcher other = (CharMatcher) obj;
    if (this.character != other.character) {
      return false;
    }
    return true;
  }

}
