package io.committed.krill.extraction.tika.html5;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class Html5ImagesTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "html5-images.html";

  public Html5ImagesTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <section id=\"images\"> \n"
            + "  <h2>Images</h2> \n"
            + "  <p>Finally, we come to images. For the sake of containment, all images are a single black pixel GIF encoded inline using base64, but height and width have been set to 10px to make it big enough to hover over.</p> \n"
            + "  <h3>Plain image</h3> \n"
            + "  <img alt=\"alt attribute text for 1 pixel black image\" src=\"data:image/gif;base64,R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs\" width=\"10px\" height=\"10px\"> \n"
            + "  <h3>Figure</h3> \n"
            + "  <figure> \n"
            + "   <img alt=\"alt attribute text for 1 pixel black image\" src=\"data:image/gif;base64,R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs\" width=\"10px\" height=\"10px\"> \n"
            + "  </figure> \n"
            + "  <h3>Figure with figcaption</h3> \n"
            + "  <figure> \n"
            + "   <img alt=\"alt attribute text for 1 pixel black image\" src=\"data:image/gif;base64,R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs\" width=\"10px\" height=\"10px\"> \n"
            + "   <figcaption>\n"
            + "     Caption for 1 pixel black image \n"
            + "   </figcaption> \n"
            + "  </figure> \n"
            + " </section> \n"
            + "</main>");
  }
}
