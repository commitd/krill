package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.physical.Word;

import java.util.List;

/**
 * Extracts lines from extracted sequences of {@link Text}s.
 * <p>
 * Each {@link Line} should be semantically complete and not contain text from adjacent columns.
 * </p>
 */
@FunctionalInterface
public interface LineFinder {

  /**
   * Find lines in the given {@link List} of {@link PositionedContainer} of {@link Text}. The
   * {@link Text}s are to passed in the order that they were extracted from the PDF. These will be
   * then passed to a {@link WordSegmenter} to create semantic {@link Line}s of {@link Word}s.
   *
   * @param sequences
   *          the sequences of {@link Text} to process
   * @return a {@link List} of line segments.
   */
  List<PositionedContainer<Text>> findLines(List<PositionedContainer<Text>> sequences);
}
