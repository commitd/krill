package io.committed.krill.extraction;

/**
 * An extraction represents the text and structure annotations that have been extracted from a
 * source.
 */
public interface Extraction {

  /**
   * Returns the html that was extracted.
   *
   * @return a {@link String} of the html (html5).
   */
  String getHtml();

}
