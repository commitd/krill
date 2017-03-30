package io.committed.krill.extraction.tika;

import com.google.common.collect.Sets;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.util.Set;

/**
 * Removes tags which should not appear in the body (ie not be converted to text).
 * <p>
 * The combination of XHTMLContentHandler and ToHTMLContentHandler produces metadata in both the
 * body and the head, so this strips that out of the body. Also it will prevent style and script
 * tags from appearing in the body.
 * </p>
 * <p>
 * We also seen an issue in Tika where (via the XHTMLContentHandler) additional html/body elements
 * are put in the body. This is because XHTMLContentHandler specifically adds a html header.
 * </p>
 */
public class NoHeadTagInBodyContentHandler extends ContentHandlerDecorator {

  private static final Set<String> HEADER_TAGS = Sets.newHashSet("title", "meta", "script", "style",
      "link");

  private boolean inHead = false;
  private boolean ignoreCharacters = false;
  private int openHtml = 0;
  private int openBody = 0;
  private boolean seenHtml = false;
  private boolean seenBody = false;

  /**
   * Instantiates a new content handler.
   *
   * @param handler
   *          the underlying handler to delegate to (typically a HTMLContentHandler)
   */
  public NoHeadTagInBodyContentHandler(final ContentHandler handler) {
    super(handler);
  }

  @Override
  public void startElement(final String uri, final String localName, final String qname,
      final Attributes atts) throws SAXException {

    if ("html".equals(localName)) {
      if (!seenHtml) {
        super.startElement(uri, localName, qname, atts);
      }
      seenHtml = true;
      openHtml = openHtml + 1;
    } else if ("body".equals(localName)) {
      if (!seenBody) {
        super.startElement(uri, localName, qname, atts);
      }
      seenBody = true;
      openBody = openBody + 1;
    } else if (inHead || !HEADER_TAGS.contains(localName)) {
      if ("head".equals(localName)) {
        inHead = true;
      }
      super.startElement(uri, localName, qname, atts);
    } else {
      // Not in head and not an allowed tag, so inner text content...
      // Technically I guess we should skip all child tags too, but that isn't relevant for
      // VALID html given our tags above
      ignoreCharacters = true;
    }
  }

  @Override
  public void characters(final char[] ch, final int start, final int length) throws SAXException {
    if (!ignoreCharacters) {
      super.characters(ch, start, length);
    }
  }

  @Override
  public void endElement(final String uri, final String localName, final String qname)
      throws SAXException {
    if ("html".equals(localName)) {
      openHtml--;
      if (openHtml == 0) {
        super.endElement(uri, localName, qname);
      }
    } else if ("body".equals(localName)) {
      openBody--;
      if (openBody == 0) {
        super.endElement(uri, localName, qname);
      }

    } else if (inHead || !HEADER_TAGS.contains(localName)) {
      if ("head".equals(localName)) {
        inHead = false;
      }
      super.endElement(uri, localName, qname);
    } else {
      ignoreCharacters = false;
    }
  }

}
