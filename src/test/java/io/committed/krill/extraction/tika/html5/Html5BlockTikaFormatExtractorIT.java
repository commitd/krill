package io.committed.krill.extraction.tika.html5;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class Html5BlockTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "html5-block.html";

  public Html5BlockTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }



  @Test
  public void testBody() {
    assertBody(
        "" + "<main class=\"Document\"> \n" + " <section> \n" + "  <h2>Block level elements</h2> \n"
            + "  <p>First, a test of familiar HTML block level elements. We have already seen h1, h2 and p, so here are the rest of the headings:</p> \n"
            + "  <h3>Heading 3</h3> \n" + "  <h4>Heading 4</h4> \n" + "  <h5>Heading 5</h5> \n"
            + "  <h6>Heading 6</h6> \n" + "  <div>\n"
            + "    A div can be used to define a new block element without any specific semantics. \n"
            + "  </div> \n"
            + "  <p>Sometimes it is useful to include larger blocks of preformatted text, which is typically rendered using a monospace font. This is useful when attempting to create diagrams using text:</p> \n"
            + "  <pre>\n" + "    Top ---&amp;gt;        +----+\n" + "                    |    |\n"
            + " Bottom ---&amp;gt;        +----+\n" + "</pre> \n" + " </section> \n" + "</main>");
  }

}
