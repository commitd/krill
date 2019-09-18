package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.tika.pdf.PdfParserConfig;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A TableExtractor that uses the {@link GridTableExtractor} followed by the {@link
 * SimpleTableExtractor}.
 */
public class CombinedTableExtractor implements TableExtractor {

  private final PdfParserConfig parserConfig;

  public CombinedTableExtractor(PdfParserConfig parserConfig) {
    this.parserConfig = parserConfig;
  }

  @Override
  public TableResult findTables(List<Line> lineCandidates, Collection<Line2D> lines) {
    List<Line> remainingLines = lineCandidates;
    Collection<TableBlock> tables = new ArrayList<>();
    if (!parserConfig.isDisableGridTableExtraction()) {
      GridTableExtractor extractor = new GridTableExtractor();
      TableResult gridTables = extractor.findTables(remainingLines, lines);
      tables.addAll(gridTables.getTableBlocks());
      remainingLines = gridTables.getRemainingLines();
    }
    if (!parserConfig.isDisableSimpleTableExtraction()) {
      TableExtractor extractor = new SimpleTableExtractor(parserConfig.isIgnoreTerseTables());
      TableResult simpleTables = extractor.findTables(remainingLines, lines);
      tables.addAll(simpleTables.getTableBlocks());
      remainingLines = simpleTables.getRemainingLines();
    }
    return new TableResult(remainingLines, tables);
  }
}
