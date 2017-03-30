package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;

import java.util.List;

/**
 * Represents a cell in a table, ready for emitting.
 */
public class TableCell extends TextBlock {

  /** The col span. */
  private int colSpan;

  /** The row span. */
  private int rowSpan;

  /**
   * Instantiates a new table cell.
   *
   * @param content
   *          the content
   */
  public TableCell(List<Line> content) {
    super(content);
  }

  /**
   * Instantiates a new table cell.
   *
   * @param content
   *          the content
   * @param colSpan
   *          the col span
   * @param rowSpan
   *          the row span
   */
  public TableCell(List<Line> content, int colSpan, int rowSpan) {
    this(content);
    this.colSpan = colSpan;
    this.rowSpan = rowSpan;
  }

  /**
   * Gets the col span.
   *
   * @return the col span
   */
  public int getColSpan() {
    return colSpan;
  }

  /**
   * Gets the row span.
   *
   * @return the row span
   */
  public int getRowSpan() {
    return rowSpan;
  }

}
