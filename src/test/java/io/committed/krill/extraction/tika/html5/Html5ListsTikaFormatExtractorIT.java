package io.committed.krill.extraction.tika.html5;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class Html5ListsTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "html5-lists.html";

  public Html5ListsTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <section> \n"
            + "  <h2>Lists</h2> \n"
            + "  <p>We now demonstrate some basic lists.</p> \n"
            + "  <h3>Ordered list</h3> \n"
            + "  <ol> \n"
            + "   <li>First item</li> \n"
            + "   <li>Second item</li> \n"
            + "   <li>Third item</li> \n"
            + "  </ol> \n"
            + "  <h3>Unordered list</h3> \n"
            + "  <ul> \n"
            + "   <li>First item</li> \n"
            + "   <li>Second item</li> \n"
            + "   <li>Third item</li> \n"
            + "  </ul> \n"
            + "  <h3>Definition list</h3> \n"
            + "  <dl> \n"
            + "   <dt>\n"
            + "     First title \n"
            + "   </dt> \n"
            + "   <dd>\n"
            + "     First description \n"
            + "   </dd> \n"
            + "   <dt>\n"
            + "     Second title \n"
            + "   </dt> \n"
            + "   <dd>\n"
            + "     Second description \n"
            + "   </dd> \n"
            + "   <dt>\n"
            + "     Third title \n"
            + "   </dt> \n"
            + "   <dd>\n"
            + "     Third description \n"
            + "   </dd> \n"
            + "  </dl> \n"
            + " </section> \n"
            + "</main>");
  }
}
