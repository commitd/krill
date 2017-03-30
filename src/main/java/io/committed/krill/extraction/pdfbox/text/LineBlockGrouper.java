package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;

import java.util.Collection;
import java.util.List;

/**
 * Groups {@link Line}s together into logical blocks, eg paragraphs.
 */
@FunctionalInterface
public interface LineBlockGrouper {

  /**
   * Group the lines.
   *
   * @param lines
   *          the lines to group
   * @return a list of {@link TextBlock}s, each containing a logical block.
   */
  List<TextBlock> group(Collection<Line> lines);

}
