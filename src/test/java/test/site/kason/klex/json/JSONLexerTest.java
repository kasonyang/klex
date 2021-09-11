package test.site.kason.klex.json;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import site.kason.klex.LexException;
import site.kason.klex.StringCharStream;
import site.kason.klex.common.CommonLexer;
import site.kason.klex.common.CommonToken;
import site.kason.klex.common.CommonTokenRule;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author KasonYang
 */
public class JSONLexerTest {

    private static int priority = 1;
    private static List<CommonTokenRule> TOKEN_RULES = new LinkedList<>();

    @Test
    public void testJsonLexer() throws IOException, LexException {
        CommonTokenRule
                EOF = tk(null),
                WS = tk("\\s+"),
                LBRACKET = tk("{"),
                RBRACKET = tk("}"),
                COLON = tk(":"),
                COMMA = tk(","),
                TRUE = tk("true"),
                FALSE = tk("false"),
                NUMBER = tk("\\d+"),
                STRING = tk("\"([^\"]|\\\\\")+\"");
        String content = readInput();
        CommonLexer lexer = new CommonLexer(new StringCharStream(content), EOF, TOKEN_RULES);
        List<CommonToken> tks = lexer.nextTokens();
        Assert.assertEquals(22, tks.size());
    }

    private static String readInput() throws IOException {
        InputStream is = JSONLexerTest.class.getResourceAsStream("/test.json");
        return String.join("\n", IOUtils.readLines(is, "utf8"));
    }

    private static CommonTokenRule tk(String pattern) {
        CommonTokenRule rule = new CommonTokenRule(priority++, pattern);
        if (pattern != null && !pattern.isEmpty()) {
            TOKEN_RULES.add(rule);
        }
        return rule;
    }

}
