package io.committed.krill.extraction.tika.processors;

import org.apache.tika.metadata.Metadata;
import org.jsoup.nodes.Document;

/**
 * A simple plain text document parser which places all content under a pre tag.
 * <p>
 * Uses the same main class=Document structure as {@link WordFormatProcessor}.
 * </p>
 */
public class RtfFormatProcessor extends AbstractJsoupFormatProcessor {

  @Override
  public Document process(final Metadata metadata, final Document document) {

    // Everything sits under a single p tag...
    wrapChildrenOfBodyInTag(document, "<main class=\"Document\"></main>");

    return document;
  }

}
