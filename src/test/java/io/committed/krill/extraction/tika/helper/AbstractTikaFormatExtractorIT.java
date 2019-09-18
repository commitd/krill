package io.committed.krill.extraction.tika.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import io.committed.krill.extraction.Extraction;
import io.committed.krill.extraction.exception.ExtractionException;
import io.committed.krill.extraction.impl.DefaultExtraction;
import io.committed.krill.extraction.tika.TikaFormatExtractor;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractTikaFormatExtractorIT {

  protected Extraction extraction;
  protected Document document;

  private final String resourceName;
  private final boolean generateTestCode;

  public AbstractTikaFormatExtractorIT(final String resourceName) {
    this(resourceName, false);
  }

  public AbstractTikaFormatExtractorIT(final String resourceName, final boolean generateTestCode) {
    this.resourceName = resourceName;
    this.generateTestCode = generateTestCode;
  }

  @Before
  public void setup() throws IOException, ExtractionException {
    final TikaFormatExtractor extractor = new TikaFormatExtractor();
    try (InputStream stream = this.getClass().getResourceAsStream(resourceName)) {
      extraction = extractor.parse(stream, resourceName);
      final Parser parser = Parser.htmlParser().setTrackErrors(1000);
      document = Jsoup.parse(extraction.getHtml(), "", parser);
      if (generateTestCode) {
        HtmlTestCodeGenerator.generate(document);
        System.exit(1);
      }
    }
  }

  @Test
  public void testNonNullExtraction() {
    assertNotNull(extraction);
    assertNotNull(extraction.getHtml());
    assertNotNull(((DefaultExtraction) extraction).getMetadata());
    assertFalse(extraction.getHtml().isEmpty());
    assertNotNull(document);
  }

  protected void assertBody(final String expectedHtml) {
    final String html = document.body().html();
    assertThat(html).isEqualTo(expectedHtml);
  }
}
