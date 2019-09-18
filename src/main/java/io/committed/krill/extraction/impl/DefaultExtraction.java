package io.committed.krill.extraction.impl;

import com.google.common.collect.Multimap;
import io.committed.krill.extraction.Extraction;

/**
 * Default implementation of the Extraction interface.
 *
 * <p>A simple immutable POJO.
 */
public class DefaultExtraction implements Extraction {

  private final String html;

  private final Multimap<String, String> metadata;

  /**
   * Instantiates a new default extraction.
   *
   * @param html the html
   * @param metadata the metadata
   */
  public DefaultExtraction(final String html, final Multimap<String, String> metadata) {
    this.html = html;
    this.metadata = metadata;
  }

  @Override
  public String getHtml() {
    return html;
  }

  /**
   * Get the metadata extracted by the parser.
   *
   * @return non-null parser metadata
   */
  public Multimap<String, String> getMetadata() {
    return metadata;
  }
}
