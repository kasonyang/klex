package site.kason.klex.match;

import java.util.Arrays;

/**
 *
 * @author Kason Yang
 */
public class ExcludeCharMatcher implements Matcher {

  private final int[] excludeChars;

  public ExcludeCharMatcher(int[] excludeChars) {
    this.excludeChars = excludeChars;
  }

  @Override
  public boolean isMatched(int character) {
    for (int c : this.excludeChars) {
      if (character == c) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 67 * hash + Arrays.hashCode(this.excludeChars);
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
    final ExcludeCharMatcher other = (ExcludeCharMatcher) obj;
    if (!Arrays.equals(this.excludeChars, other.excludeChars)) {
      return false;
    }
    return true;
  }

}
