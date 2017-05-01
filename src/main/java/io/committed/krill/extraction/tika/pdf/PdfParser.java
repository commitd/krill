package io.committed.krill.extraction.tika.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import io.committed.krill.extraction.pdfbox.PdfStructuredExtractor;

public class PdfParser implements Parser {

  private static final long serialVersionUID = 1L;

  private static final Set<MediaType> SUPPORTED_TYPES =
      Collections.singleton(MediaType.application("pdf"));

  private final PdfParserConfig pdfParserConfig;

  public PdfParser(PdfParserConfig pdfParserConfig) {
    this.pdfParserConfig = pdfParserConfig;
  }

  @Override
  public Set<MediaType> getSupportedTypes(ParseContext context) {
    return SUPPORTED_TYPES;
  }

  @Override
  public void parse(InputStream stream, ContentHandler handler, Metadata metadata,
      ParseContext context) throws IOException, SAXException, TikaException {
    try (PDDocument document = PDDocument.load(stream)) {
      parseMetadata(metadata, context, document);
      parseContent(handler, document);
    }
  }

  private void parseContent(ContentHandler handler, PDDocument document)
      throws IOException, SAXException {
    PdfStructuredExtractor extractor = new PdfStructuredExtractor(pdfParserConfig);
    extractor.processDocument(document, handler);
  }

  private void parseMetadata(Metadata metadata, ParseContext context, PDDocument document)
      throws IOException {
    PdfMetadataParser metadataParser = new PdfMetadataParser();
    metadataParser.processDocumentInformation(document, metadata);
    metadataParser.processMetadata(document, metadata, context);
  }

}
