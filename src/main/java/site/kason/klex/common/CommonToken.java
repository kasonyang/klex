package site.kason.klex.common;

/**
 * @author KasonYang
 */
public class CommonToken {

    private final CommonTokenRule rule;

    private final String text;

    public CommonToken(CommonTokenRule rule, String text) {
        this.rule = rule;
        this.text = text;
    }

    public CommonTokenRule getRule() {
        return rule;
    }

    public String getText() {
        return text;
    }

}
