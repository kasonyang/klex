package site.kason.klex.common;

import site.kason.klex.TokenRule;
import site.kason.klex.nfa.NFA;
import site.kason.klex.util.NFAUtil;

/**
 * @author KasonYang
 */
public class CommonTokenRule implements TokenRule {

    private final int priority;

    private final NFA nfa;

    public CommonTokenRule(int priority, NFA nfa) {
        this.priority = priority;
        this.nfa = nfa;
    }

    public CommonTokenRule(int priority, String pattern) {
        this.priority = priority;
        this.nfa = pattern == null || pattern.isEmpty() ? null : NFAUtil.ofPattern(pattern);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public NFA getNFA() {
        return nfa;
    }
}
