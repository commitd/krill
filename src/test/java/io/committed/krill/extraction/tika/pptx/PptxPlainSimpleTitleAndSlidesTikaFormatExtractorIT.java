package io.committed.krill.extraction.tika.pptx;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class PptxPlainSimpleTitleAndSlidesTikaFormatExtractorIT
    extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "plain-simple-titleandslides.pptx";

  public PptxPlainSimpleTitleAndSlidesTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    // NOTE thate in aside (notes) the page number appears (if notes exist)
    // NOTE: That don't get lists

    assertBody("" + "<main class=\"SlideShow\"> \n" + " <article class=\"Slide\"> \n"
        + "  <section class=\"\"> \n" + "   <p>Title</p> \n" + "   <p>Subtitle</p> \n"
        + "  </section> \n" + "  <aside> \n" + "   <p>Notes</p> \n" + "   <p>1</p> \n"
        + "  </aside> \n" + " </article> \n" + " <article class=\"Slide\"> \n"
        + "  <section class=\"\"> \n" + "   <p>Heading</p> \n" + "   <p>First bullet point</p> \n"
        + "   <p>Second bullet point</p> \n" + "   <p>Nested bullet point</p> \n"
        + "  </section> \n" + "  <aside> \n" + "   <p>More notes</p> \n" + "   <p>2</p> \n"
        + "  </aside> \n" + " </article> \n" + " <article class=\"Slide\"> \n"
        + "  <section class=\"\"> \n" + "   <p>Heading</p> \n"
        + "   <p>First section, first bullet</p> \n" + "   <p>First section, second bullet</p> \n"
        + "   <p>Second section, first bullet</p> \n" + "   <p>Second section, second bullet</p> \n"
        + "  </section> \n" + " </article> \n" + "</main>");
  }



}
