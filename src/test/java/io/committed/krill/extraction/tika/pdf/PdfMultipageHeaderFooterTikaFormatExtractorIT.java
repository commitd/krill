package io.committed.krill.extraction.tika.pdf;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class PdfMultipageHeaderFooterTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  public PdfMultipageHeaderFooterTikaFormatExtractorIT() {
    super("multipage-header-footer.pdf");
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <article class=\"page\"> \n"
            + "  <header> \n"
            + "   <table> \n"
            + "    <tbody> \n"
            + "     <tr></tr> \n"
            + "    </tbody> \n"
            + "   </table> \n"
            + "   <p style=\"font-family:Calibri;font-size:12.0\" class=\"header\">This is a heading</p> \n"
            + "   <img> \n"
            + "  </header> \n"
            + "  <p style=\"font-family:Calibri;font-size:12.0\">This is the first page of a multipage document with headers and footers.</p> \n"
            + "  <footer> \n"
            + "   <p style=\"font-family:Calibri;font-size:12.0pt;color:#4472c4\" class=\"footer\">Page 1 of 3</p> \n"
            + "  </footer> \n"
            + " </article> \n"
            + " <hr class=\"pagebreak\"> \n"
            + " <article class=\"page\"> \n"
            + "  <header> \n"
            + "   <table> \n"
            + "    <tbody> \n"
            + "     <tr></tr> \n"
            + "    </tbody> \n"
            + "   </table> \n"
            + "   <p style=\"font-family:Calibri;font-size:12.0\" class=\"header\">This is a heading</p> \n"
            + "   <img> \n"
            + "  </header> \n"
            + "  <p style=\"font-family:Calibri;font-size:12.0\">This is the second page.</p> \n"
            + "  <footer> \n"
            + "   <p style=\"font-family:Calibri;font-size:12.0pt;color:#4472c4\" class=\"footer\">Page 2 of 3</p> \n"
            + "  </footer> \n"
            + " </article> \n"
            + " <hr class=\"pagebreak\"> \n"
            + " <article class=\"page\"> \n"
            + "  <header> \n"
            + "   <table> \n"
            + "    <tbody> \n"
            + "     <tr></tr> \n"
            + "    </tbody> \n"
            + "   </table> \n"
            + "   <p style=\"font-family:Calibri;font-size:12.0\" class=\"header\">This is a heading</p> \n"
            + "   <img> \n"
            + "  </header> \n"
            + "  <p style=\"font-family:Calibri;font-size:12.0\">This the third, and final, page.</p> \n"
            + "  <footer> \n"
            + "   <p style=\"font-family:Calibri;font-size:12.0pt;color:#4472c4\" class=\"footer\">Page 3 of 3</p> \n"
            + "  </footer> \n"
            + " </article> \n"
            + "</main>");
  }
}
