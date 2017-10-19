package test.site.kason.klex.dfa;

import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.klex.StringCharStream;
import site.kason.klex.dfa.DFA;
import site.kason.klex.util.DFAMatchResult;
import site.kason.klex.util.DFAMatchUtil;
import site.kason.klex.util.DFAUtil;
import site.kason.klex.nfa.NFA;
import site.kason.klex.util.NFAUtil;

/**
 *
 * @author Kason Yang
 */
public class DFAUtilTest {

  public DFAUtilTest() {
  }

  @Test
  public void test() {
    NFA nfa = NFAUtil.oneOfString("if", "for", "while");
    DFA dfa = DFAUtil.buildFromNFA(nfa);
    assertMatch(dfa, "if");
    assertMatch(dfa, "for");
    assertMatch(dfa, "while");
    assertNotMatch(dfa, "hello");
  }

  @Test
  public void testId() {
    NFA nfa = NFAUtil.range('a', 'z').or(NFAUtil.oneOf('_')).concat(
            NFAUtil.range('a', 'z').or(NFAUtil.range('0', '9')).or(NFAUtil.oneOf('_')).closure()
    );
    DFA dfa = DFAUtil.buildFromNFA(nfa);
    assertMatch(dfa, "a");
    assertMatch(dfa, "_");
    assertNotMatch(dfa, "1");

    assertMatch(dfa, "ab");
    assertMatch(dfa, "_a");
    assertMatch(dfa, "a1");
    assertNotMatch(dfa, "0a");

    assertMatch(dfa, "abc");
    assertMatch(dfa, "_ab");
    assertMatch(dfa, "a1_");
    assertNotMatch(dfa, "0ab");

  }

  private void assertMatch(DFA dfa, String content) {
    DFAMatchResult result = DFAMatchUtil.match(dfa,new StringCharStream(content));
    assertNotNull(result);
    assertEquals(content.length(), result.getMatchedLength());
  }

  private void assertNotMatch(DFA dfa, String content) {
    DFAMatchResult result = DFAMatchUtil.match(dfa,new StringCharStream(content));
    assertNull(result);
  }

}
