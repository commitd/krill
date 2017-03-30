package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Line;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A TableExtractor that uses the {@link GridTableExtractor} followed by the
 * {@link SimpleTableExtractor}.
 */
public class CombinedTableExtractor implements TableExtractor {

  /** The grid table extractor. */
  private final TableExtractor gridTableExtractor = new GridTableExtractor();

  /** The simple table extractor. */
  private final TableExtractor simpleTableExtractor = new SimpleTableExtractor();

  @Override
  public TableResult findTables(List<Line> lineCandidates, Collection<Line2D> lines) {
    TableResult gridTables = gridTableExtractor.findTables(lineCandidates, lines);
    TableResult simpleTables = simpleTableExtractor.findTables(gridTables.getRemainingLines(),
        lines);
    Collection<TableBlock> tables = new ArrayList<>(gridTables.getTableBlocks());
    tables.addAll(simpleTables.getTableBlocks());
    return new TableResult(simpleTables.getRemainingLines(), tables);
  }
}
