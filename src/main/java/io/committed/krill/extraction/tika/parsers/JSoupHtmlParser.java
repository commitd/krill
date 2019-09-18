package io.committed.krill.extraction.tika.parsers;

import com.google.common.base.Strings;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.tika.config.ServiceLoader;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Jsoup based HTML parser for Tika.
 *
 * <p>Tika has a HTML parser but it seems to a lot of features in particular robustness to poor
 * input, support for cleaning the document and support for HTML5 types.
 *
 * <p>This is a simple implementation based on the JSoup, Tika uses TagSoup by default (which seems
 * to lack the HTML5 tags such as a header which are important to us).
 *
 * <p>Note this implementation does not actually remove questionable content from the HTML, but the
 * clean() method can be implemented to do so.
 */
public class JSoupHtmlParser extends AbstractParser {

  private static final String NS = "http://www.w3.org/1999/xhtml";

  private static final Logger LOGGER = LoggerFactory.getLogger(JSoupHtmlParser.class);

  private static final long serialVersionUID = 1L;

  private static final Set<MediaType> SUPPORTED_TYPES =
      Collections.unmodifiableSet(
          new HashSet<MediaType>(
              Arrays.asList(MediaType.text("html"), MediaType.application("xhtml+xml"))));

  private static final ServiceLoader LOADER = new ServiceLoader(HtmlParser.class.getClassLoader());

  @Override
  public Set<MediaType> getSupportedTypes(final ParseContext parseContext) {
    return SUPPORTED_TYPES;
  }

  @Override
  public void parse(
      final InputStream stream,
      final ContentHandler handler,
      final Metadata metadata,
      final ParseContext parseContext)
      throws IOException, SAXException, TikaException {

    // Copied from Tika HTML Parser
    try (AutoDetectReader reader =
        new AutoDetectReader(
            new CloseShieldInputStream(stream),
            metadata,
            parseContext.get(ServiceLoader.class, LOADER))) {
      final Charset charset = reader.getCharset();
      final String previous = metadata.get(Metadata.CONTENT_TYPE);
      MediaType contentType = null;
      if (previous == null || previous.startsWith("text/html")) {
        contentType = new MediaType(MediaType.TEXT_HTML, charset);
      } else if (previous.startsWith("application/xhtml+xml")) {
        contentType = new MediaType(MediaType.application("xhtml+xml"), charset);
      }
      if (contentType != null) {
        metadata.set(Metadata.CONTENT_TYPE, contentType.toString());
      }
      // deprecated, see TIKA-431
      metadata.set(Metadata.CONTENT_ENCODING, charset.name());

      process(
          new ReaderInputStream(reader, reader.getCharset().name()),
          reader.getCharset().name(),
          handler,
          metadata);
    }
  }

  private void process(
      final InputStream stream,
      final String charset,
      final ContentHandler handler,
      final Metadata metadata)
      throws IOException, SAXException {

    final Document document = Jsoup.parse(stream, charset, "");
    clean(document);

    document
        .head()
        .select("meta")
        .forEach(
            m -> {
              final String name = m.attr("name");
              final String value = m.attr("content");
              if (!Strings.isNullOrEmpty(name)) {
                metadata.add(name, value);
              }
            });

    document.traverse(new JsoupToSaxVisitor(handler));
  }

  /**
   * Subclasses may wish to strip code here, apply JSoup {@link Whitelist} cleaners, etc...
   *
   * @param document the document to clean.
   */
  protected void clean(final Document document) {
    // do nothing
  }

  private static final class JsoupToSaxVisitor implements NodeVisitor {
    private final ContentHandler handler;

    private JsoupToSaxVisitor(final ContentHandler handler) {
      this.handler = handler;
    }

    @Override
    public void head(final Node node, final int depth) {
      try {
        if (node instanceof Document) {
          handler.startDocument();
        } else if (node instanceof Element) {
          final Element e = (Element) node;
          final org.xml.sax.Attributes attributes = convertAttributes(e.attributes());
          handler.startElement(NS, e.tagName(), e.tagName(), attributes);
        } else if (node instanceof TextNode) {
          final TextNode t = (TextNode) node;
          final char[] c = t.toString().toCharArray();
          handler.characters(c, 0, c.length);
        }
      } catch (final SAXException e) {
        LOGGER.warn("Fail to open tag", e);
      }
    }

    private org.xml.sax.Attributes convertAttributes(final Attributes jsoup) {
      final AttributesImpl sax = new AttributesImpl();
      jsoup.forEach(a -> sax.addAttribute(NS, a.getKey(), a.getKey(), "", a.getValue()));
      return sax;
    }

    @Override
    public void tail(final Node node, final int depth) {
      try {
        if (node instanceof Document) {
          handler.endDocument();
        } else if (node instanceof Element) {
          final Element e = (Element) node;
          handler.endElement(NS, e.tagName(), e.tagName());
        }
      } catch (final SAXException e) {
        LOGGER.warn("Fail to close tag", e);
      }
    }
  }
}
