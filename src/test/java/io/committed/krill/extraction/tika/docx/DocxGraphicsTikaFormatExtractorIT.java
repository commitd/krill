package io.committed.krill.extraction.tika.docx;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class DocxGraphicsTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "graphics.docx";

  public DocxGraphicsTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <p>This is a simple document with some vector graphics and a bitmap image.</p> \n"
            + " <p></p> \n"
            + " <p>Here are the vector graphics:</p> \n"
            + " <p></p> \n"
            + " <p></p> \n"
            + " <p></p> \n"
            + " <p></p> \n"
            + " <p></p> \n"
            + " <p></p> \n"
            + " <p></p> \n"
            + " <p>And here is the bitmap image:</p> \n"
            + " <p></p> \n"
            + " <p><img src=\"embedded:image1.tiff\" alt=\"\"></p> \n"
            + " <p></p> \n"
            + " <p>Finally, this bitmap image has a caption:</p> \n"
            + " <p></p> \n"
            + " <p><img src=\"embedded:image1.tiff\" alt=\"\"></p> \n"
            + " <figcaption>\n"
            + "   Figure 1 \n"
            + " </figcaption> \n"
            + "</main>");
  }
}
