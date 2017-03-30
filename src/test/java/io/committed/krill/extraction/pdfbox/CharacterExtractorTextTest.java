package io.committed.krill.extraction.pdfbox;

import static org.junit.Assert.assertEquals;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import org.junit.Before;
import org.junit.Test;

import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.text.CharacterExtractor;

public class CharacterExtractorTextTest {
  private Vector displacement;

  private PDGraphicsState graphicsState;

  private CharacterExtractor characterExtractor;

  private PDPage page;

  @Before
  public void setup() {
    this.characterExtractor = new CharacterExtractor();
    this.displacement = new Vector(0, 0);
    this.graphicsState = new PDGraphicsState(new PDRectangle(100, 100));
    this.page = new PDPage(new PDRectangle(72 * 8.27f, 72 * 11.69f));
  }

  @Test
  public void testSpace() {
    PDFont font = PDType1Font.COURIER;
    Text position = characterExtractor.calculateStyleAndPosition(page, new Matrix(), new Matrix(),
        font, 33, " ", displacement, graphicsState, false);
    assertEquals(" ", position.toString());
  }

}
