package io.committed.krill.extraction.tika.docx;

import org.junit.Ignore;
import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocxMultipageHeaderFooterTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "multipage-header-footer.docx";

  public DocxMultipageHeaderFooterTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  /*
   * Will fail due to previous reliance on Apache Tika fork. This should pass in the next version of Tika
   */
  @Ignore
  public void testBody() {
    assertBody("" +
        "<main class=\"Document\"> \n" +
        " <header> \n" +
        "  <p></p> \n" +
        " </header> \n" +
        " <header> \n" +
        "  <p></p> \n" +
        " </header> \n" +
        " <header> \n" +
        "  <p>This is a heading <img src=\"embedded:image1.tiff\" alt=\"\"></p> \n" +
        " </header> \n" +
        " <p>This is the first page of a multipage document with headers and footers.</p> \n" +
        " <p></p> \n" +
        " <hr class=\"pagebreak\">  \n" +
        " <p>This is the second page.</p> \n" +
        " <hr class=\"pagebreak\">  \n" +
        " <p>This the third, and final, page.</p> \n" +
        " <footer> \n" +
        "  <p></p> \n" +
        " </footer> \n" +
        " <footer> \n" +
        "  <p></p> \n" +
        " </footer> \n" +
        " <footer> \n" +
        "  <p>Page 1 of 1</p> \n" +
        "  <p></p> \n" +
        " </footer> \n" +
        "</main>");
  }

}
