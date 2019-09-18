package io.committed.krill.extraction.tika.docx;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Ignore;
import org.junit.Test;

public class DocxHeaderContentFooterTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "header-content-footer.docx";

  public DocxHeaderContentFooterTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  /*
   * Will fail due to previous reliance on Apache Tika fork. This should pass in the next version of Tika
   */
  @Ignore
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <header> \n"
            + "  <p>This is the header</p> \n"
            + " </header> \n"
            + " <p>This is the content</p> \n"
            + " <footer> \n"
            + "  <p>This is the footer</p> \n"
            + " </footer> \n"
            + "</main>");
  }
}
