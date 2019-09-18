package io.committed.krill.extraction.pdfbox;

import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.text.CharacterExtractor;
import io.committed.krill.extraction.pdfbox.util.MatrixUtils;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

/**
 * A replacement for {@link PDFTextStripper} that is aware of image areas, font colours, etc.
 *
 * <p>Note: This class is NOT thread-safe.
 */
public class PdfStripper extends PDFGraphicsStreamEngine {

  /** The Constant UNIT_RECTANGLE. */
  private static final Rectangle2D UNIT_RECTANGLE = new Rectangle2D.Float(0f, 0f, 1f, 1f);

  /** The text positions. */
  private List<PositionedContainer<Text>> textPositions = new ArrayList<>();

  /** The image positions. */
  private List<Rectangle2D> imagePositions = new ArrayList<>();

  /** The lines. */
  private List<Line2D> lines = new ArrayList<>();

  /** The rectangles. */
  private List<Rectangle2D> rectangles = new ArrayList<>();

  /** The character extractor. */
  private final CharacterExtractor characterExtractor = new CharacterExtractor();

  /** The current texts. */
  private List<Text> currentTexts = new ArrayList<>();

  /** The last text. */
  private Text lastText = null;

  /** The current point. */
  private Point2D currentPoint;

  /** Instantiates a new pdf stripper. */
  public PdfStripper() {
    super(null);
  }

  /**
   * Given a PDF document and a 0-indexed page number, return the content of the page.
   *
   * <p>This is the intended main entry point to this class.
   *
   * @param doc the document to process.
   * @param page the 0-indexed page number
   * @return the {@link PageContent}
   * @throws IOException if an error occurs during parsing (which could be due to badly encoded
   *     {@link PDFont} data).
   */
  public PageContent processPage(PDDocument doc, int page) throws IOException {
    textPositions = new ArrayList<>();
    imagePositions = new ArrayList<>();
    rectangles = new ArrayList<>();
    lines = new ArrayList<>();
    PDPage pdPage = doc.getPages().get(page);
    processPage(pdPage);
    Rectangle2D pageBounds = pdPage.getCropBox().toGeneralPath().getBounds2D();
    return new PageContent(textPositions, imagePositions, lines, rectangles, pageBounds);
  }

  @Override
  protected void showFontGlyph(
      Matrix textRenderingMatrix, PDFont font, int code, String unicode, Vector displacement) {
    if (unicode == null) {
      return;
    }
    PDGraphicsState graphicsState = getGraphicsState();
    Text positionedStyledText =
        characterExtractor.calculateStyleAndPosition(
            getCurrentPage(),
            textRenderingMatrix,
            getTextMatrix(),
            font,
            code,
            unicode,
            displacement,
            graphicsState,
            lastText == null);
    lastText = positionedStyledText;
    currentTexts.add(positionedStyledText);
  }

  @Override
  public void beginText() throws IOException {
    lastText = null;
  }

  @Override
  public void endText() throws IOException {
    if (lastText == null) {
      return;
    }
    lastText.setEndText(true);
    PositionedContainer<Text> container = new PositionedContainer<>(currentTexts);
    textPositions.add(container);
    currentTexts = new ArrayList<>();
  }

  @Override
  public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException {
    double x1 = Math.min(p0.getX(), Math.min(p1.getX(), Math.min(p2.getX(), p3.getX())));
    double y1 = Math.min(p0.getY(), Math.min(p1.getY(), Math.min(p2.getY(), p3.getY())));
    double x2 = Math.max(p0.getX(), Math.max(p1.getX(), Math.max(p2.getX(), p3.getX())));
    double y2 = Math.max(p0.getY(), Math.max(p1.getY(), Math.max(p2.getY(), p3.getY())));
    Rectangle2D.Double rectangle = new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
    Shape shape = getCurrentGraphicsTransform().createTransformedShape(rectangle);

    if (isNotWhiteStroking()) {
      rectangles.add(shape.getBounds2D());
    }
  }

  /**
   * Checks if graphics context is not white stroking.
   *
   * @return true, if is not white stroking
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private boolean isNotWhiteStroking() throws IOException {
    return 16777215 != getGraphicsState().getNonStrokingColor().toRGB();
  }

  @Override
  public void drawImage(PDImage pdImage) throws IOException {
    // For now this just stores the bounding box in text space naively transformed to screen space
    // (ie origin in top left, but not scaled to the screen size).
    AffineTransform at = getCurrentGraphicsTransform();
    Rectangle2D origin = at.createTransformedShape(UNIT_RECTANGLE).getBounds2D();
    imagePositions.add(origin);
  }

  /**
   * Gets the current graphics transform.
   *
   * @return the current graphics transform
   */
  private AffineTransform getCurrentGraphicsTransform() {
    Matrix matrix = getGraphicsState().getCurrentTransformationMatrix();

    AffineTransform at = new AffineTransform();
    at.translate(1, getCurrentPage().getCropBox().getHeight());
    at.scale(1, -1);
    at.concatenate(matrix.createAffineTransform());

    PDPage page = getCurrentPage();
    if (page.getRotation() != 0) {
      AffineTransform transform = MatrixUtils.createTransform(matrix, page, Optional.empty());
      transform.concatenate(at);
      at = transform;
    }
    return at;
  }

  @Override
  public void clip(int windingRule) throws IOException {
    // do nothing
  }

  @Override
  public void moveTo(float x1, float y1) throws IOException {
    Point2D.Float newPoint = transformPoint(x1, y1);
    currentPoint = newPoint;
  }

  /**
   * Transform point.
   *
   * @param x1 the x 1
   * @param y1 the y 1
   * @return the point 2 d. float
   */
  private Point2D.Float transformPoint(float x1, float y1) {
    Point2D.Float newPoint = new Point2D.Float(x1, y1);
    getCurrentGraphicsTransform().transform(newPoint, newPoint);
    return newPoint;
  }

  @Override
  public void lineTo(float x1, float y1) throws IOException {
    Point2D.Float newPoint = transformPoint(x1, y1);
    lines.add(new Line2D.Float(currentPoint, newPoint));
    currentPoint = newPoint;
  }

  @Override
  public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3)
      throws IOException {
    Point2D newPoint = new Point2D.Float(x3, y3);
    getCurrentGraphicsTransform().transform(newPoint, newPoint);
    currentPoint = newPoint;
  }

  @Override
  public Point2D getCurrentPoint() throws IOException {
    return currentPoint;
  }

  @Override
  public void closePath() throws IOException {
    // do nothing
  }

  @Override
  public void endPath() throws IOException {
    // do nothing
  }

  @Override
  public void strokePath() throws IOException {
    // do nothing
  }

  @Override
  public void fillPath(int windingRule) throws IOException {
    // do nothing
  }

  @Override
  public void fillAndStrokePath(int windingRule) throws IOException {
    // do nothing
  }

  @Override
  public void shadingFill(COSName shadingName) throws IOException {
    // do nothing
  }
}
