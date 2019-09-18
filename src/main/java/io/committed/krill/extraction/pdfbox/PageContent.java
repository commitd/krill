package io.committed.krill.extraction.pdfbox;

import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Text;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Represents the raw extracted content from PDF file.
 *
 * <p>All coordinates are given as a simple transformation of PDF space - each unit is 1/72 of an
 * inch, but the origin is the top left of the page, not the bottom left.
 *
 * <p>Text is given as a list of {@link PositionedContainer}s that contain {@link Text} elements
 * (each Text is a single code point).
 *
 * <p>Images are returned as a list of simple bounding boxes - no attempt is made to merge them
 * together if they are completely adjacent to one another with no gaps.
 *
 * <p>Finally, the "crop box" for the page (as opposed to the media box) is given - this represents
 * the area that would be rendered on screen.
 */
public class PageContent {

  /** The text sequences. */
  private final List<PositionedContainer<Text>> textSequences;

  /** The image locations. */
  private final List<Rectangle2D> imageLocations;

  /** The lines. */
  private final List<Line2D> lines;

  /** The rectangles. */
  private final List<Rectangle2D> rectangles;

  /** The crop box. */
  private final Rectangle2D cropBox;

  /**
   * Instantiates a new page content.
   *
   * @param textSequences the text sequences
   * @param imageLocations the image locations
   * @param lines the lines
   * @param rectangles the rectangles
   * @param cropBox the crop box
   */
  public PageContent(
      List<PositionedContainer<Text>> textSequences,
      List<Rectangle2D> imageLocations,
      List<Line2D> lines,
      List<Rectangle2D> rectangles,
      Rectangle2D cropBox) {
    this.textSequences = textSequences;
    this.imageLocations = imageLocations;
    this.lines = lines;
    this.rectangles = rectangles;
    this.cropBox = cropBox;
  }

  /**
   * Returns a list of {@link PositionedContainer}s that contain {@link Text} elements (each Text is
   * a single code point).
   *
   * @return the text
   */
  public List<PositionedContainer<Text>> getTextSequences() {
    return textSequences;
  }

  /**
   * Returns a list of bounding boxes for the images in the page.
   *
   * <p>Image areas are given as a list of simple bounding boxes and no attempt is made to union
   * them if they are immediately adjacent or overlap.
   *
   * @return the image locations
   */
  public List<Rectangle2D> getImageLocations() {
    return imageLocations;
  }

  /**
   * Returns all straight lines that were drawn on the page.
   *
   * <p>Note: some lines on a page may be drawn as rectangles (this is how underline is typically
   * rendered from MS Word) so this will not contain lines depicted with such shapes.
   *
   * @return a list of lines on the page
   */
  public List<Line2D> getLines() {
    return lines;
  }

  /**
   * Returns all rectangles that were drawn on the page.
   *
   * <p><strong>Note: see {@link #getLines()} - some rectangles are logically lines</strong>
   *
   * @return a list of rectangles on the page.
   */
  public List<Rectangle2D> getRectangles() {
    return rectangles;
  }

  /**
   * Returns the cropbox for this page (ie the actual visible content when rendered in a normal
   * viewer).
   *
   * @return a Rectangle2D representing the cropbox for the page.
   */
  public Rectangle2D getCropBox() {
    return cropBox;
  }
}
