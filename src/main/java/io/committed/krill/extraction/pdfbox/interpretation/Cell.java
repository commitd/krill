package io.committed.krill.extraction.pdfbox.interpretation;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/** A class representing a table cell. */
public class Cell {

  /** The bounds. */
  private Rectangle2D bounds;

  /** The col span. */
  private int colSpan;

  /** The row span. */
  private int rowSpan;

  /**
   * Instantiates a new cell.
   *
   * @param topLeft the top left
   * @param bottomRight the bottom right
   * @param colSpan the col span
   * @param rowSpan the row span
   */
  public Cell(Point2D topLeft, Point2D bottomRight, int colSpan, int rowSpan) {
    this.colSpan = colSpan;
    this.rowSpan = rowSpan;
    this.bounds =
        new Rectangle2D.Double(
            topLeft.getX(),
            topLeft.getY(),
            bottomRight.getX() - topLeft.getX(),
            bottomRight.getY() - topLeft.getY());
  }

  /**
   * Gets the bounds.
   *
   * @return the bounds
   */
  public Rectangle2D getBounds() {
    return bounds;
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
