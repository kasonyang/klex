package site.kason.klex.common;

import site.kason.klex.CharStream;
import site.kason.klex.Klexer;

import java.util.Collection;

/**
 * @author KasonYang
 */
public class CommonLexer extends Klexer<CommonToken, CommonTokenRule> {

    public CommonLexer(CharStream charStream, CommonTokenRule eofToken, CommonTokenRule[] rules) {
        super(charStream, rules, new CommonTokenFactory(eofToken));
    }

    public CommonLexer(CharStream charStream, CommonTokenRule eofToken, Collection<CommonTokenRule> rules) {
        super(charStream, rules.toArray(new CommonTokenRule[0]), new CommonTokenFactory(eofToken));
    }

}