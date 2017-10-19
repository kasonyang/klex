package test.site.kason.klex.calculator;

import site.kason.klex.OffsetRange;
import site.kason.klex.TokenFactory;

/**
 *
 * @author Kason Yang
 */
public class CTokenFactory implements TokenFactory<CToken, CTokenRule> {

  @Override
  public CToken createToken(CTokenRule tokenRule, OffsetRange offset, int[] chars) {
    //TODO fix text
    return new CToken(tokenRule, "");
  }

  @Override
  public CToken createEOFToken(OffsetRange offset) {
    return CToken.EOF;
  }

}
