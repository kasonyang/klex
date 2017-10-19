package test.site.kason.klex.calculator;

import site.kason.klex.TokenRule;
import site.kason.klex.nfa.NFA;
import site.kason.klex.util.NFAUtil;

/**
 *
 * @author Kason Yang
 */
public enum CTokenRule implements TokenRule {

  PLUS(0, "+"),
  MIN(1, "-"),
  MUL(2, "*"),
  DIV(3, "/"),
  SPACE(4," "),
  NUM(5, NFAUtil.range('0', '9').concat(NFAUtil.range('0', '9').closure())),
  EOF(6,(NFA)null)
  ;
  private final int pripority;

  private final NFA nfa;

  private CTokenRule(int pripority, NFA nfa) {
    this.pripority = pripority;
    this.nfa = nfa;
  }

  CTokenRule(int pripority, String key) {
    this.pripority = pripority;
    this.nfa = NFAUtil.ofString(key);
  }

  @Override
  public int getPriority() {
    return pripority;
  }

  @Override
  public NFA getNFA() {
    return nfa;
  }

}
