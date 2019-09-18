package io.committed.krill.extraction.pdfbox.interpretation;

import java.util.Collection;
import java.util.List;

/**
 * Implementations of {@link ReadingOrder} sort {@link LabellablePositioned} blocks that make up a
 * single page into a logical order for reading.
 */
@FunctionalInterface
public interface ReadingOrder {

  /**
   * Order the given collection of blocks into a {@link List}.
   *
   * @param blocks the {@link Collection} of blocks to order
   * @return an ordered {@link List} of blocks, in reading order.
   */
  List<LabellablePositioned> order(Collection<LabellablePositioned> blocks);
}
