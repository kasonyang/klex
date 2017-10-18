package site.kason.klex;

/**
 *
 * @author Kason Yang
 */
public interface TokenFactory<TOKEN, TOKEN_RULE> {

  TOKEN createToken(TOKEN_RULE tokenRule, OffsetRange offset, int[] chars);

  TOKEN createEOFToken(OffsetRange offset);

}
