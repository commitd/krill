package io.committed.krill.extraction;

import io.committed.krill.extraction.exception.ExtractionException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Implementations of this interface process the given InputStream to extract the text and provide
 * html to describe the structure of the document.
 */
@FunctionalInterface
public interface FormatExtractor {

  /**
   * Process the given {@link InputStream} in the given {@link Charset} and return an {@link
   * Extraction} providing the text and associated structure.
   *
   * @param stream the {@link InputStream} to parse
   * @param source the name of the source data (typically the filename or path, including extension)
   * @return an {@link Extraction} object, representing the parsed document
   * @throws ExtractionException if an error occurs parsing the stream
   */
  Extraction parse(final InputStream stream, String source) throws ExtractionException;
}
