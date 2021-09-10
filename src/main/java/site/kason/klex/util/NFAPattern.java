package site.kason.klex.util;

import site.kason.klex.match.*;
import site.kason.klex.nfa.NFA;
import site.kason.klex.nfa.NFAState;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author KasonYang
 */
public class NFAPattern {


    private final NFA result;
    private final String pattern;
    private int parseOffset;

    private static final int EOF = 0;

    private static final int[] WHITE_SPACE_CHARS = new int[] {' ', '\f', '\n', '\r', '\t'};

    public NFAPattern(String pattern) {
        this.pattern = pattern;
        this.parseOffset = 0;
        this.result = expr();
        if (!isEOF()) {
            throw new PatternParseException("unexpected character at " + parseOffset);
        }
    }

    public NFA getNFA() {
        return result;
    }

    private NFA expr() {
        NFA e1 = sequence();
        if (e1 == null) {
            return null;
        }
        while (loadAhead(1) == '|') {
            consume();
            NFA e2 = sequence();
            if (e2 == null) {
                throw unexpectedChar();
            }
            e1.or(e2);
        }
        return e1;
    }

    private NFA sequence() {
        NFA e1 = atom();
        if (e1 == null) {
            return null;
        }
        NFA e2 = atom();
        while (e2 != null) {
            e1.concat(e2);
            e2 = atom();
        }
        return e1;
    }

    private NFA atom() {
        char la = loadAhead(1);
        NFA value;
        if (la == EOF) {
            return null;
        } else if (la == '(') {
            value = parenthesisExpr();
        } else if (la == '[') {
            value = charSetExpr();
        } else if (la == ')' || la == '|') {
            return null;
        } else if (la == '\\') {
            value = escapeExpr();
        } else if (la == '.') {
            consume();
            value = NFAUtil.anyChar();
        } else {
            consume();
            value = NFAUtil.oneOf(la);
        }
        la = loadAhead(1);
        if (la == '*') {
            consume();
            value.closure();
        } else if (la == '+') {
            consume();
            value.concat(NFAUtil.copy(value).closure());
        } else if (la == '?') {
            consume();
            value.optional();
        }
        return value;
    }

    private NFA charSetExpr() {
        expect('[');
        List<Integer> charSets = new LinkedList<>();
        List<Matcher> matchers = new LinkedList<>();
        boolean isNotExpr = false;
        if (loadAhead(1) == '^') {
            consume();
            isNotExpr = true;
        }
        while (!isEOF() && loadAhead(1) != ']') {
            char ch = consume();
            if (ch == ']') {
                break;
            }
            char nextCh = loadAhead(1);
            if (nextCh == '-') {
                consume();
                char toCh = consume();
                if (toCh < ch) {
                    throw new PatternParseException("bad range " + ch + "-" + toCh);
                }
                matchers.add(new RangeCharMatcher(ch, toCh));
            } else {
                charSets.add((int) ch);
            }
        }
        expect(']');
        if (!charSets.isEmpty()) {
            matchers.add(new CharSetMatcher(charSets));
        }
        if (matchers.isEmpty()) {
            throw new PatternParseException("empty char set at " + (parseOffset - 1));
        }
        OrMatcher orMatcher = new OrMatcher(matchers);
        Matcher matcher = isNotExpr ? new NotMatcher(orMatcher) : orMatcher;
        NFAState startState = new NFAState();
        NFAState acceptedState = new NFAState();
        startState.pushNextState(matcher, acceptedState);
        return new NFA(startState, Arrays.asList(acceptedState));
    }

    private NFA parenthesisExpr() {
        expect('(');
        NFA value = expr();
        expect(')');
        return value;
    }

    private NFA escapeExpr() {
        expect('\\');
        char la = consume();
        switch (la) {
            case '\\':
                return NFAUtil.oneOf('\\');
            case 'd':
                return NFAUtil.range('0', '9');
            case 'D':
                return NFAUtil.exclude('0', '9');
            case 'w':
                return NFAUtil.range('a', 'z', 'A', 'Z');
            case 'W':
                return NFAUtil.excludeRange('A', 'Z', 'a','z');
            case 's':
                return NFAUtil.oneOf(WHITE_SPACE_CHARS);
            case 'S':
                return NFAUtil.exclude(WHITE_SPACE_CHARS);
            case 'n':
                return NFAUtil.oneOf('\n');
            case 'r':
                return NFAUtil.oneOf('\r');
            case 'f':
                return NFAUtil.oneOf('\f');
            case 't':
                return NFAUtil.oneOf('\t');
            case '?':
            case '{':
            case '}':
            case '[':
            case ']':
            case '(':
            case ')':
            case '*':
            case '+':
            case '.':
            case '|':
            case '^':
                return NFAUtil.oneOf(la);
            default:
                throw unexpectedChar(la);

        }
    }

    private void expect(char expected) {
        char actual = consume();
        if (expected != actual) {
            throw new PatternParseException("unexpected character '" + actual + "',expected '" + expected + "'");
        }
    }

    private boolean isEOF() {
        return parseOffset >= pattern.length();
    }

    private char loadAhead(int count) {
        int offset = parseOffset + count - 1;
        if (offset >= pattern.length()) {
            return EOF;
        }
        return pattern.charAt(offset);
    }

    private char consume() {
        return pattern.charAt(parseOffset++);
    }

    private PatternParseException unexpectedChar() {
        if (isEOF()) {
            return unexpectedChar(EOF);
        }
        return unexpectedChar(pattern.charAt(parseOffset));
    }

    private PatternParseException unexpectedChar(int actual) {
        String s = actual == EOF ? "EOF" : String.valueOf(actual);
        return new PatternParseException("unexpected character '" + s + "'");
    }

    static class PatternParseException extends RuntimeException {
        public PatternParseException(String message) {
            super(message);
        }
    }


}
