package io.committed.krill.extraction.pdfbox.physical;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link PositionedContainer} for {@link Styled} content.
 * <p>
 * The style for the container is the most common style found in the content.
 * </p>
 *
 * @param <T>
 *          the content type, which must implement {@link Positioned} and {@link Styled}.
 *
 *
 */
public class PositionedStyledContainer<T extends Positioned & Styled> extends PositionedContainer<T>
    implements Styled {

  /** The style. */
  private Style style;

  /**
   * Creates a new {@link PositionedStyledContainer} for the given contennt.
   *
   * @param content
   *          the content.
   */
  public PositionedStyledContainer(List<T> content) {
    super(content);
    this.style = findCommonStyle();
  }

  /**
   * Find common style.
   *
   * @return the style
   */
  private Style findCommonStyle() {
    if (getContents().isEmpty()) {
      return null;
    }

    Map<Style, Long> styleCounts = getContents().stream().filter(Objects::nonNull)
        .collect(Collectors.groupingBy(s -> s.getStyle(), Collectors.counting()));
    Optional<Entry<Style, Long>> max = styleCounts.entrySet().stream()
        .max(Comparator.comparingLong(Entry::getValue));
    if (max.isPresent()) {
      return max.get().getKey();
    }

    return null;
  }

  /**
   * Returns the most common style in the container.
   *
   * @return the style, or <code>null</code> if there is no content.
   */
  @Override
  public Style getStyle() {
    return style;
  }

  @Override
  public void setStyle(Style style) {
    this.style = style;
  }
}
