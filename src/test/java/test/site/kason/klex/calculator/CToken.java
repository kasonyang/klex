package test.site.kason.klex.calculator;

/**
 *
 * @author Kason Yang
 */
public class CToken {

  private CTokenRule rule;

  private String text;

  public final static CToken EOF = new CToken(CTokenRule.EOF, "");

  public CToken(CTokenRule rule, String text) {
    this.rule = rule;
    this.text = text;
  }

  public CTokenRule getRule() {
    return rule;
  }

  public String getText() {
    return text;
  }

}
