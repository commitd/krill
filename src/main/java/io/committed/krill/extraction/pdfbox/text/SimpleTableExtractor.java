package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * A simple heuristic {@link TableExtractor}.
 * <p>
 * First potential tables are found by looking for lines that contain more than one line fragment on
 * the same baseline.
 * </p>
 * <p>
 * These line fragments are then assigned columns, and grids of at least 2x2 are put into
 * {@link TableBlock}s.
 * </p>
 */
public class SimpleTableExtractor implements TableExtractor {

  /**
   * Temporary configuration variable to ignore overly terse tables (those with very dense amounts
   * of text) set by system property class.getName().ignoreTerseTables. This is not a generic
   * solution, but does prevent some multi-column documents being identified as tables.
   */
  private static final boolean IGNORE_TERSE_TABLES = !Boolean
      .getBoolean(SimpleTableExtractor.class.getName() + ".ignoreTerseTables");

  /**
   * Temporary configuration variable to disable table extraction completely (this means that lines
   * will be grouped into text blocks rather than tables). set by system property
   * class.getName().disableTableExtraction
   */
  private static final boolean DISABLE_TABLE_EXTRACTION = Boolean
      .getBoolean(SimpleTableExtractor.class.getName() + ".disableTableExtraction");

  @Override
  public TableResult findTables(List<Line> lineCandidates, Collection<Line2D> lines) {
    if (DISABLE_TABLE_EXTRACTION) {
      return new TableResult(lineCandidates, Collections.emptyList());
    }
    List<LinesWithCommonBaseline> linesByBaseLine = groupByBaseline(lineCandidates);
    TableCandidateResult tableCandidateResult = gatherTableCandidates(linesByBaseLine);

    List<Line> discardedLines = new ArrayList<>();
    discardedLines.addAll(tableCandidateResult.discardedLines);

    Collection<TableBlock> tables = new ArrayList<>();
    for (TableCandidate candidate : tableCandidateResult.tableCandidates) {
      TableBlock tableBlock = extractTable(candidate);
      if (tableBlock == null) {
        candidate.lines.forEach(line -> discardedLines.addAll(line.lines));
      } else {
        tables.add(tableBlock);
      }
    }
    return new TableResult(discardedLines, tables);
  }

  /**
   * Extract table.
   *
   * @param candidate
   *          the candidate
   * @return the table block
   */
  private TableBlock extractTable(TableCandidate candidate) {
    if (candidate.lines.size() < 2) {
      return null;
    }

    List<Column> columns = new ArrayList<>();
    int lineIndex = 0;
    for (LinesWithCommonBaseline linesWithCommonBaseline : candidate.lines) {
      for (Line line : linesWithCommonBaseline.lines) {
        Column assigned = findColumn(columns, line);
        if (assigned == null) {
          assigned = new Column();
          columns.add(assigned);
        }
        assigned.lines.put(lineIndex, line);
      }
      lineIndex++;
    }

    return makeTableBlock(columns, candidate.lines.size());
  }

  /**
   * Make table block.
   *
   * @param columns
   *          the columns
   * @param lineCount
   *          the line count
   * @return the table block
   */
  private TableBlock makeTableBlock(List<Column> columns, int lineCount) {
    Line[][] grid = new Line[lineCount][columns.size()];

    for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
      for (Entry<Integer, Line> entry : columns.get(columnIndex).lines.entrySet()) {
        Line value = entry.getValue();
        if (value == null) {
          value = new Line(Collections.emptyList());
        }
        grid[entry.getKey()][columnIndex] = value;
      }
    }

    List<TableRow> rows = new ArrayList<>();
    for (int i = 0; i < lineCount; i++) {
      Line[] cellLines = grid[i];
      List<TextBlock> cells = new ArrayList<>();
      for (Line line : cellLines) {
        cells.add(new TextBlock(
            line == null ? Collections.emptyList() : Collections.singletonList(line)));
      }
      rows.add(new TableRow(cells));
    }

    if (IGNORE_TERSE_TABLES && isTooTerse(rows)) {
      return null;
    }

