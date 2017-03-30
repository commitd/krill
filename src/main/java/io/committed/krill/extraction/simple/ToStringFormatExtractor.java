package io.committed.krill.extraction.simple;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import io.committed.krill.extraction.Extraction;
import io.committed.krill.extraction.FormatExtractor;
import io.committed.krill.extraction.exception.ExtractionException;
import io.committed.krill.extraction.impl.DefaultExtraction;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * A naive implementation of {@link FormatExtractor} which does not attempt to extract any structure
 * from the given stream, simply converting the {@link InputStream} to a HTML String using the
 * specified {@link Charset}.
 * <p>
 * The String is wrapped in a pre tag (under the html body).
 * </p>
 */
public class ToStringFormatExtractor implements FormatExtractor {

  private static final Multimap<String, String> EMPTY = LinkedListMultimap.create(0);

  private final Charset charset;

  /**
   * Instantiates a new to string format extractor.
   *
   * @param charset
   *          the charset to use
   */
  public ToStringFormatExtractor(final Charset charset) {
    this.charset = charset;
  }

  @Override
  public Extraction parse(final InputStream stream, final String source)
      throws ExtractionException {
    try {
      final String body = IOUtils.toString(stream, charset);
      final String html = String.format("<html><body><pre>%s</pre></body></html>", body);
      return new DefaultExtraction(html, EMPTY);
    } catch (final IOException exception) {
      throw new ExtractionException("Unable to read stream as " + charset, exception);
    }
  }

}
