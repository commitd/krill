package io.committed.krill.extraction.pdfbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.text.CharacterExtractor;
import java.awt.geom.AffineTransform;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class CharacterExtractorStyleTest {

  private CharacterExtractor characterExtractor;

  private Matrix matrix;

  private Vector displacement;

  private PDGraphicsState graphicsState;

  private PDPage page;

  @Before
  public void setup() {
    this.characterExtractor = new CharacterExtractor();
    this.matrix = new Matrix();
    this.displacement = new Vector(0, 0);
    this.graphicsState = new PDGraphicsState(new PDRectangle(100, 100));
    this.page = new PDPage(new PDRectangle(72 * 8.27f, 72 * 11.69f));
  }

  @Test
  public void testStyleBoldWithBoldInName() {
    PDFont font = PDType1Font.COURIER_BOLD;
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isBold()).isTrue();
  }

  @Test
  public void testStyleBoldWithBlackInName() {
    PDFont font = spy(PDType1Font.COURIER);
    doReturn("Something Black").when(font).getName();
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isBold()).isTrue();
  }

  @Test
  public void testStyleBoldWithHeavyInName() {
    PDFont font = spy(PDType1Font.COURIER);
    doReturn("Something Heavy").when(font).getName();
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isBold()).isTrue();
  }

  @Test
  public void testStyleNotBoldWithNoBoldInName() {
    PDFont font = PDType1Font.COURIER;
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isBold()).isFalse();
  }

  @Test
  public void testStyleItalicWithObliqueInName() {
    PDFont font = PDType1Font.COURIER_OBLIQUE;
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isItalic()).isTrue();
  }

  @Test
  public void testStyleNotItalicWithNoObliqueInName() {
    PDFont font = PDType1Font.COURIER;
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isItalic()).isFalse();
  }

  @Test
  public void testStyleItalicWithItalicInName() {
    PDFont font = PDType1Font.TIMES_ITALIC;
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isItalic()).isTrue();
  }

  @Test
  public void testStyleNotItalicWithNoItalicInName() {
    PDFont font = PDType1Font.TIMES_ROMAN;
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isItalic()).isFalse();
  }

  private Text getStyleForFont(PDFont font) {
    return characterExtractor.calculateStyleAndPosition(
        page, matrix, matrix, font, 'A', "A", displacement, graphicsState, false);
  }

  @Test
  public void testShearItalic() {
    PDFont font = PDType1Font.TIMES_ROMAN;
    matrix.setValue(1, 0, 1f);
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isItalic()).isTrue();

    matrix.setValue(1, 0, 0f);
    positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isItalic()).isFalse();
  }

  @Test
  public void testDeclaredItalic() {
    PDFont font = spy(PDType1Font.TIMES_ROMAN);
    PDFontDescriptor pdFontDescriptor = new PDFontDescriptor(new COSDictionary());
    pdFontDescriptor.setItalic(true);
    doReturn(pdFontDescriptor).when(font).getFontDescriptor();
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isItalic()).isTrue();
  }

  @Test
  public void testWideLineStrokeIsBold() {
    graphicsState.setLineWidth(2f);
    graphicsState.getTextState().setRenderingMode(RenderingMode.FILL_STROKE);
    Text positionedStyledText =
        characterExtractor.calculateStyleAndPosition(
            page,
            matrix,
            matrix,
            PDType1Font.COURIER,
            'A',
            "A",
            displacement,
            graphicsState,
            false);
    assertThat(positionedStyledText.getStyle().isBold()).isTrue();
  }

  @Test
  public void testWideLineNoStrokeIsNotBold() {
    graphicsState.setLineWidth(2f);
    Text positionedStyledText =
        characterExtractor.calculateStyleAndPosition(
            page,
            matrix,
            matrix,
            PDType1Font.COURIER,
            'A',
            "A",
            displacement,
            graphicsState,
            false);
    assertThat(positionedStyledText.getStyle().isBold()).isFalse();
  }

  @Test
  public void testThinLineStrokeIsNotBold() {
    graphicsState.getTextState().setRenderingMode(RenderingMode.FILL_STROKE);
    Text positionedStyledText =
        characterExtractor.calculateStyleAndPosition(
            page,
            matrix,
            matrix,
            PDType1Font.COURIER,
            'A',
            "A",
            displacement,
            graphicsState,
            false);
    assertThat(positionedStyledText.getStyle().isBold()).isFalse();
  }

  @Test
  public void testFontWeightBold() {
    PDFont font = spy(PDType1Font.TIMES_ROMAN);
    PDFontDescriptor pdFontDescriptor = new PDFontDescriptor(new COSDictionary());
    pdFontDescriptor.setFontWeight(700);
    doReturn(pdFontDescriptor).when(font).getFontDescriptor();
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isBold()).isTrue();
  }

  @Test
  public void testForceBoldIsBold() {
    PDFont font = spy(PDType1Font.TIMES_ROMAN);
    PDFontDescriptor pdFontDescriptor = new PDFontDescriptor(new COSDictionary());
    pdFontDescriptor.setForceBold(true);
    doReturn(pdFontDescriptor).when(font).getFontDescriptor();
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isBold()).isTrue();
  }

  @Test
  public void testForceBoldIsNotBold() {
    PDFont font = spy(PDType1Font.TIMES_ROMAN);
    PDFontDescriptor pdFontDescriptor = new PDFontDescriptor(new COSDictionary());
    pdFontDescriptor.setForceBold(false);
    doReturn(pdFontDescriptor).when(font).getFontDescriptor();
    Text positionedStyledText = getStyleForFont(font);
    assertThat(positionedStyledText.getStyle().isBold()).isFalse();
  }

  @Test
  public void testSizeNoScale() {
    graphicsState.getTextState().setFontSize(0f);
    Text positionedStyledText =
        characterExtractor.calculateStyleAndPosition(
            page,
            matrix,
            matrix,
            PDType1Font.COURIER,
            'A',
            "A",
            displacement,
            graphicsState,
            false);
    assertThat(positionedStyledText.getStyle().getSize()).isEqualTo(0f);

    graphicsState.getTextState().setFontSize(14f);
    positionedStyledText =
        characterExtractor.calculateStyleAndPosition(
            page,
            matrix,
            matrix,
            PDType1Font.COURIER,
            'A',
            "A",
            displacement,
            graphicsState,
            false);
    assertThat(positionedStyledText.getStyle().getSize()).isEqualTo(14f);
  }

  @Test
  public void testSizeScale() {
    Matrix scaleMatrix = new Matrix(AffineTransform.getScaleInstance(14, 14));
    graphicsState.getTextState().setFontSize(0f);
    Text positionedStyledText =
        characterExtractor.calculateStyleAndPosition(
            page,
            matrix,
            scaleMatrix,
            PDType1Font.COURIER,
            'A',
            "A",
            displacement,
            graphicsState,
            false);
    assertThat(positionedStyledText.getStyle().getSize()).isEqualTo(0f);

    graphicsState.getTextState().setFontSize(1.0f);
    positionedStyledText =
        characterExtractor.calculateStyleAndPosition(
            page,
            matrix,
            scaleMatrix,
            PDType1Font.COURIER,
            'A',
            "A",
            displacement,
            graphicsState,
            false);
    assertThat(positionedStyledText.getStyle().getSize()).isEqualTo(14f);
  }
}
