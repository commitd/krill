package io.committed.krill.extraction.tika;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class SimpleDocTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple.doc";

  public SimpleDocTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n" + " <h1>Hello, World!</h1> \n"
        + " <p>Hello, World! </p> \n" + "</main>");
  }



}
