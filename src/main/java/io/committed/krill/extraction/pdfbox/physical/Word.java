package io.committed.krill.extraction.pdfbox.physical;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Represents a word in a {@link Line} of text. */
public class Word extends PositionedStyledContainer<Text> implements Baselined {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(Word.class);

  /** The baseline. */
  private final float baseline;

  /**
   * Create a new {@link Word} from the given ordered list of {@link Text} content.
   *
   * @param content the {@link Text} content for the word.
   */
  public Word(List<Text> content) {
    super(content);
    this.baseline = deriveBaseline();
  }

  /**
   * Derive baseline.
   *
   * @return the float
   */
  private float deriveBaseline() {
    Iterator<Text> iterator = getContents().iterator();
    float retval = 0;
    if (iterator.hasNext()) {
      retval = iterator.next().getBaseline();
    }
    while (iterator.hasNext()) {
      float newCandidate = iterator.next().getBaseline();
      if (Float.compare(newCandidate, retval) != 0) {
        retval = Math.min(newCandidate, retval);
        LOGGER.warn("Inconsistent baseline in word - using smallest value (this may be incorrect)");
      }
    }
    return retval;
  }

  @Override
  public float getBaseline() {
    return baseline;
  }

  @Override
  public String toString() {
    return getContents().stream().map(Text::toString).collect(Collectors.joining());
  }
}
