package io.committed.krill.extraction.tika;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class SimpleRtfTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple.rtf";

  public SimpleRtfTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  // Tika does a very poor job here

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <p>A sample document saved as RTF.<br> <br> With style of <b>bold</b> and <i>italics</i> and underline.<br> <br> It also has a list:<br> <br> with bullets<br> and another<br> <br> And also a numbered<br> <br> which <br> has <br> many <br> styles<br> <br> </p> \n"
            + "</main>");
  }
}
