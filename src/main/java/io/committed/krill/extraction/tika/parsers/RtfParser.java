package io.committed.krill.extraction.tika.parsers;

import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.rtf.RTFParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A RTF Parser which converts new lines into brs.
 *
 * <p>Thus better supporting HTML (albeit in a minor way).
 */
public class RtfParser extends RTFParser {

  private static final long serialVersionUID = 1L;

  @Override
  public void parse(
      final InputStream stream,
      final ContentHandler handler,
      final Metadata metadata,
      final ParseContext parseContext)
      throws IOException, SAXException, TikaException {
    super.parse(stream, new NewlineToBrContentHandler(handler), metadata, parseContext);
  }
}
