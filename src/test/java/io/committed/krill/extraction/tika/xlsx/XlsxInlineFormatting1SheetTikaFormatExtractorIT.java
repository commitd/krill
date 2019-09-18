package io.committed.krill.extraction.tika.xlsx;

import io.committed.krill.extraction.tika.helper.AbstractTikaSpreadsheetFormatExtractorIT;
import org.junit.Test;

public class XlsxInlineFormatting1SheetTikaFormatExtractorIT
    extends AbstractTikaSpreadsheetFormatExtractorIT {

  private static final String RESOURCE_NAME = "inlineformatting-1sheet.xlsx";

  public XlsxInlineFormatting1SheetTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  /*
   * Will fail due to previous reliance on Apache Tika fork. This should pass in the next version of Tika
   */
  public void testBody() {
    assertBody("" + "<main class=\"SpreadSheet\"> \n" + " <article class=\"Sheet\"> \n"
        + "  <h1>Sheet1</h1> \n" + "  <table> \n" + "   <tbody> \n" + "    <tr> \n"
        + "     <td>A</td> \n" + "     <td>B</td> \n" + "     <td>C</td> \n" + "    </tr> \n"
        + "    <tr> \n" + "     <td>1a</td> \n" + "     <td>1b</td> \n" + "     <td>1c</td> \n"
        + "    </tr> \n" + "    <tr> \n" + "     <td>2a</td> \n" + "     <td>2b</td> \n"
        + "     <td>2c</td> \n" + "    </tr> \n" + "    <tr> \n" + "     <td>3a</td> \n"
        + "     <td>3b</td> \n" + "     <td>3c</td> \n" + "    </tr> \n" + "   </tbody> \n"
        + "  </table> \n" + " </article> \n" + "</main>");
  }

}
