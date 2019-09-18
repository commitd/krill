package io.committed.krill.extraction.tika.pdf;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for adding metadata from the PDFBox {@link PDDocument} to the Tika {@link Metadata}
 * class.
 *
 * <p>The majority of the metadata is added through reflectively calling private method {@link
 * PDFParser#extractMetadata} rather than duplicating the logic.
 */
public class PdfMetadataParser implements Serializable {

  private static final Logger LOGGER = LoggerFactory.getLogger(PdfMetadataParser.class);

  private static final long serialVersionUID = 1L;

  void processDocumentInformation(PDDocument document, Metadata metadata) {
    PDDocumentInformation documentInformation = document.getDocumentInformation();
    if (documentInformation == null) {
      return;
    }
    metadata.add("title", documentInformation.getTitle());
    metadata.add("subject", documentInformation.getSubject());
    metadata.add("author", documentInformation.getAuthor());
    metadata.add("creator", documentInformation.getCreator());
    metadata.add("producer", documentInformation.getProducer());
  }

  void processMetadata(PDDocument document, Metadata metadata, ParseContext parseContext)
      throws IOException {
    try {
      PDFParser tikaParser = new PDFParser();
      Method method =
          tikaParser
              .getClass()
              .getDeclaredMethod(
                  "extractMetadata", PDDocument.class, Metadata.class, ParseContext.class);
      AccessController.doPrivileged(
          (PrivilegedAction<Void>)
              () -> {
                method.setAccessible(true);
                return null;
              });
      method.invoke(tikaParser, document, metadata, parseContext);
    } catch (NoSuchMethodException
        | SecurityException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      LOGGER.warn("Failed to call legacy Tika PDF Parser extractMetadata through reflection", e);
    }
  }
}
