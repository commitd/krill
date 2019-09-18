package io.committed.krill.extraction.tika;

import io.committed.krill.extraction.exception.ExtractionException;
import io.committed.krill.extraction.support.test.ThrowOnReadInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;

public class InvalidDataTikaFormatExtractorIT {

  private TikaFormatExtractor formatExtractor;

  @Before
  public void setup() {
    this.formatExtractor = new TikaFormatExtractor();
  }

  @Test(expected = ExtractionException.class)
  public void testInvalidStream() throws IOException, ExtractionException {
    try (InputStream inputStream = new ThrowOnReadInputStream(null)) {
      formatExtractor.parse(inputStream, "source");
    }
  }
}
