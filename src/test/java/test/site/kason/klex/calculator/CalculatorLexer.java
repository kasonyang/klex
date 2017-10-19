package test.site.kason.klex.calculator;

import site.kason.klex.CharStream;
import site.kason.klex.Klexer;

/**
 *
 * @author Kason Yang
 */
public class CalculatorLexer extends Klexer<CToken, CTokenRule> {

  public CalculatorLexer(CharStream charStream) {
    super(charStream, CTokenRule.values(), new CTokenFactory());
  }

}
