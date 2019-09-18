package io.committed.krill.extraction.pdfbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.text.CharacterExtractor;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class CharacterExtractorBaselineTest {

  private static final float PAGE_WIDTH_USERSPACE = 72 * 8.27f;

  private static final float PAGE_HEIGHT_USERSPACE = 72 * 11.69f;

  private static final float BASELINE_TRANSLATION_5 = 5.0f;

  private static final float BASELINE_TRANSLATION_0 = 0.0f;

  private static final String WHITESPACE = " \t";

  private static final String DECENDERS = "gpqy'";

  private static final String ASCENDERS = "bdfhijklt1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  private static final PDFont FONT = PDType1Font.HELVETICA;

  private static final Matrix MATRIX_5 = new Matrix(0, 0, 0, 0, 0, BASELINE_TRANSLATION_5);;

  private static final Matrix MATRIX_0 = new Matrix(0, 0, 0, 0, 0, BASELINE_TRANSLATION_0);;

  private Vector displacement;

  private PDGraphicsState graphicsState;

  private CharacterExtractor characterExtractor;

  private PDPage page;

  @Before
  public void setup() {
    this.characterExtractor = new CharacterExtractor();
    this.displacement = new Vector(0, 0);
    this.graphicsState = new PDGraphicsState(new PDRectangle(100, 100));
    this.page = new PDPage(new PDRectangle(PAGE_WIDTH_USERSPACE, PAGE_HEIGHT_USERSPACE));
  }

  @Test
  public void testWhitespaceTranslate5() {
    WHITESPACE.chars().forEach(c -> checkBaseline(c, MATRIX_5, PAGE_HEIGHT_USERSPACE - 5));
  }

  @Test
  public void testAscendersTranslate5() {
    ASCENDERS.chars().forEach(c -> checkBaseline(c, MATRIX_5, PAGE_HEIGHT_USERSPACE - 5));
  }

  @Test
  public void testDecendersTranslate5() {
    DECENDERS.chars().forEach(c -> checkBaseline(c, MATRIX_5, PAGE_HEIGHT_USERSPACE - 5));
  }

  @Test
  public void testWhitespaceTTranslate0() {
    WHITESPACE.chars().forEach(c -> checkBaseline(c, MATRIX_0, PAGE_HEIGHT_USERSPACE));
  }

  @Test
  public void testAscendersTranslate0() {
    ASCENDERS.chars().forEach(c -> checkBaseline(c, MATRIX_0, PAGE_HEIGHT_USERSPACE));
  }

  @Test
  public void testDecendersTranslate0() {
    DECENDERS.chars().forEach(c -> checkBaseline(c, MATRIX_0, PAGE_HEIGHT_USERSPACE));
  }

  private void checkBaseline(int codepoint, Matrix matrix, float expectedBaseline) {
    try {
      Text positionedStyledText =
          characterExtractor.calculateStyleAndPosition(
              page,
              matrix,
              matrix,
              FONT,
              codepoint,
              FONT.toUnicode(codepoint),
              displacement,
              graphicsState,
              false);
      assertThat(positionedStyledText).isNotNull();
      assertThat(positionedStyledText.getBaseline()).isEqualTo(expectedBaseline);
    } catch (IOException e) {
      fail("Failed to get character for codepoint " + codepoint, e);
    }
  }
}
