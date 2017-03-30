package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;

import java.util.List;

/**
 * Represents a row of cells ({@link TextBlock}s) in a table.
 */
public class TableRow extends PositionedContainer<TextBlock> {

  /**
   * Create a new TableRow for the given cells.
   *
   * @param cells
   *          the cells for this row.
   */
  public TableRow(List<TextBlock> cells) {
    super(cells);
  }

}
