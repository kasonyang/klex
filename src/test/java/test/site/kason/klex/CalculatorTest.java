package test.site.kason.klex;

import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.klex.CharStream;
import site.kason.klex.LexException;
import site.kason.klex.StringCharStream;
import test.site.kason.klex.calculator.CToken;
import test.site.kason.klex.calculator.CTokenRule;
import static test.site.kason.klex.calculator.CTokenRule.*;
import test.site.kason.klex.calculator.CalculatorLexer;

/**
 *
 * @author Kason Yang
 */
public class CalculatorTest {
  
  public CalculatorTest() {
  }
  
  @Test
  public void test() throws LexException{
    CharStream charStream = new StringCharStream("1+2-3*4/5");
    CalculatorLexer lexer = new CalculatorLexer(charStream);
    List<CToken> tokens = lexer.nextTokens();
    Stream<CTokenRule> rules = tokens.stream().map((t) -> {
      return t.getRule();
    });
    assertArrayEquals(new CTokenRule[]{NUM,PLUS,NUM,MIN,NUM,MUL,NUM,DIV,NUM}, rules.toArray());
  }
  
}
