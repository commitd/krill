package io.committed.krill.extraction.pdfbox.interpretation;

import java.util.ArrayList;
import java.util.Collection;

/** Represents a row in a Grid (table). */
public class Row {

  /** The cells. */
  private Collection<Cell> cells = new ArrayList<>();

  /**
   * Gets the cells.
   *
   * @return the cells
   */
  public Collection<Cell> getCells() {
    return cells;
  }

  /**
   * Adds the cell.
   *
   * @param cell the cell
   */
  void addCell(Cell cell) {
    cells.add(cell);
  }
}
