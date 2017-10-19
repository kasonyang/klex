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

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 37 * hash + this.firstAcceptedChar;
    hash = 37 * hash + this.lastAcceptedChar;
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
    final RangeCharMatcher other = (RangeCharMatcher) obj;
    if (this.firstAcceptedChar != other.firstAcceptedChar) {
      return false;
    }
    if (this.lastAcceptedChar != other.lastAcceptedChar) {
      return false;
    }
    return true;
  }

}
