package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Baselined;
import io.committed.krill.extraction.pdfbox.physical.Line;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/** Utility functions to help with spatial analysis of {@link Line}s. */
public class LineSpacingUtils {

  /** The Constant BASELINE_COMPARATOR. */
  private static final Comparator<Line> BASELINE_COMPARATOR =
      Comparator.comparing(Line::getBaseline).thenComparing(s -> s.getPosition().getMinX());

  /** Instantiates a new line spacing utils. */
  private LineSpacingUtils() {
    // no easy construction
  }

  /**
   * Sort by baseline.
   *
   * @param baselined the baselined
   * @return the list
   */
  static List<Line> sortByBaseline(Collection<Line> baselined) {
    List<Line> list = new ArrayList<>(baselined);
    list.sort(BASELINE_COMPARATOR);
    return list;
  }

  /**
   * Most frequent baseline spacing.
   *
   * @param sorted the sorted
   * @return the int
   */
  static int mostFrequentBaselineSpacing(List<Line> sorted) {
    int[] lineSpacings = gatherLineSpacings(sorted);

    int largestCount = 0;
    int largestSpacing = 0;
    for (int i = 0; i < lineSpacings.length; i++) {
      if (lineSpacings[i] > largestCount) {
        largestSpacing = i;
        largestCount = lineSpacings[i];
      }
    }
    return largestSpacing;
  }

  /**
   * Gather line spacings.
   *
   * @param lines the lines
   * @return the int[]
   */
  private static int[] gatherLineSpacings(List<Line> lines) {
    int[] spacings = createSpacings(lines);

    int max = Arrays.stream(spacings).max().getAsInt();
    int[] spacingCounts = new int[max + 1];
    for (int spacing : spacings) {
      // ignore lines on same baseline - this can occur if line extraction was wrong, or multiple
      // columns are on the same baseline
      if (spacing > 0) {
        spacingCounts[spacing]++;
      }
    }
    return spacingCounts;
  }

  /**
   * Creates the spacings.
   *
   * @param <T> the generic type
   * @param lines the lines
   * @return the int[]
   */
  private static <T extends Baselined> int[] createSpacings(List<T> lines) {
    int[] spacings = new int[lines.size()];
    int index = 0;
    Iterator<T> it = lines.iterator();
    if (it.hasNext()) {
      T previous = it.next();
      while (it.hasNext()) {
        T current = it.next();
        int spacing = getSpacing(previous, current);
        spacings[index++] = spacing;
        previous = current;
      }
    }
    return spacings;
  }

  /**
   * Gets the spacing.
   *
   * @param <T> the generic type
   * @param previous the previous
   * @param current the current
   * @return the spacing
   */
  private static <T extends Baselined> int getSpacing(T previous, T current) {
    double currentPos = current.getBaseline();
    double previousPos = previous.getBaseline();
    return (int) Math.max(0, Math.abs(previousPos - currentPos));
  }
}
