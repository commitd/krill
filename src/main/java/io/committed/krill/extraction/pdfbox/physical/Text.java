package io.committed.krill.extraction.pdfbox.physical;

import java.awt.geom.Rectangle2D;

/**
 * Representation of the physical text contained in the PDF. This is often a single character (which
 * may be composed of multiple combining glyphs).
 */
public class Text implements Positioned, Styled, Baselined {

  /** The content. */
  private final String content;

  /** The position. */
  private final Rectangle2D position;

  /** The baseline. */
  private final float baseline;

  /** The start text. */
  private final boolean startText;

  /** The end text. */
  private boolean endText;

  /** The style. */
  private Style style;

  /**
   * Creates a new text element (a single code point, which may be encoded as a surrogate pair so
   * stored as a String).
   *
   * @param content the String representation of the code point, if available.
   * @param position the bounding box of this item in the page.
   * @param baseLine the computed baseline of the text (may be different from the bottom of the
   *     bounding box - consider descenders)
   * @param style the style computed for the text based on current rendering state.
   * @param startText whether this text is the first in a sequence or not.
   */
  public Text(
      String content, Rectangle2D position, float baseLine, Style style, boolean startText) {
    this.content = content;
    this.position = position;
    this.style = style;
    this.baseline = baseLine;
    this.startText = startText;
  }

  @Override
  public Rectangle2D getPosition() {
    return position;
  }

  @Override
  public float getBaseline() {
    return baseline;
  }

  @Override
  public Style getStyle() {
    return style;
  }

  @Override
  public void setStyle(Style style) {
    this.style = style;
  }

  @Override
  public String toString() {
    return content;
  }

  /**
   * Returns whether this text is the first in a sequence or not.
   *
   * @return true if this is the first text in a sequence.
   */
  public boolean isStartText() {
    return startText;
  }

  /**
   * Returns whether this text marks the last in a sequence or not.
   *
   * @return true if this is the last text in a sequence.
   */
  public boolean isEndText() {
    return endText;
  }

  /**
   * Sets whether this text is the last in a sequence or not.
   *
   * @param endText the new end text
   */
  public void setEndText(boolean endText) {
    this.endText = endText;
  }
}
