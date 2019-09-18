package io.committed.krill.extraction.pdfbox;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import io.committed.krill.extraction.pdfbox.text.CharacterExtractor;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class CharacterExtractorPositionTest {

  private Vector displacement;

  private PDGraphicsState graphicsState;

  private CharacterExtractor characterExtractor;

  private Matrix matrix;

  private PDPage page;

  @Before
  public void setup() {
    this.characterExtractor = new CharacterExtractor();
    this.displacement = new Vector(5, 1);
    this.matrix = new Matrix();
    this.page = new PDPage(new PDRectangle(72 * 8.27f, 72 * 11.69f));
    this.graphicsState = new PDGraphicsState(page.getMediaBox());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetBoundingBoxErrorHandling() throws IOException {
    PDType1Font spyFont = spy(PDType1Font.COURIER);
    doThrow(new IOException()).when(spyFont).getBoundingBox();
    characterExtractor.calculateStyleAndPosition(
        page, matrix, matrix, spyFont, 32, " ", displacement, graphicsState, false);
  }

  // @Test()
  // public void testFoo() throws IOException {
  // PDType1Font spyFont = spy(PDType1Font.COURIER);
  // Text pos = characterExtractor.calculateStyleAndPosition(page, matrix, spyFont, 32, " ",
  // displacement, graphicsState, false);
  // assertThat(pos.getBaseline()).isEqualTo(0.0f);
  // assertThat(pos.getPosition().getY()).isEqualTo(0.0d);
  // assertThat(pos.getPosition().getWidth()).isEqualTo(0.0d);
  // assertThat(pos.getPosition().getHeight()).isEqualTo(0.0d);
  // assertThat(pos.toString()).isEqualTo(" ");
  // }

}
