package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.interpretation.LabellablePositioned;
import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;
import io.committed.krill.extraction.tika.pdf.PdfParserConfig;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** Default implementation of PageSegmenter. */
public class SimplePageSegmenter implements PageSegmenter {

  /** The line finder. */
  private final LineFinder lineFinder;

  /** The word segmenter. */
  private final WordSegmenter wordSegmenter;

  /** The line block grouper. */
  private final LineBlockGrouper lineBlockGrouper;

  /** The table extractor. */
  private final TableExtractor tableExtractor;

  /** The Constant FIND_TABLES. */
  private static final boolean FIND_TABLES = true;

  /** Create a new {@link SimplePageSegmenter}. */
  public SimplePageSegmenter(PdfParserConfig parserConfig) {
    lineFinder = new BaselineLineFinder(parserConfig);
    wordSegmenter = new WhitespaceWordSegmenter();
    lineBlockGrouper = new XyCutBlockGrouper(parserConfig);
    tableExtractor = new CombinedTableExtractor(parserConfig);
  }

  @Override
  public Collection<LabellablePositioned> segment(
      List<PositionedContainer<Text>> sequences, Collection<Line2D> lines) {
    List<PositionedContainer<Text>> lineCandidates = lineFinder.findLines(sequences);
    ArrayList<Line> textLines = gatherLines(lineCandidates);

    if (FIND_TABLES) {
      TableResult tableResults = tableExtractor.findTables(textLines, lines);
      Collection<LabellablePositioned> blocks = new ArrayList<>();
      Collection<TextBlock> textBlocks = lineBlockGrouper.group(tableResults.getRemainingLines());
      blocks.addAll(tableResults.getTableBlocks());
      blocks.addAll(textBlocks);
      return blocks;
    } else {
      return new ArrayList<>(lineBlockGrouper.group(textLines));
    }
  }

  /**
   * Gather lines.
   *
   * @param lineCandidates the line candidates
   * @return the array list
   */
  private ArrayList<Line> gatherLines(List<PositionedContainer<Text>> lineCandidates) {
    ArrayList<Line> lines = new ArrayList<>();
    for (PositionedContainer<Text> lineCandidate : lineCandidates) {
      Optional<Line> segmented = wordSegmenter.segmentWords(lineCandidate);
      if (segmented.isPresent()) {
        lines.add(segmented.get());
      }
    }
    return lines;
  }
}
