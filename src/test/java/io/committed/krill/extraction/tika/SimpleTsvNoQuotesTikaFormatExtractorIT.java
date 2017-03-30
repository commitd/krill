package io.committed.krill.extraction.tika;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class SimpleTsvNoQuotesTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple.tsv";

  public SimpleTsvNoQuotesTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    assertBody(
        "" + "<main class=\"SpreadSheet\"> \n" + " <article class=\"Sheet\"> \n" + "  <table> \n"
            + "   <tbody> \n" + "    <tr> \n" + "     <th>A</th> \n" + "     <th>B</th> \n"
            + "     <th>C</th> \n" + "    </tr> \n" + "    <tr> \n" + "     <td>1a</td> \n"
            + "     <td>1b</td> \n" + "     <td>1</td> \n" + "    </tr> \n" + "    <tr> \n"
            + "     <td>2a</td> \n" + "     <td>2b</td> \n" + "     <td>2</td> \n" + "    </tr> \n"
            + "    <tr> \n" + "     <td>3a</td> \n" + "     <td>3b</td> \n" + "     <td>3</td> \n"
            + "    </tr> \n" + "   </tbody> \n" + "  </table> \n" + " </article> \n" + "</main>");
  }



}
