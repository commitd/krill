package io.committed.krill.extraction.tika.processors;

import org.apache.tika.metadata.Metadata;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * A simple plain text document parser which places all content under a pre tag.
 * <p>
 * This basically replicates the original Tika to text approach but uses a sane pre tag rather than
 * p.
 * </p>
 * <p>
 * Because everything is places under s single pre by Tika, we do not add the main class="Document"
 * as an indication that is not structured.
 * </p>
 */
public class TextFormatProcessor extends AbstractJsoupFormatProcessor {

  /*
   * (non-Javadoc)
   * 
   * @see
   * io.committed.krill.extraction.tika.processors.AbstractJsoupFormatProcessor#process(org.apache.
   * tika.metadata.Metadata, org.jsoup.nodes.Document)
   */
  @Override
  public Document process(final Metadata metadata, final Document document) {

    // If we have 1 node, it a p, it contains only text nodes, then treat it as pre
    if (document.body().children().size() == 1) {
      final Elements paragraphs = document.select("body > p");
      if (paragraphs.size() == 1 && isAllTextNodes(paragraphs.first())) {
        paragraphs.first().tagName("pre");
      }
    }
    return document;
  }

  private boolean isAllTextNodes(final Element parent) {
    final boolean allText = true;
    for (final Node node : parent.childNodes()) {
      if (!(node instanceof TextNode)) {
        return false;
      }
    }
    return allText;
  }

}
