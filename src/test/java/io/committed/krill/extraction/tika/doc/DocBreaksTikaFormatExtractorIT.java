package io.committed.krill.extraction.tika.doc;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Ignore;
import org.junit.Test;

public class DocBreaksTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "breaks.doc";

  public DocBreaksTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  /*
   * Will fail due to previous reliance on Apache Tika fork. This should pass in the next version of Tika
   */
  @Ignore
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <header> \n"
            + "  <p></p> \n"
            + "  <p></p> \n"
            + "  <p>Header</p> \n"
            + " </header> \n"
            + " <h3>Breaks, quotes, et al. </h3> \n"
            + " <p>It is possible to quote text. The following text is in the ‘Quote’ style and was created by typing =rand(4,1) into the document: </p> \n"
            + " <blockquote>\n"
            + "   The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. \n"
            + " </blockquote> \n"
            + " <p>The =lorum() generator that is present on Windows isn’t available in Word for Mac 2016, so the following text has been quoted, using the Intense Quote style, from <a href=\"http://www.lipsum.com\"><u>www.lipsum.com</u></a> (the previous URL has been captured as a link and is in the ‘Hyperlink’ style). This is, apparently, “The standard Lorem Ipsum passage, used since the 1500s”: </p> \n"
            + " <blockquote>\n"
            + "   \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\" \n"
            + " </blockquote> \n"
            + " <p>We are able to divide a document into different sections. </p> \n"
            + " <hr class=\"pagebreak\"> \n"
            + " <p>There was a page break immediately before this sentence. </p> \n"
            + " <p>There is a bookmark immediately before this sentence (Bookmark1) and another immediately after it (BookMark2) but before the full stop. </p> \n"
            + " <p>We may decide to split a table over a page break, as below. In this case we have padded the table to the bottom of the page with empty lines, and split the table after row 1 using a Page Break. </p> \n"
            + " <table> \n"
            + "  <tbody> \n"
            + "   <tr> \n"
            + "    <td><p><b>A</b></p> </td> \n"
            + "    <td><p><b>B</b></p> </td> \n"
            + "    <td><p><b>C</b></p> </td> \n"
            + "    <td><p><b>D</b></p> </td> \n"
            + "   </tr> \n"
            + "   <tr> \n"
            + "    <td><p>1a</p> </td> \n"
            + "    <td><p>1b</p> </td> \n"
            + "    <td><p>1c</p> </td> \n"
            + "    <td><p>1d</p> </td> \n"
            + "   </tr> \n"
            + "  </tbody> \n"
            + " </table> \n"
            + " <hr class=\"pagebreak\"> \n"
            + " <table> \n"
            + "  <tbody> \n"
            + "   <tr> \n"
            + "    <td><p><b>2a</b></p> </td> \n"
            + "    <td><p><b>2b</b></p> </td> \n"
            + "    <td><p><b>2c</b></p> </td> \n"
            + "    <td><p><b>2d</b></p> </td> \n"
            + "   </tr> \n"
            + "   <tr> \n"
            + "    <td><p>3a</p> </td> \n"
            + "    <td><p>3b</p> </td> \n"
            + "    <td><p>3c</p> </td> \n"
            + "    <td><p>3d</p> </td> \n"
            + "   </tr> \n"
            + "  </tbody> \n"
            + " </table> \n"
            + " <hr class=\"pagebreak\"> \n"
            + " <p>Immediately after this sentence, and after the full stop, is a continuous section break. </p> \n"
            + " <hr class=\"pagebreak\"> \n"
            + " <p>However, the section break after this sentence, and after the full stop, will start a new page. </p> \n"
            + " <p>On this page we number the first three lines. </p> \n"
            + " <p>This is the second line. </p> \n"
            + " <p>This is third line. </p> \n"
            + " <hr class=\"pagebreak\"> \n"
            + " <p>We can put text into two columns. This seems like another good test for the =rand(x,y) test. </p> \n"
            + " <hr class=\"pagebreak\"> \n"
            + " <p>The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. </p> \n"
            + " <p>The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. </p> \n"
            + " <p>The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. </p> \n"
            + " <p> The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. </p> \n"
            + " <p>The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. </p> \n"
            + " <p>The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. </p> \n"
            + " <hr class=\"pagebreak\"> \n"
            + " <p>Images can have captions (in this case the image is a SmartArt diagram): </p> \n"
            + " <p><img src=\"embedded:image1.png\" alt=\"image1.png\"></p> \n"
            + " <figcaption>\n"
            + "   Figure 1: All too often Scrum becomes Scrummerfall or Water-Scrum-Fall, ie handing over to different teams \n"
            + " </figcaption> \n"
            + " <footer> \n"
            + "  <p>PAGE </p> \n"
            + "  <p>Page 1 of 1 </p> \n"
            + "  <p>Footer</p> \n"
            + " </footer> \n"
            + "</main>");
  }
}
