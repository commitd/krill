package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.interpretation.Cell;
import io.committed.krill.extraction.pdfbox.interpretation.Grid;
import io.committed.krill.extraction.pdfbox.interpretation.GridExtractor;
import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * A table extractor that attempts to identify cells and rows using grids created from lines drawn
 * on the page.
 */
public class GridTableExtractor implements TableExtractor {

  /** The grid extractor. */
  private GridExtractor gridExtractor = new GridExtractor();

  @Override
  public TableResult findTables(List<Line> lineCandidates, Collection<Line2D> lines) {
    Collection<Grid> grids = gridExtractor.findGrids(lines);
    Collection<TableBlock> blocks = new ArrayList<>();

    grids.forEach(
        grid -> {
          List<TableRow> rows = new ArrayList<>();
          grid.getRows()
              .forEach(
                  row -> {
                    List<TextBlock> cells = new ArrayList<>();
                    row.getCells()
                        .forEach(
                            cell -> {
                              TableCell tableCell = makeTableCell(lineCandidates, cell);
                              cells.add(tableCell);
                            });
                    rows.add(new TableRow(cells));
                  });
          blocks.add(new TableBlock(rows));
        });
    return new TableResult(lineCandidates, blocks);
  }

  /**
   * Make table cell.
   *
   * @param lineCandidates the line candidates
   * @param cell the cell
   * @return the table cell
   */
  private TableCell makeTableCell(List<Line> lineCandidates, Cell cell) {
    List<Line> cellLines = new ArrayList<>();
    ListIterator<Line> listIterator = lineCandidates.listIterator();
    while (listIterator.hasNext()) {
      Line line = listIterator.next();
      if (cell.getBounds().contains(line.getPosition())) {
        cellLines.add(line);
        listIterator.remove();
      }
    }
    Comparator<Line> byX = Comparator.comparing(l -> l.getPosition().getMinX());
    Comparator<Line> byY = Comparator.comparing(l -> l.getPosition().getMinY());
    cellLines.sort(byY.thenComparing(byX));
    return new TableCell(cellLines, cell.getColSpan(), cell.getRowSpan());
  }
}
