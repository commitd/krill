package io.committed.krill.extraction.pdfbox;

import io.committed.krill.extraction.pdfbox.interpretation.BlockTypeLabel;
import io.committed.krill.extraction.pdfbox.interpretation.LabellablePositioned;
import io.committed.krill.extraction.pdfbox.physical.ImageBlock;
import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.Positioned;
import io.committed.krill.extraction.pdfbox.physical.Style;
import io.committed.krill.extraction.pdfbox.physical.Styled;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;
import io.committed.krill.extraction.pdfbox.physical.Word;
import io.committed.krill.extraction.pdfbox.text.TableBlock;
import io.committed.krill.extraction.pdfbox.text.TableCell;
import io.committed.krill.extraction.pdfbox.text.TableRow;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A helper class for emitting identified blocks from a PDF.
 */
public class BlockContentHandler extends SimpleXhtmlContentHandler {

  /** The Constant HEADER. */
  private static final String HEADER = "header";

  /** The Constant FOOTER. */
  private static final String FOOTER = "footer";

  /** The emit absolution positioning. */
  private final boolean emitAbsolutionPositioning;

  /**
   * Instantiates a new block content handler.
   *
   * @param delegate
   *          the delegate
   * @param emitAbsolutionPositioning
   *          the emit absolution positioning
   */
  public BlockContentHandler(ContentHandler delegate, boolean emitAbsolutionPositioning) {
    super(delegate);
    this.emitAbsolutionPositioning = emitAbsolutionPositioning;
  }

  /**
   * Emit the given blocks, wrapped in a "header" tag.
   *
   * @param headerBlocks
   *          the blocks for the header section.
   * @throws SAXException
   *           if an error occurs generating SAX events.
   */
  public void emitPageHeader(List<LabellablePositioned> headerBlocks) throws SAXException {
    if (headerBlocks.isEmpty()) {
      return;
    }
    startElement(HEADER);
    emitBlocks(headerBlocks);
    endElement(HEADER);
  }

  /**
   * Emit the given blocks, wrapped in a "footer" tag.
   *
   * @param footerBlocks
   *          the blocks for the footer section.
   * @throws SAXException
   *           if an error occurs generating SAX events.
   */
  public void emitPageFooter(List<LabellablePositioned> footerBlocks) throws SAXException {
    if (footerBlocks.isEmpty()) {
      return;
    }
    startElement(FOOTER);
    emitBlocks(footerBlocks);
    endElement(FOOTER);
  }

  /**
   * Emit the given blocks.
   *
   * @param pageBlocks
   *          the blocks to emit, with special handling for {@link TextBlock}, {@link ImageBlock}
   *          and {@link TableBlock}.
   *
   * @throws SAXException
   *           if an error occurs generating SAX events.
   */
  public void emitBlocks(List<LabellablePositioned> pageBlocks) throws SAXException {
    for (LabellablePositioned block : pageBlocks) {
      if (block instanceof TextBlock) {
        emitBlock((TextBlock) block);
      } else if (block instanceof ImageBlock) {
        emitBlock((ImageBlock) block);
      } else if (block instanceof TableBlock) {
        emitBlock((TableBlock) block);
      }
    }
  }

  /**
   * Emit Image block.
   *
   * @param imageBlock
   *          the image block
   * @throws SAXException
   *           the SAX exception
   */
  private void emitBlock(ImageBlock imageBlock) throws SAXException {
    startElement("img", imageBlock);
    endElement("img");
  }

  /**
   * Emit Table block.
   *
   * @param tableBlock
   *          the table block
   * @throws SAXException
   *           the SAX exception
   */
  private void emitBlock(TableBlock tableBlock) throws SAXException {
    startElement("table", tableBlock);

    // all lines in the block currently have the same style
    List<TableRow> rows = tableBlock.getContents();
    for (TableRow row : rows) {
      startElement("tr");
      for (TextBlock cell : row.getContents()) {
        startElement("td", cell);
        emitBlock(new TextBlock(cell.getContents()));
        endElement("td");
      }
      endElement("tr");
    }

    endElement("table");
  }

