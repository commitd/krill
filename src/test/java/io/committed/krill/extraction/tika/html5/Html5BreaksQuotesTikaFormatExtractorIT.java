package io.committed.krill.extraction.tika.html5;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class Html5BreaksQuotesTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "html5-breaks-quotes.html";

  public Html5BreaksQuotesTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <section> \n"
            + "  <h2>Horizontal breaks, quotes, et al.</h2> \n"
            + "  <p>It can be sometimes desirable to insert a horizontal rule into the document. One could argue this is style best reserved for CSS, but others argue it is a structural element in its own right.</p> \n"
            + "  <hr> \n"
            + "  <p>It appears that structure won as this section has a different theme (HTML5 defines hr as a 'thematic break'). We demonstrate the different types of quote:</p> \n"
            + "  <h3>Citation</h3> \n"
            + "  <cite>This is a citation from an unknown source.</cite> \n"
            + "  <h3>Blockquote</h3> \n"
            + "  <blockquote cite=\"some source\"> \n"
            + "   <p>The quick brown fox jumped over the lazy dog 123456780 times.</p> \n"
            + "  </blockquote> \n"
            + "  <h3>Inline quotations</h3> \n"
            + "  <p>We can also define <q>Inline quotations, like this one</q> if we so wish.</p> \n"
            + " </section> \n"
            + "</main>");
  }
}
