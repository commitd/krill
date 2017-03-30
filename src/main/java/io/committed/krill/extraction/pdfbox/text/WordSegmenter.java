package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.physical.Word;

import java.util.Optional;

/**
 * {@link WordSegmenter}s take a container of {@link Text} with the same baseline and segment it
 * into a {@link Line} of {@link Word}s.
 */
@FunctionalInterface
public interface WordSegmenter {

  /**
   * Segment the given {@link Text}s into {@link Line}s.
   *
   * @param baselinedText
   *          the {@link Text} with a common baseline.
   * @return an {@link Optional} {@link Line} extracted from the given {@link Text}s.
   */
  Optional<Line> segmentWords(PositionedContainer<Text> baselinedText);

}
