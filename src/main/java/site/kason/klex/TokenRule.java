package site.kason.klex;

import site.kason.klex.nfa.NFA;

/**
 *
 * @author Kason Yang
 */
public interface TokenRule {

  public int getPriority();

  public NFA getNFA();

}
