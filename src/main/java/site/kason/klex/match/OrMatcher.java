package site.kason.klex.match;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author KasonYang
 */
public class OrMatcher implements Matcher {

    private final Collection<Matcher> matchers;

    public OrMatcher(Collection<Matcher> matchers) {
        this.matchers = matchers;
    }

    public OrMatcher(Matcher... matchers) {
        this.matchers = Arrays.asList(matchers);
    }

    @Override
    public boolean isMatched(int character) {
        for (Matcher m : matchers) {
            if (m.isMatched(character)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrMatcher orMatcher = (OrMatcher) o;
        return Objects.equals(matchers, orMatcher.matchers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchers);
    }
}
