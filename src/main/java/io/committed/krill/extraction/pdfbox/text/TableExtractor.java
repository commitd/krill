package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Line;

import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.List;

/**
 * Table extractors find {@link TableBlock}s within a collection of candidate {@link Line}s.
 */
@FunctionalInterface
public interface TableExtractor {

  /**
   * Finds the {@link TableBlock} in the given candidate {@link Line}s.
   *
   * @param lineCandidates
   *          the candidate {@link Line}s of text
   * @param lines
   *          all of the lines that have been extracted from the page
   * @return a {@link TableResult} with found {@link TableBlock}s and any {@link Line}s not consumed
   */
  TableResult findTables(List<Line> lineCandidates, Collection<Line2D> lines);

}
