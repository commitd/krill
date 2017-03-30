package io.committed.krill.extraction.tika.ppt;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class PptPlainSimpleTitleAndSlidesTikaFormatExtractorIT
    extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "plain-simple-titleandslides.ppt";

  public PptPlainSimpleTitleAndSlidesTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"SlideShow\"> \n" + " <article class=\"Slide\"> \n"
        + "  <section> \n" + "   <p>Title</p> \n" + "   <p>Subtitle</p> \n" + "  </section> \n"
        + "  <aside> \n" + "   <p>Notes</p> \n" + "  </aside> \n" + " </article> \n"
        + " <article class=\"Slide\"> \n" + "  <section> \n" + "   <p>Heading</p> \n" + "   <ul> \n"
        + "    <li>First bullet point</li> \n" + "    <li>Second bullet point</li> \n"
        + "    <li>Nested bullet point</li> \n" + "   </ul> \n" + "  </section> \n" + "  <aside> \n"
        + "   <p>More notes</p> \n" + "  </aside> \n" + " </article> \n"
        + " <article class=\"Slide\"> \n" + "  <section> \n" + "   <p>Heading</p> \n" + "   <ul> \n"
        + "    <li>First section, first bullet</li> \n"
        + "    <li>First section, second bullet</li> \n" + "   </ul> \n" + "   <ul> \n"
        + "    <li>Second section, first bullet</li> \n"
        + "    <li>Second section, second bullet</li> \n" + "   </ul> \n" + "  </section> \n"
        + " </article> \n" + " <aside> \n" + "  <p>Notes</p> \n" + "  <p>More notes</p> \n"
        + " </aside> \n" + "</main>");
  }



}
