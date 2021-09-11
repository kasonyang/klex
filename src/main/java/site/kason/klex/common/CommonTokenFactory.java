package site.kason.klex.common;

import site.kason.klex.OffsetRange;
import site.kason.klex.TokenFactory;

/**
 * @author KasonYang
 */
public class CommonTokenFactory implements TokenFactory<CommonToken, CommonTokenRule> {

    private final CommonTokenRule eofTokenRule;

    public CommonTokenFactory(CommonTokenRule eofTokenRule) {
        this.eofTokenRule = eofTokenRule;
    }


    @Override
    public CommonToken createToken(CommonTokenRule tokenRule, OffsetRange offset, int[] chars) {
        //TODO fix text
        return new CommonToken(tokenRule, "");
    }

    @Override
    public CommonToken createEOFToken(OffsetRange offset) {
        return new CommonToken(eofTokenRule, "");
    }

}