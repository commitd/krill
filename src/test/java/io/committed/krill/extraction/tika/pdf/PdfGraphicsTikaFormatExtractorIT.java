package io.committed.krill.extraction.tika.pdf;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class PdfGraphicsTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  public PdfGraphicsTikaFormatExtractorIT() {
    super("graphics.pdf");
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n" + " <article class=\"page\"> \n" + "  <table> \n"
        + "   <tbody> \n" + "    <tr></tr> \n" + "   </tbody> \n" + "  </table> \n"
        + "  <p style=\"font-family:Calibri;font-size:12.0\">This is a simple document with some vector graphics and a bitmap image.</p> \n"
        + "  <p style=\"font-family:Calibri;font-size:12.0\">Here are the vector graphics:</p> \n"
        + "  <p style=\"font-family:Calibri;font-size:12.0\">And here is the bitmap image:</p> \n"
        + "  <img> \n"
        + "  <p style=\"font-family:Calibri;font-size:12.0\">Finally, this bitmap image has a caption:</p> \n"
        + "  <img> \n"
        + "  <p style=\"font-family:Calibri-Italic;font-size:9.0pt;color:#44546a\"><i>Figure 1</i></p> \n"
        + " </article> \n" + "</main>");
  }

}
