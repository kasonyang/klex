package test.site.kason.klex.nfa;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.klex.CharStream;
import site.kason.klex.StringCharStream;
import site.kason.klex.nfa.NFA;
import site.kason.klex.util.NFAUtil;
import site.kason.klex.nfa.NFAState;
import site.kason.klex.util.NFAMatchResult;
import site.kason.klex.util.NFAMatchUtil;

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
    NFA nfa = NFAUtil.oneOfString("hello");
    NFAState[] acceptedStates = nfa.getAcceptedStates();
    assertEquals(acceptedStates.length, 1);
    NFAMatchResult matchedState = NFAMatchUtil.match(nfa,is);
    assertNotNull(matchedState);
    //assertTrue(Arrays.asList(acceptedStates).contains(matchedState.getMatchedState()));
  }

  @Test
  public void testRangeMatch() {
    NFA nfa = NFAUtil.range('a', 'z');
    assertMatch(nfa, "a");
    assertMatch(nfa, "g");
    assertMatch(nfa, "m");
    assertMatch(nfa, "z");
    assertNotMatch(nfa, "A");
  }

  @Test
  public void testOr() {
    StringCharStream is1 = new StringCharStream("hello");
    StringCharStream is2 = new StringCharStream("hi");
    StringCharStream isConcat = new StringCharStream("hellohi");
    StringCharStream is3 = new StringCharStream("not match");
    NFA nfa = NFAUtil.oneOfString("hello");
    NFA nfa2 = NFAUtil.oneOfString("hi");
    nfa.or(nfa2);
    NFAMatchResult matched1 = NFAMatchUtil.match(nfa,is1);
    NFAMatchResult matched2 = NFAMatchUtil.match(nfa,is2);
    NFAMatchResult matched3 = NFAMatchUtil.match(nfa,is3);
    assertNotNull(matched1);
    assertNotNull(matched2);
    assertNull(matched3);
    nfa.concat(nfa2);
    assertNotNull(NFAMatchUtil.match(nfa,isConcat));

    nfa.closure();
    assertEquals(null, NFAMatchUtil.match(nfa,new StringCharStream("")));
    assertEquals(7, NFAMatchUtil.match(nfa,new StringCharStream("hellohi")).getMatchedLength());
    assertEquals(14, NFAMatchUtil.match(nfa,new StringCharStream("hellohihellohi")).getMatchedLength());
    assertEquals(21, NFAMatchUtil.match(nfa,new StringCharStream("hellohihellohihellohi")).getMatchedLength());
  }
  
  @Test
  public void testExclude(){
    NFA nfa = NFAUtil.exclude(',');
    assertMatch(nfa, "a");
    assertNotMatch(nfa, ",");
  }
  
  @Test
  public void testAny(){
    Random random = new Random();
    NFA nfa = NFAUtil.anyChar();
    for(int i=0;i<50;i++){
      int value = random.nextInt();
      NFAMatchResult result = NFAMatchUtil.match(nfa, new StringCharStream(new int[]{value}));
      assertNotNull(result);
    }
  }
  
  @Test
  public void testOneOfInt(){
    NFA nfa = NFAUtil.oneOf('a','b');
    assertMatch(nfa, "a");
    assertMatch(nfa, "b");
    assertNotMatch(nfa, "c");
    assertNotMatch(nfa, "d");
  }
  
  @Test
  public void testOneOfString(){
    NFA nfa = NFAUtil.oneOfString("if","for");
    assertMatch(nfa, "if");
    assertMatch(nfa, "for");
    assertNotMatch(nfa, "while");
    assertNotMatch(nfa, "else");
  }
  
  @Test
  public void testCopy(){
    NFA nfa = NFAUtil.copy(NFAUtil.oneOfString("if","for"));
    assertMatch(nfa, "if");
    assertMatch(nfa, "for");
    assertNotMatch(nfa, "while");
    assertNotMatch(nfa, "else");
  }
  
  private void assertMatch(NFA nfa,String content){
    NFAMatchResult result = NFAMatchUtil.match(nfa, new StringCharStream(content));
    assertNotNull(result);
  }
  
  private void assertNotMatch(NFA nfa,String content){
    NFAMatchResult result = NFAMatchUtil.match(nfa, new StringCharStream(content));
    assertNull(result);
  }

}
