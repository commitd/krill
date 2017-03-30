package io.committed.krill.extraction.tika.docx;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocxTablesTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "tables.docx";

  public DocxTablesTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n"
        + " <h3><a name=\"_Toc465796964\"></a>Tables</h3> \n"
        + " <p>Here is a simple text table:</p> \n" + " <table> \n" + "  <tbody> \n" + "   <tr> \n"
        + "    <td><p>1a</p> </td> \n" + "    <td><p>1b</p> </td> \n" + "    <td><p>1c</p> </td> \n"
        + "    <td><p>1d</p> </td> \n" + "   </tr> \n" + "   <tr> \n" + "    <td><p>2a</p> </td> \n"
        + "    <td><p>2b</p> </td> \n" + "    <td><p>2c</p> </td> \n" + "    <td><p>2d</p> </td> \n"
        + "   </tr> \n" + "   <tr> \n" + "    <td><p>3a</p> </td> \n" + "    <td><p>3b</p> </td> \n"
        + "    <td><p>3c</p> </td> \n" + "    <td><p>3d</p> </td> \n" + "   </tr> \n"
        + "  </tbody> \n" + " </table> \n" + " <p></p> \n"
        + " <p>They can have title rows and styles applied (in this case ‘Plain Table 3’):</p> \n"
        + " <table> \n" + "  <tbody> \n" + "   <tr> \n" + "    <td><p>A</p> </td> \n"
        + "    <td><p>B</p> </td> \n" + "    <td><p>C</p> </td> \n" + "    <td><p>D</p> </td> \n"
        + "   </tr> \n" + "   <tr> \n" + "    <td><p>1a</p> </td> \n" + "    <td><p>1b</p> </td> \n"
        + "    <td><p>1c</p> </td> \n" + "    <td><p>1d</p> </td> \n" + "   </tr> \n" + "   <tr> \n"
        + "    <td><p>2a</p> </td> \n" + "    <td><p>2b</p> </td> \n" + "    <td><p>2c</p> </td> \n"
        + "    <td><p>2d</p> </td> \n" + "   </tr> \n" + "   <tr> \n" + "    <td><p>3a</p> </td> \n"
        + "    <td><p>3b</p> </td> \n" + "    <td><p>3c</p> </td> \n" + "    <td><p>3d</p> </td> \n"
        + "   </tr> \n" + "  </tbody> \n" + " </table> \n" + " <p></p> \n"
        + " <p>Tables can contain almost any element, as with HTML, </p> \n" + " <table> \n"
        + "  <tbody> \n" + "   <tr> \n" + "    <td> \n" + "     <ul> \n" + "      <li>One</li> \n"
        + "      <li>Two</li> \n" + "      <li>Three</li> \n" + "     </ul> </td> \n"
        + "    <td> \n" + "     <table> \n" + "      <tbody> \n" + "       <tr> \n"
        + "        <td><p>Including</p> </td> \n" + "        <td><p>Nested</p> </td> \n"
        + "        <td><p>Tables</p> </td> \n" + "       </tr> \n" + "       <tr> \n"
        + "        <td><p>Why</p> </td> \n" + "        <td><p>Oh</p> </td> \n"
        + "        <td><p>Why</p> </td> \n" + "       </tr> \n" + "      </tbody> \n"
        + "     </table> <p></p> </td> \n" + "   </tr> \n" + "   <tr> \n" + "    <td> \n"
        + "     <ul> \n" + "      <li>Alpha</li> \n" + "      <li>Beta</li> \n"
        + "     </ul> </td> \n"
        + "    <td><p>And images, SmartArt, etc. Here is a Shape (a Cloud) containing some text – because every technical document should mention clouds.</p><p>It is just someone elses computer!</p> <p></p> </td> \n"
        + "   </tr> \n" + "  </tbody> \n" + " </table> \n" + "</main>");
  }



}
