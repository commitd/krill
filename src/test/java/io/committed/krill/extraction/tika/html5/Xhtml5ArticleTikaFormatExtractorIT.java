package io.committed.krill.extraction.tika.html5;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class Xhtml5ArticleTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "xhtml5-article.html";

  public Xhtml5ArticleTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <article> \n"
            + "  <header> \n"
            + "   <h1>Comprehensive HTML5 document</h1> \n"
            + "  </header> \n"
            + "  <p>This document contains comprehensive coverage of HTML5 tags and, to a lesser extent their attributes. It is intended as a demonstration of the document structures we expect to be able to annotate.</p> \n"
            + "  <footer> \n"
            + "   <p>This document does not exhaustively document HTML5, but it should cover the major structural elements commonly seen in the wild. It notably does not include form elements.</p> \n"
            + "  </footer> \n"
            + " </article> \n"
            + "</main>");
  }
}
