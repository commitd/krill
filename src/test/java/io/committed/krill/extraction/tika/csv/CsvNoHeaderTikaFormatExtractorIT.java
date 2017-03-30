package io.committed.krill.extraction.tika.csv;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class CsvNoHeaderTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "noheader.csv";

  public CsvNoHeaderTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"SpreadSheet\"> \n" + " <article class=\"Sheet\"> \n"
        + "  <table> \n" + "   <tbody> \n" + "    <tr> \n" + "     <td>1a</td> \n"
        + "     <td>1</td> \n" + "     <td>1</td> \n" + "    </tr> \n" + "    <tr> \n"
        + "     <td>2a</td> \n" + "     <td>2</td> \n" + "     <td>2</td> \n" + "    </tr> \n"
        + "    <tr> \n" + "     <td>3a</td> \n" + "     <td>3</td> \n" + "     <td>3</td> \n"
        + "    </tr> \n" + "   </tbody> \n" + "  </table> \n" + " </article> \n" + "</main>");
  }

}
