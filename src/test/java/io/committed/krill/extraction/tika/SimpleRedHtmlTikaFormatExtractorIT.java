package io.committed.krill.extraction.tika;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class SimpleRedHtmlTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple-red.html";

  public SimpleRedHtmlTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n" + " <h1>Hello, World!</h1> \n"
        + " <h1 class=\"red\">Saluton, mondo!</h1> \n" + " <p>Hello, World!</p> \n" + "</main>");
  }

}
