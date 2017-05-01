package io.committed.krill.extraction.pdfbox;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import io.committed.krill.extraction.pdfbox.interpretation.BlockTypeClassifier;
import io.committed.krill.extraction.pdfbox.interpretation.BlockTypeLabel;
import io.committed.krill.extraction.pdfbox.interpretation.LabellablePositioned;
import io.committed.krill.extraction.pdfbox.interpretation.ReadingOrder;
import io.committed.krill.extraction.pdfbox.interpretation.SimpleBlockClassifier;
import io.committed.krill.extraction.pdfbox.interpretation.XyCutReadingOrder;
import io.committed.krill.extraction.pdfbox.physical.ImageBlock;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Style;
import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.text.PageSegmenter;
import io.committed.krill.extraction.pdfbox.text.SimplePageSegmenter;
import io.committed.krill.extraction.tika.pdf.PdfParserConfig;

/**
 * Extract structure from PDF files.
 */
public class PdfStructuredExtractor {

  /** The Constant MAX_UNDERLINE_HEIGHT. */
  private static final double MAX_UNDERLINE_HEIGHT = 72 / 12d;

  /** The pdf stripper. */
  private final PdfStripper pdfStripper;

  /** The segmenter. */
  private final PageSegmenter segmenter;

  /** The reading order. */
  private final ReadingOrder readingOrder;

  /** The parser config. */
  private final PdfParserConfig parserConfig;

  /**
   * Instantiates a new pdf structured extractor.
   */
  public PdfStructuredExtractor() {
    this(new PdfParserConfig());
  }

  /**
   * Instantiates a new pdf structured extractor.
   *
   * @param parserConfig the parser config
   */
  public PdfStructuredExtractor(PdfParserConfig parserConfig) {
    this.parserConfig = parserConfig;
    pdfStripper = new PdfStripper();
    segmenter = new SimplePageSegmenter(parserConfig);
    readingOrder = new XyCutReadingOrder(parserConfig);
  }

