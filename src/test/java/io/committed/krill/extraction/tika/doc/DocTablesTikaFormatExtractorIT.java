package io.committed.krill.extraction.tika.doc;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocTablesTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "tables.doc";

  public DocTablesTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    // NOTE: Nested tables don't work

    assertBody("" + "<main class=\"Document\"> \n" + " <h3>Tables </h3> \n"
        + " <p>Here is a simple text table: </p> \n" + " <table> \n" + "  <tbody> \n" + "   <tr> \n"
        + "    <td><p>1a</p> </td> \n" + "    <td><p>1b</p> </td> \n" + "    <td><p>1c</p> </td> \n"
        + "    <td><p>1d</p> </td> \n" + "   </tr> \n" + "   <tr> \n" + "    <td><p>2a</p> </td> \n"
        + "    <td><p>2b</p> </td> \n" + "    <td><p>2c</p> </td> \n" + "    <td><p>2d</p> </td> \n"
        + "   </tr> \n" + "   <tr> \n" + "    <td><p>3a</p> </td> \n" + "    <td><p>3b</p> </td> \n"
        + "    <td><p>3c</p> </td> \n" + "    <td><p>3d</p> </td> \n" + "   </tr> \n"
        + "  </tbody> \n" + " </table> \n"
        + " <p>They can have title rows and styles applied (in this case ‘Plain Table 3’): </p> \n"
        + " <table> \n" + "  <tbody> \n" + "   <tr> \n" + "    <td><p><b>A</b></p> </td> \n"
        + "    <td><p><b>B</b></p> </td> \n" + "    <td><p><b>C</b></p> </td> \n"
        + "    <td><p><b>D</b></p> </td> \n" + "   </tr> \n" + "   <tr> \n"
        + "    <td><p>1a</p> </td> \n" + "    <td><p>1b</p> </td> \n" + "    <td><p>1c</p> </td> \n"
        + "    <td><p>1d</p> </td> \n" + "   </tr> \n" + "   <tr> \n" + "    <td><p>2a</p> </td> \n"
        + "    <td><p>2b</p> </td> \n" + "    <td><p>2c</p> </td> \n" + "    <td><p>2d</p> </td> \n"
        + "   </tr> \n" + "   <tr> \n" + "    <td><p>3a</p> </td> \n" + "    <td><p>3b</p> </td> \n"
        + "    <td><p>3c</p> </td> \n" + "    <td><p>3d</p> </td> \n" + "   </tr> \n"
        + "  </tbody> \n" + " </table> \n"
        + " <p>Tables can contain almost any element, as with HTML, </p> \n" + " <table> \n"
        + "  <tbody> \n" + "   <tr> \n" + "    <td> \n" + "     <ul> \n" + "      <li>One </li> \n"
        + "      <li>Two </li> \n" + "      <li>Three</li> \n" + "     </ul> </td> \n"
        + "    <td><p>Including </p> <p>Nested </p> <p>Tables </p> <p>Why </p> <p>Oh </p> <p>Why </p> <p></p> </td> \n"
        + "   </tr> \n" + "   <tr> \n" + "    <td> \n" + "     <ul> \n" + "      <li>Alpha </li> \n"
        + "      <li>Beta</li> \n" + "     </ul> </td> \n"
        + "    <td><p>And images, SmartArt, etc. Here is a Shape (a Cloud) containing some text – because every technical document should mention clouds.</p> </td> \n"
        + "   </tr> \n" + "  </tbody> \n" + " </table> \n"
        + " <p>It is just someone elses computer! </p> \n" + " <p> </p> \n" + " <p> </p> \n"
        + "</main>");
  }



}
