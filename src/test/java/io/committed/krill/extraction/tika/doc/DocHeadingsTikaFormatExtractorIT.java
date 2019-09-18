package io.committed.krill.extraction.tika.doc;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class DocHeadingsTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "headings.doc";

  public DocHeadingsTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <h3>Headings </h3> \n"
            + " <p>A document may contain a number of headings using the default styles on the Microsoft Word toolbar. We have seen the heading levels one to three already, so here are the rest of the standard headings: </p> \n"
            + " <h4>Heading 4 </h4> \n"
            + " <h5>Heading 5 </h5> \n"
            + " <h6>Heading 6 </h6> \n"
            + " <h6>Heading 7 </h6> \n"
            + " <h6>Heading 8 </h6> \n"
            + " <h6>Heading 9</h6> \n"
            + " <h1>Title </h1> \n"
            + " <h2>Subtitle </h2> \n"
            + " <p><b><i>Book Title </i></b></p> \n"
            + "</main>");
  }
}
