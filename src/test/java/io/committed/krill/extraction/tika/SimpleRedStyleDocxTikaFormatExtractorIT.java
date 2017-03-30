package io.committed.krill.extraction.tika;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class SimpleRedStyleDocxTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple-red-style.docx";

  public SimpleRedStyleDocxTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n" + " <h1>Hello, World!</h1> \n"
        + " <h1><a name=\"_GoBack\"></a>Saluton, Mondo!</h1> \n" + " <p>Hello, World!</p> \n"
        + " <p></p> \n" + "</main>");
  }



}
