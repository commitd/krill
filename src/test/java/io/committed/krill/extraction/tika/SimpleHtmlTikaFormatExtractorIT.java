package io.committed.krill.extraction.tika;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Test;

import io.committed.krill.extraction.impl.DefaultExtraction;
import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class SimpleHtmlTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple.html";

  public SimpleHtmlTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n" + " <h1>Hello, World!</h1> \n"
        + " <p>Hello, World!</p> \n" + "</main>");
  }

  @Test
  public void testHasHeadMeta() {
    assertThat(document.head().select("meta[name=example]").first().attr("content"))
        .isEqualTo("test");
  }

  @Test
  public void testHasMetadata() {
    final Collection<String> collection =
        ((DefaultExtraction) extraction).getMetadata().get("example");
    assertThat(collection).contains("test");
  }

}
