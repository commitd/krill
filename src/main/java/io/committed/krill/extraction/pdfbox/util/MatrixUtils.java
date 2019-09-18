package io.committed.krill.extraction.pdfbox.util;

import java.awt.geom.AffineTransform;
import java.util.Optional;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.Matrix;

/** PDF matrix utility functions. */
public class MatrixUtils {

  private MatrixUtils() {
    // no easy construction
  }

  /**
   * Create the transform for rendering items onto the page.
   *
   * <p>This applies a translation and a scale of (-1,1) so that the origin is top left not bottom
   * left, and applies rotation if the page is declared rotated.
   *
   * @param matrix the current PDF transformational matrix
   * @param page the page
   * @param font an optional font (if present, the matrix is concatenated at the right point)
   * @return an {@link AffineTransform} for the current rendering matrices.
   */
  public static AffineTransform createTransform(Matrix matrix, PDPage page, Optional<PDFont> font) {
    AffineTransform at = createPageTranslationTransform(page);
    at.concatenate(matrix.createAffineTransform());
    if (font.isPresent()) {
      at.concatenate(font.get().getFontMatrix().createAffineTransform());
    }

    if (page.getRotation() != 0) {
      AffineTransform transform = createPageRotationTransform(page);
      transform.concatenate(at);
      at = transform;
    }
    return at;
  }

  /**
   * Creates the page translation transform.
   *
   * @param page the page
   * @return the affine transform
   */
  public static AffineTransform createPageTranslationTransform(PDPage page) {
    AffineTransform at = new AffineTransform();
    at.translate(0, page.getCropBox().getHeight());
    at.scale(1, -1);
    return at;
  }

  /**
   * Creates the page rotation transform.
   *
   * @param page the page
   * @return the affine transform
   */
  public static AffineTransform createPageRotationTransform(PDPage page) {
    PDRectangle cropBox = page.getCropBox();
    AffineTransform transform = new AffineTransform();
    transform.concatenate(
        AffineTransform.getTranslateInstance(cropBox.getHeight() / 2, cropBox.getWidth() / 2));
    transform.concatenate(
        AffineTransform.getRotateInstance(Math.toRadians(360d - page.getRotation())));
    transform.concatenate(
        AffineTransform.getTranslateInstance(-cropBox.getWidth() / 2, -cropBox.getHeight() / 2));
    return transform;
  }
}
