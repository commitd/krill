package io.committed.krill.extraction.tika;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import io.committed.krill.extraction.Extraction;
import io.committed.krill.extraction.FormatExtractor;
import io.committed.krill.extraction.exception.ExtractionException;
import io.committed.krill.extraction.impl.DefaultExtraction;
import io.committed.krill.extraction.tika.parsers.CsvParser;
import io.committed.krill.extraction.tika.parsers.JSoupHtmlParser;
import io.committed.krill.extraction.tika.parsers.RtfParser;
import io.committed.krill.extraction.tika.pdf.PdfParser;
import io.committed.krill.extraction.tika.processors.CsvFormatProcessor;
import io.committed.krill.extraction.tika.processors.ExcelFormatProcessor;
import io.committed.krill.extraction.tika.processors.FormatProcessor;
import io.committed.krill.extraction.tika.processors.HtmlFormatProcessor;
import io.committed.krill.extraction.tika.processors.PdfFormatProcessor;
import io.committed.krill.extraction.tika.processors.PowerpointFormatProcessor;
import io.committed.krill.extraction.tika.processors.RtfFormatProcessor;
import io.committed.krill.extraction.tika.processors.TextFormatProcessor;
import io.committed.krill.extraction.tika.processors.WordFormatProcessor;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.CompositeParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlMapper;
import org.apache.tika.parser.html.IdentityHtmlMapper;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Extract HTML using Tika as the underlying reader.
 * <p>
 * This is the default implementation of FormatExtractor. It's setup is as follows:
 * </p>
 *
 * <ul>
 * <li>Replaces Tika's implementation of various parsers with our own, better performing tailored
 * parsers</li>
 * <li>Set up format specific content processors which clean the output of Tika</li>
 * </ul>
 *
 * <p>
 * When a stream is passed for extraction the Tika autodetection is used to determine which parser
 * to use (either Tika's or our replacement). Tika processes the stream and we collect the results
 * as HTML string together with the Metadata extracted by Tika.
 * </p>
 * <p>
 * Tika detects as part of its metadata the Content Type of the stream. This is used to select which
 * format processors will be used (if any).
 * </p>
 * <p>
 * The format processors act to clean up and standardise on a per document (content type) level.
 * They convert the HTML from Tika into HTML5 tags, removing nonsense and adding back information
 * lost by Tika implementations.
 * </p>
 * <p>
 * Once the format processors have completed the result of extraction is returned.
 * </p>
 */
public class TikaFormatExtractor implements FormatExtractor {

  private final CompositeParser parser;

  private final ParseContext context;

  private final Map<String, FormatProcessor> processors = new HashMap<>();

  private final TextFormatProcessor defaultProcessor = new TextFormatProcessor();

  /**
   * Constructs a new {@link TikaFormatExtractor} with an {@link AutoDetectParser}.
   */
  public TikaFormatExtractor() {
    this.parser = new AutoDetectParser();
    this.context = new ParseContext();

    addParsers(new CsvParser(), new JSoupHtmlParser(), new PdfParser(), new RtfParser());

    addProcessor(new CsvFormatProcessor(), "text/csv", "text/tab-separated-values");
    addProcessor(new ExcelFormatProcessor(), "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.template",
        "application/vnd.ms-excel.sheet.macroEnabled.12",
        "application/vnd.ms-excel.template.macroEnabled.12",
        "application/vnd.ms-excel.addin.macroEnabled.12",
        "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
    addProcessor(new WordFormatProcessor(), "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
        "application/vnd.ms-word.document.macroEnabled.12",
        "application/vnd.ms-word.template.macroEnabled.12");
    addProcessor(new PowerpointFormatProcessor(), "application/vnd.ms-powerpoint",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "application/vnd.openxmlformats-officedocument.presentationml.template",
        "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
        "application/vnd.ms-powerpoint.addin.macroEnabled.12",
        "application/vnd.ms-powerpoint.presentation.macroEnabled.12",
        "application/vnd.ms-powerpoint.template.macroEnabled.12",
        "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
    addProcessor(new PdfFormatProcessor(), MediaType.application("pdf").toString());
    addProcessor(new HtmlFormatProcessor(), MediaType.text("html").toString(),
        MediaType.application("xhtml+xml").toString());
    addProcessor(new RtfFormatProcessor(), MediaType.application("rtf").toString());

    // use the IdentityHtmlMapper to capture more of the structure.
    context.set(HtmlMapper.class, new IdentityHtmlMapper());
  }

  /**
   * Adds the format processor.
   *
   * @param processor
   *          the processor
   * @param contentTypes
   *          the content types to use the processor with
   */
  private final void addProcessor(final FormatProcessor processor, final String... contentTypes) {
    Arrays.stream(contentTypes).forEach(k -> processors.put(k, processor));
  }

  @Override
  public Extraction parse(final InputStream stream, final String source)
      throws ExtractionException {

    final Metadata tikaMetadata = new Metadata();
    tikaMetadata.set(TikaMetadataKeys.RESOURCE_NAME_KEY, source);

    final ToHTMLContentHandler handler = new ToHTMLContentHandler();

    try {
      parser.parse(stream,
          new XHTMLContentHandler(new NoHeadTagInBodyContentHandler(handler), tikaMetadata),
          tikaMetadata, context);
    } catch (IOException | SAXException | TikaException | NullPointerException exception) {
      throw new ExtractionException("Failed to parse stream", exception);
    }

    final String processedHtml = postProcess(tikaMetadata, handler.toString());
    return new DefaultExtraction(processedHtml, convertMetadata(tikaMetadata));
  }

  /**
   * Process the HTML output by Tika through any relevant format processors.
   *
   * @param metadata
   *          the metadata
   * @param html
   *          the html
   * @return the string
   */
  private String postProcess(final Metadata metadata, final String html) {
    String contentType = metadata.get("Content-Type");

    // Deal with case "contentType = text/html; charset=ISO-8859-1" (id=73) "
    final int sep = contentType.indexOf(';');
    if (sep != -1) {
      contentType = contentType.substring(0, sep);
    }

    final FormatProcessor processor = processors.get(contentType);
    if (processor == null) {
      // If there is no processor push it through the default one
      return defaultProcessor.process(metadata, html);
    }

    return processor.process(metadata, html);
  }

  /**
   * Convert metadata from Tika object into Multimap.
   *
   * @param tika
   *          the tika metadata
   * @return the multimap
   */
  private Multimap<String, String> convertMetadata(final Metadata tika) {
    final Multimap<String, String> map = LinkedListMultimap.create(tika.size());
    for (final String name : tika.names()) {
      final String[] values = tika.getValues(name);
      map.putAll(name, Arrays.asList(values));
    }
    return map;
  }

  /**
   * Replace Tika's parser with our parse for specific media types.
   *
   * @param newParsers
   *          the list of parsers to add
   */
  private final void addParsers(final Parser... newParsers) {
    final Map<MediaType, Parser> tikaParsers = parser.getParsers();
    for (final Parser newParser : newParsers) {
      final Set<MediaType> supportedTypes = newParser.getSupportedTypes(context);
      supportedTypes.forEach(mediaType -> tikaParsers.put(mediaType, newParser));
    }
    parser.setParsers(tikaParsers);
  }
}
