package test.site.kason.klex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.klex.Klexer;
import site.kason.klex.OffsetRange;
import site.kason.klex.LexException;
import site.kason.klex.StringCharStream;
import site.kason.klex.TokenFactory;
import site.kason.klex.nfa.NFA;
import site.kason.klex.util.NFAUtil;
import site.kason.klex.TokenRule;

/**
 *
 * @author Kason Yang
 */
public class LexerTest {

  static class TTokenRule implements TokenRule {

    private int priority;
    private NFA nfa;

    public TTokenRule(int priority, NFA nfa) {
      this.priority = priority;
      this.nfa = nfa;
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

  static TokenRule TI(int priority, NFA nfa) {
    return new TTokenRule(priority, nfa);
  }

  final static TokenRule[] TOKENS = new TokenRule[]{
    TI(0, NFAUtil.oneOfString(" ")),
    TI(1, NFAUtil.oneOfString("if")),
    TI(2, NFAUtil.oneOfString("for")),
    TI(3, NFAUtil.oneOfString("do")),
    TI(4, NFAUtil.range('a', 'z')
    .concat(NFAUtil.range('a', 'z').closure())),};

  public LexerTest() {
  }

  private List<Integer> parse(String input) throws LexException {
    Klexer<Integer, TTokenRule> lexer = new Klexer(new StringCharStream(input), TOKENS, new TokenFactory<Integer, TTokenRule>() {
      @Override
      public Integer createToken(TTokenRule tokenInfo, OffsetRange offset, int[] chars) {
        return tokenInfo.priority;
      }

      @Override
      public Integer createEOFToken(OffsetRange offset) {
        return -1;
      }
    });
    List<Integer> tokens = lexer.nextTokens();
    List<Integer> types = new ArrayList(tokens.size());
    for (Integer t : tokens) {
      types.add(t);
    }
    return types;
  }

  @Test
  public void test() throws LexException {

    assertEquals(Arrays.asList(1, 0, 2, 0, 3, 0, 4), parse("if for do x"));
    assertEquals(Arrays.asList(1, 0, 2, 0, 3, 0, 4), parse("if for do xx"));
    assertEquals(Arrays.asList(1, 0, 2, 0, 3, 0, 4), parse("if for do xxxxxxx"));
    assertEquals(Arrays.asList(1, 0, 4, 0, 3), parse("if xxx do"));
    assertEquals(Arrays.asList(4), parse("iffordo"));
  }

}
