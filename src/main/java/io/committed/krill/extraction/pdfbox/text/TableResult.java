package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Line;

import java.util.Collection;
import java.util.List;

/**
 * The result from a {@link TableExtractor}, containing found {@link TableBlock}s and any
 * {@link Line}s not consumed in the extraction.
 */
public class TableResult {

  /** The remaining lines. */
  private final List<Line> remainingLines;

  /** The table blocks. */
  private final Collection<TableBlock> tableBlocks;

  /**
   * Creates a new TableResult.
   *
   * @param remainingLines
   *          the lines not consumed.
   * @param tableBlocks
   *          found table blocks.
   */
  public TableResult(List<Line> remainingLines, Collection<TableBlock> tableBlocks) {
    this.remainingLines = remainingLines;
    this.tableBlocks = tableBlocks;
  }

  /**
   * Returns the lines that were not consumed.
   *
   * @return the {@link List} of {@link Line}s.
   */
  public List<Line> getRemainingLines() {
    return remainingLines;
  }

  /**
   * Returns the found {@link TableBlock}s.
   *
   * @return the {@link Collection} of {@link TableBlock}s.
   */
  public Collection<TableBlock> getTableBlocks() {
    return tableBlocks;
  }

}
