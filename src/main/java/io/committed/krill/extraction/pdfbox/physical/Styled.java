package io.committed.krill.extraction.pdfbox.physical;

/** Allows the rendered text style of an item to be retrieved. */
public interface Styled {
  /**
   * Returns the style.
   *
   * @return the style.
   */
  Style getStyle();

  /**
   * Sets the style.
   *
   * @param style the new style
   */
  void setStyle(Style style);
}
