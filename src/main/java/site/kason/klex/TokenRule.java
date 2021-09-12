package site.kason.klex;

import site.kason.klex.nfa.NFA;

/**
 *
 * @author Kason Yang
 */
public interface TokenRule {

  /**
   * Get the priority of token.Smaller value has higher priority.
   * @return the priority of token
   */
  int getPriority();

  NFA getNFA();

}