    return new TableBlock(rows);
  }

  /**
   * Checks if is too terse.
   *
   * @param rows
   *          the rows
   * @return true, if is too terse
   */
  private boolean isTooTerse(List<TableRow> rows) {
    int terseRows = 0;
    for (TableRow tableRow : rows) {
      List<TextBlock> contents = tableRow.getContents();
      int terseCells = 0;
      for (TextBlock textBlock : contents) {
        if (textBlock.getContents().stream().filter(Objects::nonNull)
            .flatMap(f -> f.getContents().stream()).count() > 1) {
          terseCells++;
        }
      }
      if (terseCells == contents.size()) {
        terseRows++;
      }
    }
    return terseRows >= rows.size() * 0.9f;
  }

  /**
   * Find column.
   *
   * @param columns
   *          the columns
   * @param line
   *          the line
   * @return the column
   */
  private Column findColumn(List<Column> columns, Line line) {
    for (Column column : columns) {
      if (overlaps(column, line)) {
        return column;
      }
    }
    return null;
  }

  /**
   * Overlaps.
   *
   * @param column
   *          the column
   * @param line
   *          the line
   * @return true, if successful
   */
  private boolean overlaps(Column column, Line line) {
    double lineMinX = line.getPosition().getMinX();
    double lineMaxX = line.getPosition().getMaxX();
    double columnMinX = column.getMinX();
    double columnMaxX = column.getMaxX();
    return (columnMinX < lineMaxX) && ((lineMinX - columnMaxX) < 3f);
  }

  /**
   * Gather table candidates.
   *
   * @param linesByBaseLine
   *          the lines by base line
   * @return the table candidate result
   */
  private TableCandidateResult gatherTableCandidates(
      List<LinesWithCommonBaseline> linesByBaseLine) {
    TableCandidateResult result = new TableCandidateResult();
    TableCandidate tableCandidate = null;

    for (LinesWithCommonBaseline line : linesByBaseLine) {
      if (line.lines.size() < 2) {
        if (tableCandidate != null) {
          result.tableCandidates.add(tableCandidate);
          tableCandidate = null;
        }
        result.discardedLines.addAll(line.lines);
      } else {
        if (tableCandidate == null) {
          tableCandidate = new TableCandidate();
        }
        tableCandidate.lines.add(line);
      }
    }
    if (tableCandidate != null) {
      result.tableCandidates.add(tableCandidate);
    }
    return result;
  }

  /**
   * Group by baseline.
   *
   * @param lineCandidates
   *          the line candidates
   * @return the list
   */
  private List<LinesWithCommonBaseline> groupByBaseline(List<Line> lineCandidates) {
    List<LinesWithCommonBaseline> linesByBaseLine = new ArrayList<>();
    LinesWithCommonBaseline currentLine = new LinesWithCommonBaseline();
    Iterator<Line> iterator = lineCandidates.iterator();
    if (iterator.hasNext()) {
      Line previous = iterator.next();
      currentLine.lines.add(previous);
      while (iterator.hasNext()) {
        Line current = iterator.next();
        if (Float.compare(current.getBaseline(), previous.getBaseline()) != 0) {
          linesByBaseLine.add(currentLine);
          currentLine = new LinesWithCommonBaseline();
        }
        currentLine.lines.add(current);
        previous = current;
      }
      if (!currentLine.lines.isEmpty()) {
        linesByBaseLine.add(currentLine);
      }
    }
    return linesByBaseLine;
  }

  /**
   * The Class Column.
   */
  private static class Column {

    /** The lines. */
    private final Map<Integer, Line> lines = new HashMap<>();

    /**
     * Gets the min X.
     *
     * @return the min X
     */
    private double getMinX() {
      return lines.values().stream().mapToDouble(l -> l.getPosition().getMinX()).min()
          .getAsDouble();
    }

    /**
     * Gets the max X.
     *
     * @return the max X
     */
    private double getMaxX() {
      return lines.values().stream().mapToDouble(l -> l.getPosition().getMaxX()).max()
          .getAsDouble();
    }
  }

  /**
   * The Class LinesWithCommonBaseline.
   */
  private static class LinesWithCommonBaseline {

    /** The lines. */
    private final Collection<Line> lines = new ArrayList<>();
  }

  /**
   * The Class TableCandidateResult.
   */
  private static class TableCandidateResult {

    /** The table candidates. */
    private final List<TableCandidate> tableCandidates = new ArrayList<>();

    /** The discarded lines. */
    private final Collection<Line> discardedLines = new ArrayList<>();
  }

  /**
   * The Class TableCandidate.
   */
  private static class TableCandidate {

    /** The lines. */
    private final List<LinesWithCommonBaseline> lines = new ArrayList<>();
  }
}
