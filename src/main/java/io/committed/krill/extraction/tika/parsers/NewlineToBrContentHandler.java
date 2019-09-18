package io.committed.krill.extraction.tika.parsers;

import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Convert Newlines to BR tags when in the Body element.
 *
 * <p>Useful to wrapping Tika parsers which output some HTML tags but still use newlines.
 */
public class NewlineToBrContentHandler extends ContentHandlerDecorator {

  private static final String NS = "http://www.w3.org/1999/xhtml";
  private boolean inBody = false;

  /**
   * Newline to BR content handler.
   *
   * @param handler the handler
   */
  public NewlineToBrContentHandler(final ContentHandler handler) {
    super(handler);
  }

  @Override
  public void startElement(
      final String uri, final String localName, final String name, final Attributes atts)
      throws SAXException {
    if (localName.equalsIgnoreCase("body")) {
      inBody = true;
    }
    super.startElement(uri, localName, name, atts);
  }

  @Override
  public void endElement(final String uri, final String localName, final String name)
      throws SAXException {
    if (localName.equalsIgnoreCase("body")) {
      inBody = false;
    }
    super.endElement(uri, localName, name);
  }

  @Override
  public void characters(final char[] ch, final int start, final int length) throws SAXException {
    if (!inBody) {
      super.characters(ch, start, length);
      return;
    }

    // Otherwise split on newlines, send out br tags
    int startIndex = start;
    for (int index = start; index < length; index++) {
      if (ch[index] == '\n') {
        // output from s to here
        final int lengthRemaining = index - startIndex;
        if (lengthRemaining > 0) {
          super.characters(ch, startIndex, lengthRemaining);
        }
        super.startElement(NS, "br", "br", new AttributesImpl());
        super.endElement(NS, "br", "br");

        // Start at the end element...(so we get a newline after the br)
        startIndex = index;
      }
    }

    // Send the last span
    final int lengthRemaining = length - startIndex;
    if (lengthRemaining > 0) {
      super.characters(ch, startIndex, lengthRemaining);
    }
  }
}
