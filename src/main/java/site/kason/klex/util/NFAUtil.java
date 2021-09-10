package site.kason.klex.util;

import site.kason.klex.match.*;

import java.util.*;

import site.kason.klex.nfa.NFA;
import site.kason.klex.nfa.NFAState;

/**
 *
 * @author Kason Yang
 */
public class NFAUtil {

  public static NFA range(int... chars) {
    return rangeNFA(false, chars);
  }

  public static NFA excludeRange(int... chars) {
    return rangeNFA(true, chars);
  }

  public static NFA oneOfString(String... str) {
    NFA nfa = null;
    for (int i = 0; i < str.length; i++) {
      NFA theNfa = ofString(str[i]);
      nfa = (nfa == null) ? theNfa : nfa.or(theNfa);
    }
    return nfa;
  }

  public static NFA ofString(String str) {
    NFAState startState = new NFAState();
    NFAState currentState = startState;
    int strLen = str.length();
    int offset = 0;
    while (offset < strLen) {
      char nextInput = str.charAt(offset++);
      NFAState nextState = new NFAState();
      currentState.pushNextState(nextInput, nextState);
      currentState = nextState;
    }
    return new NFA(startState, Arrays.asList(currentState));
  }

  public static NFA exclude(int... excludes) {
    NFAState startState = new NFAState();
    NFAState acceptedState = new NFAState();
    startState.pushNextState(new ExcludeCharMatcher(excludes), acceptedState);
    return new NFA(startState, Arrays.asList(acceptedState));
  }
  
  public static NFA oneOf(int... chars) {
    NFAState startState = new NFAState();
    NFAState acceptedState = new NFAState();
    for (int i = 0; i < chars.length; i++) {
      startState.pushNextState(chars[i], acceptedState);
    }
    return new NFA(startState, Arrays.asList(acceptedState));
  }

  public static NFA ofPattern(String pattern) {
    return new NFAPattern(pattern).getNFA();
  }

  public static NFA anyChar() {
    NFAState startState = new NFAState();
    NFAState acceptedState = new NFAState();
    startState.pushNextState(new AnyCharMatcher(), acceptedState);
    return new NFA(startState, Arrays.asList(acceptedState));
  }
  
  public static NFA copy(NFA nfa){
    Map<NFAState,NFAState> old2new = new HashMap();
    NFAState startState = getOrCopyState(nfa.getStartState(),old2new);
    List<NFAState> acceptedStates = new LinkedList();
    for(NFAState s:nfa.getAcceptedStates()){
      NFAState newACState = old2new.get(s);
      if(newACState!=null){
        acceptedStates.add(newACState);
      }
    }
    return new NFA(startState, acceptedStates);
  }
  
  private static NFAState getOrCopyState(NFAState oldState,Map<NFAState,NFAState> old2new){
    NFAState newState = old2new.get(oldState);
    if(newState==null){
      newState = new NFAState();
      old2new.put(oldState, newState);
      for(NFAState ls:oldState.getLambdaClosureStates()){
        newState.pushLambdaClosureState(getOrCopyState(ls,old2new));
      }
      for(Map.Entry<Matcher, Set<NFAState>> e:oldState.getNextStates().entrySet()){
        for(NFAState s:e.getValue()){
          newState.pushNextState(e.getKey(), getOrCopyState(s,old2new));
        }
      }
    }
    return newState;
  }

  private static NFA rangeNFA(boolean excludeMode, int... chars) {
    if (chars.length % 2 != 0) {
      throw new IllegalArgumentException("invalid arguments count:" + chars.length);
    }
    NFAState startState = new NFAState();
    NFAState acceptedState = new NFAState();
    Matcher[] matchers = new Matcher[chars.length / 2];
    for (int i = 0; i < chars.length; i += 2) {
      matchers[i/2] = new RangeCharMatcher(chars[i], chars[i + 1]);
    }
    Matcher orMatcher = matchers.length == 1 ? matchers[0] : new OrMatcher(matchers);
    Matcher finalMatcher = excludeMode ? new NotMatcher(orMatcher) : orMatcher;
    startState.pushNextState(finalMatcher, acceptedState);
    return new NFA(startState, Collections.singletonList(acceptedState));
  }

}