  /**
   * Emit Text block.
   *
   * @param textBlock
   *          the text block
   * @throws SAXException
   *           the SAX exception
   */
  private void emitBlock(TextBlock textBlock) throws SAXException {
    String tag = getTag(textBlock);

    startElement(tag, textBlock);

    List<Line> contents = textBlock.getContents();
    boolean curBold = false;
    boolean curItalic = false;
    boolean curUnderline = false;
    boolean prevLine = false;
    for (Line line : contents) {
      if (prevLine) {
        startElement("br");
        endElement("br");
        characters(System.lineSeparator());
      }
      prevLine = true;
      boolean prevWord = false;
      for (Word word : line.getContents()) {
        if (prevWord) {
          characters(" ");
        }
        prevWord = true;
        Style cr = word.getStyle();
        if (cr.isBold() != curBold) {
          if (curUnderline) {
            endElement("u");
            curUnderline = false;
          }
          if (curItalic) {
            endElement("i");
            curItalic = false;
          }
          if (cr.isBold()) {
            startElement("b");
          } else {
            endElement("b");
          }
          curBold = cr.isBold();
        }

        if (cr.isItalic() != curItalic) {
          if (curUnderline) {
            endElement("u");
            curUnderline = false;
          }
          if (cr.isItalic()) {
            startElement("i");
          } else {
            endElement("i");
          }
          curItalic = cr.isItalic();
        }

        if (cr.isUnderlined() != curUnderline) {
          if (cr.isUnderlined()) {
            startElement("u");
          } else {
            endElement("u");
          }
          curUnderline = cr.isUnderlined();
        }
        characters(word.toString());

      }
      if (curUnderline) {
        endElement("u");
        curUnderline = false;
      }
      if (curItalic) {
        endElement("i");
        curItalic = false;
      }
      if (curBold) {
        endElement("b");
        curBold = false;
      }
    }

    endElement(tag);
  }

  /**
   * Gets the tag.
   *
   * @param textBlock
   *          the text block
   * @return the tag
   */
  private String getTag(TextBlock textBlock) {
    Set<BlockTypeLabel> labels = textBlock.getLabels();
    if (labels.contains(BlockTypeLabel.HEADING)) {
      return "h1";
    }
    return "p";
  }

  /**
   * Start element.
   *
   * @param element
   *          the element
   * @param block
   *          the block
   * @throws SAXException
   *           the SAX exception
   */
  private void startElement(String element, LabellablePositioned block) throws SAXException {
    List<String> attributes = new ArrayList<>();

    StringBuilder styleBuffer = new StringBuilder();
    if (emitAbsolutionPositioning) {
      styleBuffer.append(makePosition(block));
    }
    if (block instanceof Styled) {
      styleBuffer.append(makeFontStyle((Styled) block));
    }

    if (styleBuffer.length() > 0) {
      attributes.add("style");
      attributes.add(styleBuffer.toString());
    }

    if (block instanceof TextBlock) {
      String styleClass = makeClass(block);
      if (styleClass != null && !styleClass.isEmpty()) {
        attributes.add("class");
        attributes.add(styleClass);
      }
    }

    if (block instanceof TableCell) {
      TableCell tableCell = (TableCell) block;
      if (tableCell.getColSpan() > 0) {
        attributes.add("colspan");
        attributes.add(Integer.toString(tableCell.getColSpan()));
      }
      if (tableCell.getRowSpan() > 0) {
        attributes.add("rowspan");
        attributes.add(Integer.toString(tableCell.getRowSpan()));
      }
    }
    startElement(element, attributes.toArray(new String[attributes.size()]));
  }

  /**
   * Make class label.
   *
   * @param block
   *          the block
   * @return the string
   */
  private static String makeLabel(LabellablePositioned block) {
    Set<BlockTypeLabel> labels = block.getLabels();
    return labels.stream().map(s -> s.toString().toLowerCase()).collect(Collectors.joining(","));
  }

  /**
   * Make position style attribute.
   *
   * @param location
   *          the location
   * @return the string
   */
  private static String makePosition(Positioned location) {
    if (location == null) {
      return "";
    }
    Rectangle2D position = location.getPosition();
    StringBuilder sb = new StringBuilder();
    sb.append("position: absolute; top:");
    sb.append(position.getY());
    sb.append("; left: ");
    sb.append(position.getX());
    sb.append("; width: ");
    sb.append(position.getWidth());
    sb.append("; height: ");
    sb.append(position.getHeight());
    sb.append(";");
    return sb.toString();
  }

  /**
   * Make font style attribute.
   *
   * @param block
   *          the block
   * @return the string
   */
  private static String makeFontStyle(Styled block) {
    if (block == null) {
      return "";
    }
    Style style = block.getStyle();
    if (style == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("font-family:");
    sb.append(style.getName());
    sb.append(";font-size:");
    sb.append(style.getSize());
    if (!style.getColor().isEmpty()) {
      sb.append("pt;color:");
      sb.append(style.getColor());
    }
    return sb.toString();
  }

  /**
   * Make class attribute.
   *
   * @param block
   *          the block
   * @return the string
   */
  private static String makeClass(LabellablePositioned block) {
    return makeLabel(block);
  }
}
