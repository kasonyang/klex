title=Guide : Implement a calculator lexer
date=2017-10-11
type=post
tags=
status=published
~~~~~~

## Define token,token rules and token factory

First,we should define 3 classes:`CTokenRule`,`CToken`,`CTokenFactory`:

CTokenRule:

    public enum CTokenRule implements TokenRule {

      PLUS(0, "+"),
      MIN(1, "-"),
      MUL(2, "*"),
      DIV(3, "/"),
      SPACE(4," "),
      NUM(5, NFAUtil.range('0', '9').concat(NFAUtil.range('0', '9').closure())),
      EOF(6,(NFA)null)
      ;
      private final int pripority;

      private final NFA nfa;

      private CTokenRule(int pripority, NFA nfa) {
        this.pripority = pripority;
        this.nfa = nfa;
      }

      CTokenRule(int pripority, String key) {
        this.pripority = pripority;
        this.nfa = NFAUtil.ofString(key);
      }

      @Override
      public int getPriority() {
        return pripority;
      }

      @Override
      public NFA getNFA() {
        return nfa;
      }

    }

CToken:

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

CTokenFactory:

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

## Define CalculatorLexer

Now,we could define our lexer `CalculatorLexer`:

    public class CalculatorLexer extends Klexer<CToken, CTokenRule> {

      public CalculatorLexer(CharStream charStream) {
        super(charStream, CTokenRule.values(), new CTokenFactory());
      }

    }

## Using CalculatorLexer

    CharStream charStream = new StringCharStream("1+2-3*4/5");
    CalculatorLexer lexer = new CalculatorLexer(charStream);
    List<CToken> tokens = lexer.nextToken();
    //process tokens
