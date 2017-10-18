package test.site.kason.klex.nfa;

import java.util.Arrays;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.klex.CharStream;
import site.kason.klex.StringCharStream;
import site.kason.klex.nfa.NFA;
import site.kason.klex.nfa.NFAUtil;
import site.kason.klex.nfa.NFAState;
import site.kason.klex.nfa.NFAMatchResult;

/**
 *
 * @author Kason Yang
 */
public class NFAUtilTest {

  public NFAUtilTest() {
  }

  @Test
  public void testNFA() {
    CharStream is = new StringCharStream("hello");
    NFA nfa = NFAUtil.oneOf("hello");
    NFAState[] acceptedStates = nfa.getAcceptedStates();
    assertEquals(acceptedStates.length, 1);
    NFAMatchResult matchedState = nfa.match(is);
    assertNotNull(matchedState);
    //assertTrue(Arrays.asList(acceptedStates).contains(matchedState.getMatchedState()));
  }

  @Test
  public void testRangeMatch() {
    NFA nfa = NFAUtil.range('a', 'z');
    assertNotNull(nfa.match(new StringCharStream("a")));
    assertNotNull(nfa.match(new StringCharStream("g")));
    assertNotNull(nfa.match(new StringCharStream("m")));
    assertNotNull(nfa.match(new StringCharStream("z")));
    assertNull(nfa.match(new StringCharStream("A")));
  }

  @Test
  public void testOr() {
    StringCharStream is1 = new StringCharStream("hello");
    StringCharStream is2 = new StringCharStream("hi");
    StringCharStream isConcat = new StringCharStream("hellohi");
    StringCharStream is3 = new StringCharStream("not match");
    NFA nfa = NFAUtil.oneOf("hello");
    NFA nfa2 = NFAUtil.oneOf("hi");
    nfa.or(nfa2);
    NFAMatchResult matched1 = nfa.match(is1);
    NFAMatchResult matched2 = nfa.match(is2);
    NFAMatchResult matched3 = nfa.match(is3);
    assertNotNull(matched1);
    assertNotNull(matched2);
    assertNull(matched3);
    nfa.concat(nfa2);
    assertNotNull(nfa.match(isConcat));

    nfa.closure();
    assertEquals(null, nfa.match(new StringCharStream("")));
    assertEquals(7, nfa.match(new StringCharStream("hellohi")).getMatchedLength());
    assertEquals(14, nfa.match(new StringCharStream("hellohihellohi")).getMatchedLength());
    assertEquals(21, nfa.match(new StringCharStream("hellohihellohihellohi")).getMatchedLength());
  }
  
  @Test
  public void testExclude(){
    NFA nfa = NFAUtil.exclude(',');
    assertNotNull(nfa.match(new StringCharStream("a")));
    assertNull(nfa.match(new StringCharStream(",")));
  }
  
  @Test
  public void testAny(){
    Random random = new Random();
    NFA nfa = NFAUtil.anyChar();
    for(int i=0;i<50;i++){
      assertNotNull(nfa.match(new StringCharStream(new int[]{random.nextInt()})));
    }
  }
  
  @Test
  public void testOneOfInt(){
    NFA nfa = NFAUtil.oneOf('a','b');
    assertNotNull(nfa.match(new StringCharStream("a")));
    assertNotNull(nfa.match(new StringCharStream("b")));
    assertNull(nfa.match(new StringCharStream("c")));
    assertNull(nfa.match(new StringCharStream("d")));
  }
  
    @Test
  public void testOneOfString(){
    NFA nfa = NFAUtil.oneOf("if","for");
    assertNotNull(nfa.match(new StringCharStream("if")));
    assertNotNull(nfa.match(new StringCharStream("for")));
    assertNull(nfa.match(new StringCharStream("while")));
    assertNull(nfa.match(new StringCharStream("else")));
  }

}
