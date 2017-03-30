package io.committed.krill.extraction.pdfbox.util;

import static org.junit.Assert.assertEquals;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;
import org.junit.Before;
import org.junit.Test;

import io.committed.krill.extraction.pdfbox.util.MatrixUtils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Optional;

public class MatrixUtilsTest {

  private Matrix matrix;

  private PDPage page;

  @Before
  public void setup() {
    page = new PDPage(PDRectangle.A4);
    matrix = new Matrix();
  }

  @Test
  public void testCreatePageTranslationTransform() {
    AffineTransform transform = MatrixUtils.createPageTranslationTransform(page);
    Point2D result = transform.transform(new Point2D.Double(0, 0), new Point2D.Double());
    assertEquals(new Point2D.Double(0, PDRectangle.A4.getHeight()), result);
  }

  @Test
  public void testCreatePageRotationTransform() {
    page.setRotation(90);
    AffineTransform transform = MatrixUtils.createPageRotationTransform(page);
    Point2D result = transform.transform(new Point2D.Double(0, 0), new Point2D.Double());
    assertEquals(new Point2D.Double(0, PDRectangle.A4.getWidth()), result);
  }

  @Test
  public void testCreateTransformNoRotation() {
    AffineTransform transform = MatrixUtils.createTransform(matrix, page, Optional.empty());
    Point2D result = transform.transform(new Point2D.Double(0, 0), new Point2D.Double());
    assertEquals(new Point2D.Double(0, PDRectangle.A4.getHeight()), result);
  }

  @Test
  public void testCreateTransformRotation() {
    page.setRotation(90);
    AffineTransform transform = MatrixUtils.createTransform(matrix, page, Optional.empty());
    Point2D result = transform.transform(new Point2D.Double(0, 0), new Point2D.Double());
    assertEquals(new Point2D.Double(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()), result);
  }

}
