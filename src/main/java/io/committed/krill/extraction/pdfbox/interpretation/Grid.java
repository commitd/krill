package io.committed.krill.extraction.pdfbox.interpretation;

import java.util.ArrayList;
import java.util.Collection;

/** A class representing a grid (table). */
public class Grid {

  /** The rows. */
  private Collection<Row> rows = new ArrayList<>();

  /**
   * Gets the rows.
   *
   * @return the rows
   */
  public Collection<Row> getRows() {
    return rows;
  }

  /**
   * Adds a row.
   *
   * @param row the row
   */
  void addRow(Row row) {
    rows.add(row);
  }
}
