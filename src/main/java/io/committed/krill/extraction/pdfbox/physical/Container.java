package io.committed.krill.extraction.pdfbox.physical;

import java.util.List;

/**
 * Simple container for multiple items.
 *
 * @param <T> the item type.
 */
@FunctionalInterface
public interface Container<T> {

  /**
   * Return the list of items.
   *
   * @return the items.
   */
  List<T> getContents();
}
