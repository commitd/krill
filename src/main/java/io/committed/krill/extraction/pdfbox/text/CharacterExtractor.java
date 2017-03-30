package io.committed.krill.extraction.pdfbox.text;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.committed.krill.extraction.pdfbox.PdfStripper;
import io.committed.krill.extraction.pdfbox.physical.Style;
import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.util.MatrixUtils;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.rendering.RendererPackageWorkaround;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Called by {@link PdfStripper}, this class extracts {@link Text} objects representing characters
 * on a page. To help save memory it caches both computed {@link Style}s (essentially Java2D
 * {@link GeneralPath paths} for each rendered character) used to calculate character bounding
 * boxes.
 */
public class CharacterExtractor {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(CharacterExtractor.class);

  /** The style cache. */
  private final Cache<Style, Style> styleCache = createCache();

  /** The renderer workaround. */
  private final RendererPackageWorkaround rendererWorkaround = new RendererPackageWorkaround();

  /**
   * For the given code point (and state), create a {@link Text} representing its text, style and
   * position on the page.
   *
   * @param page
   *          the page
   * @param textRenderingMatrix
   *          The current text rendering matrix.
   * @param textMatrix
   *          The current text matrix (without font transform etc)
   * @param font
   *          The current font.
   * @param code
   *          The code point in the font.
   * @param unicode
   *          The unicode representation of the glyph in the font.
   * @param displacement
   *          The current PDF displacement.
   * @param graphicsState
   *          The current graphics state.
   * @param startText
   *          whether this is the first character in a start/end text stream
   * @return A new {@link Text} containing the location, style and text represented by this glyph,
   *         or null if it was not possible to determine a character for the code point. Note:
   *         whitespace is represented using {@link PDFont#getBoundingBox()}
   */
  public Text calculateStyleAndPosition(PDPage page, Matrix textRenderingMatrix, Matrix textMatrix,
      PDFont font, int code, String unicode, Vector displacement, PDGraphicsState graphicsState,
      boolean startText) {
    AffineTransform at = MatrixUtils.createTransform(textRenderingMatrix, page, Optional.of(font));
    Rectangle2D bounds = calculateBoundingBox(at, font, code, displacement);
    float baseline = calculateBaseline(at);
    Style style = determineFontStyle(font, textMatrix, graphicsState);
    return new Text(unicode, bounds, baseline, style, startText);
  }

  /**
   * Returns the baseline that would be used with the given {@link AffineTransform} (or, more
   * strictly, the y coordinate of point (0,0) transformed through the current matrices).
   *
   * @param at
   *          the {@link AffineTransform} that will be used to position glyphs on the page.
   * @return the baseline that would be used with the given {@link AffineTransform}
   * @see <a href=
   *      "https://www.freetype.org/freetype2/docs/glyphs/glyphs-3.html">https://www.freetype.org/freetype2/docs/glyphs/glyphs-3.html</a>
   */
  private static float calculateBaseline(AffineTransform at) {
    return (float) at.transform(new Point2D.Float(0, 0), new Point2D.Float()).getY();
  }

  /**
   * Calculate the bounding box of the given {@link PDFont} codepoint, as would be positioned using
   * the {@link AffineTransform} and displacement {@link Vector}.
   *
   * @param at
   *          the transform used to position glyphs on the page.
   * @param font
   *          the {@link PDFont} to obtain glyphs from.
   * @param code
   *          the codepoint of the glyph to process
   * @param displacement
   *          the current PDF displacement {@link Vector}.
   * @return the bounding box of the character on the page.
   */
  private Rectangle2D calculateBoundingBox(AffineTransform at, PDFont font, int code,
      Vector displacement) {
    Shape path = null;
    try {
      path = getPath(font, code);
    } catch (IOException e) {
      LOGGER.warn("Failed to get path for code point {} in font {}", code, font.getName(), e);
    }

    if (path == null || path.getBounds2D().getWidth() <= 0.01f) {
      path = createFontBounds(font);
    }

    return createTranslatedBoundsForPath(at, font, displacement, path);
  }

  /**
   * Gets the path.
   *
   * @param font
   *          the font
   * @param code
   *          the code
   * @return the path
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private Shape getPath(PDFont font, int code) throws IOException {
    return rendererWorkaround.getPathForCharacter(font, code);
  }

  /**
   * Given the path representing a glyph, translate it to page coordinates and correct size, and
   * return the bounding box, taking into account displacement.
   *
   * <p>
   * Note: if the font is not embedded and the width of the character does match the expected
   * displacement then the glyph is stretched accordingly.
   * </p>
   *
   * @param at
   *          the transform used to position glyphs on the page.
   * @param font
   *          the {@link PDFont} to obtain glyphs from.
   * @param displacement
   *          the current PDF displacement {@link Vector}.
   * @param path
   *          the glyph path.
   * @return the translated and displaced bounding box for the path
   */
  private static Rectangle2D createTranslatedBoundsForPath(AffineTransform at, PDFont font,
      Vector displacement, Shape path) {
    // stretch non-embedded glyph if it is not a space and does not match the width in the PDF
    if (!font.isEmbedded()) {
      float fontWidth = (float) path.getBounds2D().getWidth();
      if (fontWidth > 0 && Math.abs(fontWidth - displacement.getX() * 1000) > 0.0001) {
        float pdfWidth = displacement.getX() * 1000;
        at.scale(pdfWidth / fontWidth, 1);
      }
    }
    return at.createTransformedShape(path).getBounds2D();
  }

