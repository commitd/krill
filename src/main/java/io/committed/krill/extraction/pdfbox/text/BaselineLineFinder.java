package io.committed.krill.extraction.pdfbox.text;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.committed.krill.extraction.pdfbox.physical.Baselined;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Style;
import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.tika.pdf.PdfParserConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A {@link LineFinder} that identifies lines by grouping by {@link Style} and then by {@link
 * Baselined baseline}.
 *
 * <p>A line break within a baseline (eg for multiple columns) is created if the next {@link Text}
 * encountered when ordering by minimum X position is considered too far from the previous, or when
 * a new stream of {@link Text}s was created in the PDF (some generators emit each column line as a
 * new sequence).
 */
public class BaselineLineFinder implements LineFinder {

  /** The Constant CONSIDER_TEXT_STREAM. */
  private static final boolean CONSIDER_TEXT_STREAM = true;

  private final PdfParserConfig parserConfig;

  public BaselineLineFinder(PdfParserConfig parserConfig) {
    this.parserConfig = parserConfig;
  }

  @Override
  public List<PositionedContainer<Text>> findLines(List<PositionedContainer<Text>> sequences) {
    List<Text> positions =
        sequences.stream()
            .map(PositionedContainer::getContents)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    List<ArrayList<Text>> candidateLines = findBaselineLines(positions);
    List<PositionedContainer<Text>> lines = new ArrayList<>();

    // this is flawed as it doesn't keep the flow when font changes within a line
    for (ArrayList<Text> arrayList : candidateLines) {
      Map<Float, Collection<Text>> textByStyle = collectTextByStyle(arrayList);
      for (Collection<Text> texts : textByStyle.values()) {
        ArrayList<Text> line = new ArrayList<>(texts);
        line.sort(Comparator.comparing(s -> s.getPosition().getMinX()));
        lines.add(new PositionedContainer<Text>(line));
      }
    }
    return lines;
  }

  /**
   * Collect text by style.
   *
   * @param candidateLine the candidate line
   * @return the map
   */
  private Map<Float, Collection<Text>> collectTextByStyle(ArrayList<Text> candidateLine) {
    Multimap<Float, Text> styledLines = HashMultimap.create();
    candidateLine.forEach(text -> styledLines.put(text.getStyle().getSize(), text));
    return styledLines.asMap();
  }

  /**
   * Find baseline lines.
   *
   * @param texts the texts
   * @return the list
   */
  private List<ArrayList<Text>> findBaselineLines(Collection<Text> texts) {
    List<Text> bag = new ArrayList<>(texts);
    bag.sort(Comparator.comparing(Text::getBaseline).thenComparing(s -> s.getPosition().getMinX()));

    List<ArrayList<Text>> lines = new ArrayList<>();

    List<Text> currentLine = new ArrayList<>();
    float prevBaseLine = 0f;
    float cumulativeWidth = 0f;
    Text previous = null;
    for (Text text : bag) {
      if (isLineBreak(currentLine, prevBaseLine, cumulativeWidth, previous, text)) {
        lines.add(new ArrayList<Text>(currentLine));
        currentLine = new ArrayList<>();
        cumulativeWidth = 0f;
      }
      cumulativeWidth += text.getPosition().getWidth();
      currentLine.add(text);
      prevBaseLine = text.getBaseline();
      previous = text;
    }
    if (!currentLine.isEmpty()) {
      lines.add(new ArrayList<Text>(currentLine));
    }
    return lines;
  }

  /**
   * Checks if is line break.
   *
   * @param currentLine the current line
   * @param prevBaseLine the prev base line
   * @param cumulativeWidth the cumulative width
   * @param previous the previous
   * @param text the text
   * @return true, if is line break
   */
  private boolean isLineBreak(
      List<Text> currentLine, float prevBaseLine, float cumulativeWidth, Text previous, Text text) {
    boolean baselineChanged = Float.compare(prevBaseLine, text.getBaseline()) != 0;
    boolean haveContent = !currentLine.isEmpty();
    boolean tooFar =
        CONSIDER_TEXT_STREAM
            && text.isStartText()
            && isTooFarAwayHorizontally(text, previous, cumulativeWidth / currentLine.size());
    return haveContent && (tooFar || baselineChanged);
  }

  /**
   * Checks if is too far away horizontally.
   *
   * @param text the text
   * @param previous the previous
   * @param averageWidth the average width
   * @return true, if is too far away horizontally
   */
  private boolean isTooFarAwayHorizontally(Text text, Text previous, float averageWidth) {
    if (previous == null || text == null) {
      return false;
    }

    double minX = text.getPosition().getMinX();
    double maxX = previous.getPosition().getMaxX();
    return minX - maxX >= parserConfig.getMaxBaselineSeprationMultiplier() * averageWidth;
  }
}
