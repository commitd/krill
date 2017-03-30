package io.committed.krill.extraction.tika.doc;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocFootnotesAndCommentsTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "footnotesandcomments.doc";

  public DocFootnotesAndCommentsTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n"
        + " <p>This sentence has a footnote before the full stop .</p> \n"
        + " <p>This sentence has a comment before the full stop . </p> \n"
        + " <p>This sentence has a footnote before the full stop, too. </p> \n"
        + " <details class=\"footnode\">\n" + "   This is the first footnote. \n" + " </details> \n"
        + " <details class=\"footnode\">\n" + "   This, too, is a footnote. \n" + " </details> \n"
        + " <p> </p> \n" + " <details class=\"footnode\">\n" + "   This is a comment. \n"
        + " </details> \n" + " <p> </p> \n" + "</main>");
  }

}
