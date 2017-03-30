package io.committed.krill.extraction.tika.processors;

import io.committed.krill.extraction.tika.NoHeadTagInBodyContentHandler;
import io.committed.krill.extraction.tika.parsers.JSoupHtmlParser;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.jsoup.nodes.Document;

/**
 * Format processor for HTML.
 * <p>
 * Wraps a HTML document as it it where a DOC/DOC see {@link WordFormatProcessor} for the structure.
 * </p>
 * <p>
 * Also converts textarea to pre, in case additional data is stored in that tag.
 * </p>
 * <p>
 * Note that the majority of clean up of of HTML has already occured in {@link XHTMLContentHandler},
 * {@link NoHeadTagInBodyContentHandler}, {@link ToHTMLContentHandler} and {@link JSoupHtmlParser}.
 * </p>
 */
public class HtmlFormatProcessor extends AbstractJsoupFormatProcessor {

  @Override
  public Document process(final Metadata metadata, final Document document) {

    // This must be something to do with the XHTMLContentHandler / HTMLContentHandler interaction
    // but...
    // get <html><body><html><body>... so remove that
    document.select("body html").unwrap();
    document.select("body body").unwrap();

    // Treat any pure html as a document
    wrapChildrenOfBodyInTag(document, "<main class=\"Document\"></main>");

    // Convert text area to pre!
    // Could do this with other form values... unclear if it has an validity but we know the
    // textarea does
    document.select("textarea").tagName("pre");

    // Remove (what was) &nbsp; from any doc, replacing with a standard space
    document.select("*").forEach(e -> {
      if (e.ownText().equalsIgnoreCase("&amp;nbsp;") || e.ownText().equalsIgnoreCase("&nbsp;")) {
        e.text(" ");
      }
    });

    return document;
  }
}
