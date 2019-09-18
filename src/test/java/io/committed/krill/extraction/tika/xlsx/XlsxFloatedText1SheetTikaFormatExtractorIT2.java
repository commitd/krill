package io.committed.krill.extraction.tika.xlsx;

import io.committed.krill.extraction.tika.helper.AbstractTikaSpreadsheetFormatExtractorIT;
import org.junit.Test;

public class XlsxFloatedText1SheetTikaFormatExtractorIT2
    extends AbstractTikaSpreadsheetFormatExtractorIT {

  private static final String RESOURCE_NAME = "floatedtext-1sheet.xlsx";

  public XlsxFloatedText1SheetTikaFormatExtractorIT2() {
    super(RESOURCE_NAME);
  }

  @Test
  /*
   * Will fail due to previous reliance on Apache Tika fork. This should pass in the next version of Tika
   */
  public void testBody() {
    assertBody("" + "<main class=\"SpreadSheet\"> \n" + " <article class=\"Sheet\"> \n"
        + "  <h1>Sheet1</h1> \n" + "  <table> \n" + "   <tbody> \n" + "    <tr> \n"
        + "     <td>1a</td> \n" + "     <td>1b</td> \n" + "     <td>1c</td> \n" + "    </tr> \n"
        + "    <tr> \n" + "     <td>2a</td> \n" + "     <td>2b</td> \n" + "     <td>2c</td> \n"
        + "    </tr> \n" + "    <tr> \n" + "     <td>3a</td> \n" + "     <td>3b</td> \n"
        + "     <td>3c</td> \n" + "    </tr> \n" + "   </tbody> \n" + "  </table> \n"
        + "  <p>This is a text box beneath the table</p> \n" + " </article> \n" + "</main>");
  }

}
