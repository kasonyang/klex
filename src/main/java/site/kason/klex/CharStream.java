package site.kason.klex;

/**
 *
 * @author Kason Yang
 */
public interface CharStream {

  public final static int EOF = -1;

  public int consume();

  public int[] consume(int count);

  public void skip(int count);

  /**
   *
   * @param count char count to look ahead
   * @return return -1 if out the ending of input
   */
  public int lookAhead(int count);

  public int getCurrentLine();

  public int getCurrentColumn();

  public int getCurrentOffset();

}
