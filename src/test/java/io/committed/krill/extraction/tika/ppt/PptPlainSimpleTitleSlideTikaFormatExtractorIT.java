package io.committed.krill.extraction.tika.ppt;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class PptPlainSimpleTitleSlideTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "plain-simple-titleslideonly.ppt";

  public PptPlainSimpleTitleSlideTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"SlideShow\"> \n" + " <article class=\"Slide\"> \n"
        + "  <section> \n" + "   <p>Title</p> \n" + "   <p>Subtitle</p> \n" + "  </section> \n"
        + "  <aside> \n" + "   <p>Notes</p> \n" + "  </aside> \n" + " </article> \n" + "</main>");
  }

}
