package io.committed.krill.extraction.pdfbox.physical;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Container of words, represents a line of text and its position in the page.
 */
public class Line extends PositionedStyledContainer<Word> implements Baselined {

  /**
   * Constructs a new {@link Line} from an ordered list of {@link Word}s.
   *
   * @param content
   *          the list of words.
   */
  public Line(List<Word> content) {
    super(content);
  }

  @Override
  public float getBaseline() {
    List<Word> contents = getContents();
    if (contents.iterator().hasNext()) {
      // assume consistent baseline
      return contents.iterator().next().getBaseline();
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return getContents().stream().map(Word::toString).collect(Collectors.joining(" "));
  }

}
