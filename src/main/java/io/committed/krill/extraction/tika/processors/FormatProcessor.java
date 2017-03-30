package io.committed.krill.extraction.tika.processors;

import io.committed.krill.extraction.tika.TikaFormatExtractor;
import org.apache.tika.metadata.Metadata;

/**
 * Format specific Processor.
 *
 * <p>
 * Tika attempts to get text content from many document sources, but it does little to standardise
 * their output. Furthermore the results of Tika's processing can be very noisy, with excessive tags
 * derived from semnatic, syntax or stylistic extractions.
 * </p>
 *
 * <p>
 * The role of a format processor is to convert the HTML output of Tika into a cleaner, more
 * meaningful and standardised HTML5 output. Examples might be to convert tags such as
 * 'div.slide-content' to be a slide, or even simplify identify the entire document as a slideshow.
 * </p>
 *
 * <p>
 * Implementations of this should be plugin into {@link TikaFormatExtractor} through addProcessor
 * function.
 * </p>
 *
 * <p>
 * NOTE since the majority of work in a format processor is HTML manipulation most implementation
 * will wish to derive from {@link AbstractJsoupFormatProcessor} to use JSoup's power in DOM
 * manipulation.
 * </p>
 */
@FunctionalInterface
public interface FormatProcessor {

  /**
   * Process the html provided and return html.
   *
   *
   * @param metadata
   *          the metadata
   * @param html
   *          the html input
   * @return the processed html as ouput
   */
  String process(Metadata metadata, String html);
}
