package io.committed.krill.extraction.tika.doc;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocHeaderContentFooterTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "header-content-footer.doc";

  public DocHeaderContentFooterTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n" + " <header> \n"
        + "  <p>This is the header </p> \n" + " </header> \n" + " <p>This is the content</p> \n"
        + " <footer> \n" + "  <p>This is the footer </p> \n" + " </footer> \n" + "</main>");
  }

}
