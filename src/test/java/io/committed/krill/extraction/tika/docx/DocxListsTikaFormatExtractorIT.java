package io.committed.krill.extraction.tika.docx;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocxListsTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "lists.docx";

  public DocxListsTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n"
        + " <h3><a name=\"_Toc465796963\"></a>Lists</h3> \n"
        + " <p>As one might expect, Word supports all manner of different lists.</p> \n"
        + " <p>Bullet lists</p> \n" + " <ul> \n" + "  <li>One</li> \n" + "  <li>Two</li> \n"
        + "  <li>Three</li> \n" + " </ul> \n" + " <p></p> \n" + " <ul> \n" + "  <li>1. One</li> \n"
        + "  <li>2. Two</li> \n" + "  <li>3. Three</li> \n" + " </ul> \n" + " <p></p> \n"
        + " <p>There are a number of lists styles defined in the list library. The main ones are demonstrated below (with the exception of that define headings / sections):</p> \n"
        + " <p></p> \n" + " <ul> \n" + "  <li>1) One</li> \n" + "  <li>2) Two</li> \n"
        + "  <li>3) Three</li> \n" + " </ul> \n" + " <p></p> \n" + " <ul> \n"
        + "  <li>1. One</li> \n" + "  <li>2. Two</li> \n" + "  <li>3. Three</li> \n" + " </ul> \n"
        + " <p></p> \n" + " <ul> \n" + "  <li>One</li> \n" + "  <li>Two</li> \n"
        + "  <li>Three</li> \n" + " </ul> \n" + " <p></p> \n" + " <ul> \n" + "  <li>A. One</li> \n"
        + "  <li>B. Two</li> \n" + "  <li>C. Three</li> \n" + " </ul> \n" + " <p></p> \n"
        + " <p>As with HTML, it is possible to nest lists, so as a single example here is a numbered list with lettered sub-lists:</p> \n"
        + " <ul> \n" + "  <li>1. One</li> \n" + "  <li>a. Alpha</li> \n" + "  <li>b. Beta</li> \n"
        + "  <li>2. Two</li> \n" + "  <li>a. Alpha</li> \n" + "  <li>b. Beta</li> \n" + " </ul> \n"
        + " <p></p> \n" + "</main>");
  }



}
