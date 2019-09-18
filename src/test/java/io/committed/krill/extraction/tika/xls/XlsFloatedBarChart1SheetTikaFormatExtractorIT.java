package io.committed.krill.extraction.tika.xls;

import io.committed.krill.extraction.tika.helper.AbstractTikaSpreadsheetFormatExtractorIT;
import org.junit.Test;

public class XlsFloatedBarChart1SheetTikaFormatExtractorIT
    extends AbstractTikaSpreadsheetFormatExtractorIT {

  private static final String RESOURCE_NAME = "floatedbarchart-1sheet.xls";

  public XlsFloatedBarChart1SheetTikaFormatExtractorIT() {
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
            + "     <td>A</td> \n"
            + "     <td>B</td> \n"
            + "    </tr> \n"
            + "    <tr> \n"
            + "     <td>Alpha</td> \n"
            + "     <td>5</td> \n"
            + "    </tr> \n"
            + "    <tr> \n"
            + "     <td>Beta</td> \n"
            + "     <td>4</td> \n"
            + "    </tr> \n"
            + "    <tr> \n"
            + "     <td>Charlie</td> \n"
            + "     <td>9</td> \n"
            + "    </tr> \n"
            + "    <tr> \n"
            + "     <td>Delta</td> \n"
            + "     <td>1</td> \n"
            + "    </tr> \n"
            + "   </tbody> \n"
            + "  </table> \n"
            + " </article> \n"
            + " <article class=\"Sheet\"> \n"
            + "  <h1>Sheet1</h1> \n"
            + "  <table> \n"
            + "   <tbody> \n"
            + "    <tr> \n"
            + "     <td></td> \n"
            + "    </tr> \n"
            + "   </tbody> \n"
            + "  </table> \n"
            + "  <section>\n"
            + "    B \n"
            + "  </section> \n"
            + " </article> \n"
            + "</main>");
  }
}
