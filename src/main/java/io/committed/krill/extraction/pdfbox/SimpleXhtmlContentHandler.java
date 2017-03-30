package io.committed.krill.extraction.pdfbox;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A {@link ContentHandlerDecorator} which emits elements in the the XHTML namespace.
 */
public class SimpleXhtmlContentHandler extends ContentHandlerDecorator {

  /** The Constant XHTML. */
  public static final String XHTML = "http://www.w3.org/1999/xhtml";

  /**
   * Instantiates a new simple xhtml content handler.
   *
   * @param delegate
   *          the delegate
   */
  public SimpleXhtmlContentHandler(ContentHandler delegate) {
    super(delegate);
  }

  @Override
  public void startDocument() throws SAXException {
    super.startDocument();
    startPrefixMapping("", XHTML);
  }

  @Override
  public void endDocument() throws SAXException {
    endPrefixMapping("");
    super.endDocument();
  }

  /**
   * Start element.
   *
   * @param name
   *          the name
   * @param strings
   *          the strings
   * @throws SAXException
   *           the SAX exception
   */
  public void startElement(String name, String... strings) throws SAXException {
    startElement(XHTML, name, name, attributes(strings));
  }

  /**
   * Characters.
   *
   * @param string
   *          the string
   * @throws SAXException
   *           the SAX exception
   */
  public void characters(String string) throws SAXException {
    characters(string.toCharArray(), 0, string.length());
  }

  /**
   * End element.
   *
   * @param name
   *          the name
   * @throws SAXException
   *           the SAX exception
   */
  public void endElement(String name) throws SAXException {
    endElement(XHTML, name, name);
  }

  /**
   * Generates an Attributes object from a list of key-value pairs provided in a flat array /
   * varargs.
   *
   * @param attributePairs
   *          the attribute pairs
   * @return the attributes
   */
  private static Attributes attributes(String... attributePairs) {
    if (attributePairs.length % 2 != 0) {
      throw new IllegalArgumentException("Odd number of attribute pair components!");
    }
    AttributesImpl attributes = new AttributesImpl();
    for (int i = 0; i < attributePairs.length; i += 2) {
      attributes.addAttribute("", attributePairs[i], attributePairs[i], "CDATA",
          attributePairs[i + 1]);
    }
    return attributes;
  }

}