  /**
   * Extract the content from a PDF file, extracting structure around the content as SAX events.
   *
   * @param document the {@link PDDocument} to extract.
   * @param contentHandler the SAX {@link ContentHandler} to send structure events too.
   * @throws IOException if an error occurs reading the document.
   * @throws SAXException if an error occurs creating SAX events.
   */
  public void processDocument(PDDocument document, ContentHandler contentHandler)
      throws IOException, SAXException {
    BlockContentHandler handler =
        new BlockContentHandler(contentHandler, parserConfig.isEmitAbsolutePositioning());
    handler.startDocument();

    BlockTypeClassifier blockClassifier = classifyBlocks(document);

    for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
      handler.startElement("article", "class", "page");
      Collection<LabellablePositioned> pageBlocks = blockClassifier.getBlocks(pageIndex);

      Collection<LabellablePositioned> headerBlocks = pageBlocks.stream()
          .filter(s -> s.getLabels().contains(BlockTypeLabel.HEADER)
              || s.getPosition().getMaxY() < blockClassifier.getHeaderRegionBottom())
          .collect(Collectors.toList());
      handler.emitPageHeader(readingOrder.order(headerBlocks));

      Collection<LabellablePositioned> bodyBlocks = pageBlocks.stream()
          .filter(s -> !s.getLabels().contains(BlockTypeLabel.HEADER)
              && !s.getLabels().contains(BlockTypeLabel.FOOTER)
              && s.getPosition().getMaxY() >= blockClassifier.getHeaderRegionBottom()
              && s.getPosition().getMinY() <= blockClassifier.getFooterRegionTop())
          .collect(Collectors.toList());
      handler.emitBlocks(readingOrder.order(bodyBlocks));

      Collection<LabellablePositioned> footerBlocks = pageBlocks.stream()
          .filter(s -> s.getLabels().contains(BlockTypeLabel.FOOTER)
              || s.getPosition().getMinY() > blockClassifier.getFooterRegionTop())
          .collect(Collectors.toList());
      handler.emitPageFooter(readingOrder.order(footerBlocks));

      handler.endElement("article");
    }
    handler.endDocument();
  }

  /**
   * Classify blocks.
   *
   * @param document the document
   * @return the block type classifier
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private BlockTypeClassifier classifyBlocks(PDDocument document) throws IOException {
    BlockTypeClassifier blockClassifier = new SimpleBlockClassifier();
    for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
      PageContent content = pdfStripper.processPage(document, pageIndex);
      Collection<Line2D> lines = new ArrayList<>();
      lines.addAll(content.getLines());
      lines.addAll(makeLines(content.getRectangles()));
      findUnderlines(content);
      Collection<? extends LabellablePositioned> imageBlocks =
          content.getImageLocations().stream().map(ImageBlock::new).collect(Collectors.toList());
      Collection<? extends LabellablePositioned> textBlocks =
          segmenter.segment(content.getTextSequences(), lines);

      Collection<LabellablePositioned> blocks = new ArrayList<>();
      blocks.addAll(imageBlocks);
      blocks.addAll(textBlocks);

      blockClassifier.addPage(pageIndex, blocks, content.getCropBox());
    }
    blockClassifier.label();
    return blockClassifier;
  }

  /**
   * Make lines.
   * <p>
   * Lines are encoded as both lines and rectangle commands in PDF files. This extracts very thin
   * rectangles as lines too.
   * </p>
   *
   * @param rectangles the rectangles
   * @return the collection of extracted lines.
   */
  private Collection<Line2D> makeLines(List<Rectangle2D> rectangles) {
    Collection<Line2D> lines = new HashSet<>();
    for (Rectangle2D r : rectangles) {
      if (r.getWidth() > r.getHeight() && r.getHeight() < 2) {
        // assume this is a 'line' rectangle
        double ypos = (r.getMinY() + r.getMaxY()) / 2;
        lines.add(new Line2D.Double(r.getMinX(), ypos, r.getMaxX(), ypos));
      } else if (r.getWidth() < 2) {
        // assume this is a 'line' rectangle
        double xpos = (r.getMinX() + r.getMaxX()) / 2;
        lines.add(new Line2D.Double(xpos, r.getMinY(), xpos, r.getMaxY()));
      }

      // otherwise assume this is a large rectangle
      lines.add(new Line2D.Double(r.getMinX(), r.getMinY(), r.getMaxX(), r.getMinY()));
      lines.add(new Line2D.Double(r.getMinX(), r.getMaxY(), r.getMaxX(), r.getMaxY()));
      lines.add(new Line2D.Double(r.getMinX(), r.getMinY(), r.getMinX(), r.getMaxY()));
      lines.add(new Line2D.Double(r.getMaxX(), r.getMinY(), r.getMaxX(), r.getMaxY()));
    }
    return lines;
  }

  /**
   * Find underlines.
   *
   * @param content the content
   */
  private void findUnderlines(PageContent content) {
    List<Rectangle2D> underlineCandidates = findUnderlineCandidates(content.getRectangles());
    List<PositionedContainer<Text>> textSequences = content.getTextSequences();
    for (PositionedContainer<Text> container : textSequences) {
      for (Text text : container.getContents()) {
        for (Rectangle2D underline : underlineCandidates) {
          if (isUnderlinedBy(text, underline)) {
            // cannot mutate the object as it may be shared with other texts!
            Style style = new Style(text.getStyle());
            style.setUnderlined(true);
            text.setStyle(style);
          }
        }
      }
    }
  }

  /**
   * Checks if is text is underlined by the line.
   *
   * <p>
   * PDF has no concept of underlining - it is achieved by drawing lines or rectangles in the right
   * place.
   * </p>
   *
   * @param text the text
   * @param underline the underline
   * @return true, if is underlined by
   */
  private boolean isUnderlinedBy(Text text, Rectangle2D underline) {
    // expand the underline vertically by twice it's height (this should compensate the gap between
    // non-descenders and the underline)
    Rectangle2D.Double rectangle =
        new Rectangle2D.Double(underline.getMinX(), underline.getMinY() - 2 * underline.getHeight(),
            underline.getWidth(), underline.getHeight() * 3);
    Rectangle2D pos = text.getPosition();

    // test if the expanded underline intersects the baseline of the text
    return rectangle.intersectsLine(pos.getMinX(), text.getBaseline(), pos.getMaxX(),
        text.getBaseline());
  }

  /**
   * Find underline candidates.
   *
   * @param rectangles the rectangles
   * @return the list
   */
  private List<Rectangle2D> findUnderlineCandidates(List<Rectangle2D> rectangles) {
    return rectangles.stream()
        .filter(
            r -> r.getHeight() < MAX_UNDERLINE_HEIGHT && r.getWidth() > 5 * MAX_UNDERLINE_HEIGHT)
        .collect(Collectors.toList());
  }

}
