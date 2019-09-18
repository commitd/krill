package io.committed.krill.extraction.tika;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class SimplePdfTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple.pdf";

  public SimplePdfTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <article class=\"page\"> \n"
            + "  <table> \n"
            + "   <tbody> \n"
            + "    <tr></tr> \n"
            + "   </tbody> \n"
            + "  </table> \n"
            + "  <h1 style=\"font-family:Calibri-Light;font-size:16.0pt;color:#2e74b5\" class=\"heading\">Hello, World!</h1> \n"
            + "  <p style=\"font-family:Calibri;font-size:12.0\">Hello, World!</p> \n"
            + " </article> \n"
            + "</main>");
  }
}
