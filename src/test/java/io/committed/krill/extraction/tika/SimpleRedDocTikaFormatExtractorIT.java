package io.committed.krill.extraction.tika;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class SimpleRedDocTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple-red.doc";

  public SimpleRedDocTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    // NOTE: Style not coming through

    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <h1>Hello, World! </h1> \n"
            + " <h1>Saluton, Mondo!</h1> \n"
            + " <p>Hello, World! </p> \n"
            + "</main>");
  }
}