  /**
   * Creates the font bounds.
   *
   * @param font
   *          the font
   * @return the rectangle 2 D
   */
  private Rectangle2D createFontBounds(PDFont font) {
    BoundingBox box;
    try {
      box = font.getBoundingBox();
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to get bounding box for font", e);
    }
    float width = font.getSpaceWidth();
    float height = box.getHeight();
    return new Rectangle2D.Float(0, 0, width, height);
  }

  /**
   * Determine font style.
   *
   * @param font
   *          the font
   * @param matrix
   *          the matrix
   * @param graphicsState
   *          the graphics state
   * @return the style
   */
  private Style determineFontStyle(PDFont font, Matrix matrix, PDGraphicsState graphicsState) {
    String fontName = getFontName(font);
    float fontSize = calculateFontSize(matrix, graphicsState.getTextState());
    boolean bold = isBold(font, graphicsState);
    boolean italic = isItalic(font, matrix);
    String color = determineTextColor(graphicsState);
    Style style = new Style(fontName, fontSize, bold, italic, false, color);
    try {
      return styleCache.get(style, () -> style);
    } catch (ExecutionException e) {
      LOGGER.warn("Failed to cache style", e);
      return style;
    }
  }

  /**
   * Get the name of the given font (often the PostScript name and not the name seen in the
   * authoring application, eg for TrueType fonts one may see Arial in the authoring application,
   * but ArialMT is returned here).
   * <p>
   * From PDF 32000-1:2008 "Document management — Portable document format — Part 1: PDF 1.7"
   * </p>
   * <blockquote>
   * <p>
   * <b>9.6.4 Font Subsets</b>
   * </p>
   * <p>
   * For a font subset, the PostScript name of the font—the value of the font’s BaseFont entry and
   * the font descriptor’s FontName entry— shall begin with a tag followed by a plus sign (+). The
   * tag shall consist of exactly six uppercase letters; the choice of letters is arbitrary, but
   * different subsets in the same PDF file shall have different tags.
   * </p>
   * </blockquote>
   * <p>
   * We therefore strip the prefix as it is of little practical use in this application.
   * </p>
   *
   * @param font
   *          the font.
   * @return the name of the font, with any prefix removed.
   */
  private String getFontName(PDFont font) {
    String fontName = font.getFontDescriptor().getFontName();

    if (font.isEmbedded() && fontName.length() > 7 && fontName.charAt(6) == '+') {
      return fontName.substring(7);
    }
    return fontName;
  }

  /**
   * Determine text color.
   *
   * @param graphicsState
   *          the graphics state
   * @return the string
   */
  private String determineTextColor(PDGraphicsState graphicsState) {
    PDColor nonStrokingColor = graphicsState.getNonStrokingColor();
    int nonStrokingRgb = 0;
    try {
      nonStrokingRgb = nonStrokingColor.toRGB();
    } catch (IOException e) {
      LOGGER.warn("Failed to get text color from PDF graphics state", e);
    }

    int rgb = nonStrokingRgb;
    return rgb == 0 ? "" : String.format("#%06x", 0xFFFFFF & rgb);
  }

  /**
   * Calculate font size.
   *
   * @param textMatrix
   *          the text matrix
   * @param textState
   *          the text state
   * @return the float
   */
  private static float calculateFontSize(Matrix textMatrix, PDTextState textState) {
    return textState.getFontSize() * textMatrix.getScalingFactorX();
  }

  /**
   * Checks if is bold.
   *
   * @param font
   *          the font
   * @param graphicsState
   *          the graphics state
   * @return true, if is bold
   */
  private static boolean isBold(PDFont font, PDGraphicsState graphicsState) {
    String lowerCaseName = font.getName().toLowerCase(Locale.ROOT);
    boolean boldName = lowerCaseName.contains("black") || lowerCaseName.contains("bold")
        || lowerCaseName.contains("heavy");

    PDFontDescriptor fontDescriptor = font.getFontDescriptor();
    boolean forceBold = fontDescriptor.isForceBold();
    boolean boldWeight = fontDescriptor.getFontWeight() >= 700;

    PDTextState textState = graphicsState.getTextState();
    RenderingMode renderingMode = textState.getRenderingMode();
    boolean boldLineStroke = graphicsState.getLineWidth() > 1.0f && renderingMode.isStroke();

    return forceBold || boldWeight || boldLineStroke || boldName;
  }

  /**
   * Checks if is italic.
   *
   * @param font
   *          the font
   * @param matrix
   *          the matrix
   * @return true, if is italic
   */
  private static boolean isItalic(PDFont font, Matrix matrix) {
    String lowerCaseName = font.getName().toLowerCase(Locale.ROOT);
    boolean italicName = lowerCaseName.contains("italic") || lowerCaseName.contains("oblique");

    boolean shearItalic = Math.abs(matrix.getValue(1, 0)) > 0.0f;

    PDFontDescriptor fontDescriptor = font.getFontDescriptor();
    boolean declaredItalic = fontDescriptor.isItalic();
    return declaredItalic || italicName || shearItalic;
  }

  /**
   * Convenience method for creating Guava {@link Cache} instances with consistent behaviour.
   *
   * @param <K>
   *          the key type
   * @param <V>
   *          the value type
   * @return a new Cache instance.
   */
  private static <K, V> Cache<K, V> createCache() {
    return CacheBuilder.newBuilder().maximumSize(1000).build();
  }

}
