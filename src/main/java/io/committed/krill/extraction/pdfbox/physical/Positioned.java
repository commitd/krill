package io.committed.krill.extraction.pdfbox.physical;

import java.awt.geom.Rectangle2D;

/**
 * Positioned items have a Rectangle2D that represents their location in a page.
 */
@FunctionalInterface
public interface Positioned {

  /**
   * Returns the position in the page.
   *
   * @return the position.
   */
  Rectangle2D getPosition();
}
