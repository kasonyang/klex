package site.kason.klex;

import site.kason.klex.nfa.NFA;

/**
 *
 * @author Kason Yang
 */
public interface TokenRule {

  /**
   * smaller value has higher priority
   * @return 
   */
  public int getPriority();

  public NFA getNFA();

}
