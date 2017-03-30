package io.committed.krill.extraction.tika.xlsx;

import org.junit.Ignore;
import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaSpreadsheetFormatExtractorIT;

public class XlsxFloatedBarChart1SheetTikaFormatExtractorIT
    extends AbstractTikaSpreadsheetFormatExtractorIT {

  private static final String RESOURCE_NAME = "floatedbarchart-1sheet.xlsx";

  public XlsxFloatedBarChart1SheetTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  /*
   * Will fail due to previous reliance on Apache Tika fork. This should pass in the next version of Tika
   */
  @Ignore
  public void testBody() {
    assertBody("" + "<main class=\"SpreadSheet\"> \n" + " <article class=\"Sheet\"> \n"
        + "  <h1>Sheet1</h1> \n" + "  <table> \n" + "   <tbody> \n" + "    <tr> \n"
        + "     <td>A</td> \n" + "     <td>B</td> \n" + "    </tr> \n" + "    <tr> \n"
        + "     <td>Alpha</td> \n" + "     <td>5</td> \n" + "    </tr> \n" + "    <tr> \n"
        + "     <td>Beta</td> \n" + "     <td>4</td> \n" + "    </tr> \n" + "    <tr> \n"
        + "     <td>Charlie</td> \n" + "     <td>9</td> \n" + "    </tr> \n" + "    <tr> \n"
        + "     <td>Delta</td> \n" + "     <td>1</td> \n" + "    </tr> \n" + "   </tbody> \n"
        + "  </table> \n" + " </article> \n" + "</main>");
  }
}
