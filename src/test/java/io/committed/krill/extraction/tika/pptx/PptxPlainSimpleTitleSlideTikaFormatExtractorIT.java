package io.committed.krill.extraction.tika.pptx;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class PptxPlainSimpleTitleSlideTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "plain-simple-titleslideonly.pptx";

  public PptxPlainSimpleTitleSlideTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"SlideShow\"> \n" + " <article class=\"Slide\"> \n"
        + "  <section class=\"\"> \n" + "   <p>Title</p> \n" + "   <p>Subtitle</p> \n"
        + "  </section> \n" + "  <aside> \n" + "   <p>Notes</p> \n" + "   <p>1</p> \n"
        + "  </aside> \n" + " </article> \n" + "</main>");
  }
}
