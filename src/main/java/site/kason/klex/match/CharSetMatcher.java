package site.kason.klex.match;

import java.util.*;

/**
 *
 * @author Kason Yang
 */
public class CharSetMatcher implements Matcher {

  private final Set<Integer> charSet;

  public CharSetMatcher(List<Integer> characters) {
    charSet = new HashSet<>(characters);
  }

  public CharSetMatcher(int... characters) {
    charSet = new HashSet<>();
    for (int c : characters) {
      charSet.add(c);
    }
  }

  @Override
  public boolean isMatched(int character) {
    return charSet.contains(character);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CharSetMatcher that = (CharSetMatcher) o;
    return Objects.equals(charSet, that.charSet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(charSet);
  }
}
