package io.committed.krill.extraction.tika.docx;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocxFootnotesAndCommentsTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "footnotesandcomments.docx";

  public DocxFootnotesAndCommentsTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody("" +
        "<main class=\"Document\"> \n" +
        " <p>This sentence has a footnote before the full stop. </p>\n" +
        " <details class=\"footnote\">\n" +
        "   This is the first footnote. \n" +
        " </details>\n" +
        " <p></p> \n" +
        " <p>This sentence has a comment before the full stop. Comment by James Fry: This is a comment.</p> \n"
        +
        " <p>This sentence has a footnote before the full stop, too. </p>\n" +
        " <details class=\"footnote\">\n" +
        "   This, too, is a footnote. \n" +
        " </details>\n" +
        " <p></p> \n" +
        "</main>");
  }



}
