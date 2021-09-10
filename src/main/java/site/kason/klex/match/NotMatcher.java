package site.kason.klex.match;

import java.util.Objects;

/**
 * @author KasonYang
 */
public class NotMatcher implements Matcher {

    private final Matcher matcher;

    public NotMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean isMatched(int character) {
        return !matcher.isMatched(character);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotMatcher that = (NotMatcher) o;
        return Objects.equals(matcher, that.matcher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matcher);
    }
}
