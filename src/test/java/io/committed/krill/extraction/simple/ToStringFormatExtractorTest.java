package io.committed.krill.extraction.simple;

import static org.assertj.core.api.Assertions.assertThat;

import io.committed.krill.extraction.Extraction;
import io.committed.krill.extraction.exception.ExtractionException;
import io.committed.krill.extraction.simple.ToStringFormatExtractor;
import io.committed.krill.extraction.support.test.ThrowOnReadInputStream;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ToStringFormatExtractorTest {

  private static final String BODY = "body";

  private static final Charset UTF8 = StandardCharsets.UTF_8;

  private static final String TEST_STRING = "[aɪ kæn iːt glɑːs ænd ɪt dɐz nɒt hɜːt miː]";

  private ToStringFormatExtractor formatExtractor;

  @Before
  public void setup() {
    this.formatExtractor = new ToStringFormatExtractor(UTF8);
  }

  protected Document toDocument(final Extraction extraction) {
    return Jsoup.parse(extraction.getHtml());
  }

  @Test
  public void testStringIsReturned() throws IOException, ExtractionException {
    final Extraction extraction = makeExtraction();
    assertThat(toDocument(extraction).text()).isEqualTo(TEST_STRING);
  }

  @Test
  public void testSingleWholeBodyTagIsReturned() throws IOException, ExtractionException {
    final Extraction extraction = makeExtraction();
    final Document document = toDocument(extraction);
    assertThat(document.body().children().size()).isEqualTo(1);
    assertThat(document.body().child(0).tagName()).isEqualTo("pre");
  }

  @Test(expected = ExtractionException.class)
  public void testInvalidStream() throws IOException, ExtractionException {
    try (InputStream inputStream = new ThrowOnReadInputStream(null)) {
      formatExtractor.parse(inputStream, BODY);
    }
  }

  private Extraction makeExtraction() throws IOException, ExtractionException {
    try (InputStream inputStream = IOUtils.toInputStream(TEST_STRING, UTF8)) {
      return formatExtractor.parse(inputStream, BODY);
    }
  }
}
