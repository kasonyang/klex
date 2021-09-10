package site.kason.klex.match;

import java.util.Collection;
import java.util.Objects;

/**
 * @author KasonYang
 */
public class AndMatcher implements Matcher {

    private final Collection<Matcher> matchers;

    public AndMatcher(Collection<Matcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean isMatched(int character) {
        for (Matcher m : matchers) {
            if (!m.isMatched(character)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndMatcher that = (AndMatcher) o;
        return Objects.equals(matchers, that.matchers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchers);
    }
}
