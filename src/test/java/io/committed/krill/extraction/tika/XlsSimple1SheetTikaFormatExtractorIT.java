package io.committed.krill.extraction.tika;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class XlsSimple1SheetTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple-1sheet.xls";

  public XlsSimple1SheetTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"SpreadSheet\"> \n"
            + " <article class=\"Sheet\"> \n"
            + "  <h1>Sheet1</h1> \n"
            + "  <table> \n"
            + "   <tbody> \n"
            + "    <tr> \n"
            + "     <td>1a</td> \n"
            + "     <td>1b</td> \n"
            + "     <td>1c</td> \n"
            + "    </tr> \n"
            + "    <tr> \n"
            + "     <td>2a</td> \n"
            + "     <td>2b</td> \n"
            + "     <td>2c</td> \n"
            + "    </tr> \n"
            + "    <tr> \n"
            + "     <td>3a</td> \n"
            + "     <td>3b</td> \n"
            + "     <td>3c</td> \n"
            + "    </tr> \n"
            + "   </tbody> \n"
            + "  </table> \n"
            + " </article> \n"
            + "</main>");
  }
}
