package io.committed.krill.extraction.tika.docx;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocxPageAsTableTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "pageastable.docx";

  public DocxPageAsTableTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n" + " <table> \n" + "  <tbody> \n" + "   <tr> \n"
        + "    <td><p>The whole page is a table.</p> </td> \n"
        + "    <td><p>This is some simple text opposite.</p> </td> \n" + "   </tr> \n"
        + "   <tr> \n" + "    <td><p>Tables can contain tables.</p> </td> \n" + "    <td> \n"
        + "     <table> \n" + "      <tbody> \n" + "       <tr> \n"
        + "        <td><p>One</p> </td> \n" + "        <td><p>A</p> </td> \n" + "       </tr> \n"
        + "       <tr> \n" + "        <td><p>Two</p> </td> \n" + "        <td><p>B</p> </td> \n"
        + "       </tr> \n" + "      </tbody> \n" + "     </table> <p></p> </td> \n" + "   </tr> \n"
        + "   <tr> \n" + "    <td><p>Or lists</p> </td> \n" + "    <td> \n" + "     <ul> \n"
        + "      <li>1. Like</li> \n" + "      <li>2. This</li> \n"
        + "      <li>3. <a name=\"_GoBack\"></a>Ones</li> \n" + "     </ul> </td> \n"
        + "   </tr> \n" + "  </tbody> \n" + " </table> \n" + " <p></p> \n" + "</main>");
  }



}
