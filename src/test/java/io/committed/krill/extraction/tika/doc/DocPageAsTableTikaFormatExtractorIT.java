package io.committed.krill.extraction.tika.doc;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class DocPageAsTableTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "pageastable.doc";

  public DocPageAsTableTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    // NOTE: Table in table does not work

    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <table> \n"
            + "  <tbody> \n"
            + "   <tr> \n"
            + "    <td><p>The whole page is a table.</p> </td> \n"
            + "    <td><p>This is some simple text opposite.</p> </td> \n"
            + "   </tr> \n"
            + "   <tr> \n"
            + "    <td><p>Tables can contain tables.</p> </td> \n"
            + "    <td><p>One </p> <p>A </p> <p>Two </p> <p>B </p> <p></p> </td> \n"
            + "   </tr> \n"
            + "   <tr> \n"
            + "    <td><p>Or lists</p> </td> \n"
            + "    <td> \n"
            + "     <ul> \n"
            + "      <li>1. Like </li> \n"
            + "      <li>2. This </li> \n"
            + "      <li>3. Ones</li> \n"
            + "     </ul> </td> \n"
            + "   </tr> \n"
            + "  </tbody> \n"
            + " </table> \n"
            + "</main>");
  }
}
