package io.committed.krill.extraction.pdfbox.physical;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A base implementation of {@link Container} that is also {@link Positioned}.
 *
 * @param <T> the type of contents, which must also be {@link Positioned}
 */
public class PositionedContainer<T extends Positioned> implements Container<T>, Positioned {

  /** The Constant EMPTY_BOUNDS. */
  private static final Rectangle2D EMPTY_BOUNDS = new Rectangle2D.Float();

  /** The content. */
  private final List<T> content;

  /** The position. */
  private final Rectangle2D position;

  /**
   * Creates a new PositionedContainer for the given list of {@link Positioned} content.
   *
   * @param content the content
   */
  public PositionedContainer(List<T> content) {
    this.content = content;
    this.position = deriveAggregateBounds();
  }

  /**
   * Derive aggregate bounds.
   *
   * @return the rectangle 2 D
   */
  private Rectangle2D deriveAggregateBounds() {
    if (content.isEmpty()) {
      return EMPTY_BOUNDS;
    }
    if (content.size() == 1) {
      T next = content.iterator().next();
      return next == null ? EMPTY_BOUNDS : next.getPosition();
    }
    Optional<Rectangle2D> bounds =
        content.stream()
            .filter(s -> s != null && !s.getPosition().isEmpty())
            .map(Positioned::getPosition)
            .reduce((left, right) -> left.createUnion(right));

    return bounds.isPresent() ? bounds.get() : EMPTY_BOUNDS;
  }

  @Override
  public Rectangle2D getPosition() {
    return position;
  }

  @Override
  public List<T> getContents() {
    return content;
  }

  @Override
  public String toString() {
    return content.stream().map(s -> s == null ? "" : s.toString()).collect(Collectors.joining());
  }
}
