package io.committed.krill.extraction.tika.parsers;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Tika compliant parser for CSV.
 *
 * <p>This CSV parser auto detects TSV / CSV (and other common separators).
 *
 * <p>It is a standard Tika Parser and thus sits within the Tika pipeline and outputs a HTML (table)
 * representation of the CSV.
 */
public class CsvParser implements Parser {

  /** Comparator for header ordering. */
  private static class HeaderOrderComparator
      implements Comparator<Map.Entry<String, Integer>>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final Entry<String, Integer> o1, final Entry<String, Integer> o2) {
      return Integer.compare(o1.getValue(), o2.getValue());
    }
  }

  private static final char[] COMMON_DELIMITERS = {',', '\t', '|', '~', '\\', '/'};

  private static final AttributesImpl EMPTY_ATTRIBUTES = new AttributesImpl();

  private static final String URI = "http://www.w3.org/1999/xhtml";

  private static final String TR = "tr";

  private static final String TH = "th";

  private static final String TD = "td";

  private static final String TABLE = "table";

  private static final long serialVersionUID = 1L;

  @Override
  public void parse(
      final InputStream stream,
      final ContentHandler handler,
      final Metadata metadata,
      final ParseContext context)
      throws IOException, SAXException, TikaException {

    try (Reader reader = new AutoDetectReader(stream)) {
      handler.startDocument();
      handler.startElement(URI, "html", "html", EMPTY_ATTRIBUTES);
      handler.startElement(URI, "body", "body", EMPTY_ATTRIBUTES);

      handleTable(handler, reader);

      handler.endElement(URI, "body", "body");
      handler.endElement(URI, "html", "html");
      handler.endDocument();
    }
  }

  /**
   * Output the table (the entire CSV) to the content handler
   *
   * @param handler the handler
   * @param reader the reader
   * @throws SAXException the SAX exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void handleTable(final ContentHandler handler, final Reader reader)
      throws SAXException, IOException {
    final String document = IOUtils.toString(reader);
    try (CSVParser parser = getParser(document)) {
      handler.startElement(URI, TABLE, TABLE, EMPTY_ATTRIBUTES);

      handleHeaderRow(parser, handler);
      for (final CSVRecord record : parser) {
        handleRow(record, handler);
      }
      handler.endElement(URI, TABLE, TABLE);
    }
  }

  /**
   * Output a header row to the content handler.
   *
   * @param parser the parser
   * @param handler the handler
   * @throws SAXException the SAX exception
   */
  private void handleHeaderRow(final CSVParser parser, final ContentHandler handler)
      throws SAXException {
    if (parser.getHeaderMap() == null || parser.getHeaderMap().size() == 0) {
      return;
    }
    final ArrayList<Entry<String, Integer>> headers =
        new ArrayList<>(parser.getHeaderMap().entrySet());
    Collections.sort(headers, new HeaderOrderComparator());
    handler.startElement(URI, TR, TR, EMPTY_ATTRIBUTES);
    for (final Entry<String, Integer> column : headers) {
      handleHeaderColumn(column, handler);
    }
    handler.endElement(URI, TR, TR);
  }

  /**
   * Output a header value to the content handler.
   *
   * @param column the column
   * @param handler the {@link ContentHandler} to emit to
   * @throws SAXException if an error occurs calling the handler.
   */
  private void handleHeaderColumn(final Entry<String, Integer> column, final ContentHandler handler)
      throws SAXException {
    handler.startElement(URI, TH, TH, EMPTY_ATTRIBUTES);
    final char[] charArray = column.getKey().toCharArray();
    handler.characters(charArray, 0, charArray.length);
    handler.endElement(URI, TH, TH);
  }

  /**
   * Gets the parser implementation based on analysing the document.
   *
   * @param document the document
   * @return the parser
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private CSVParser getParser(final String document) throws IOException {
    CSVFormat format = CSVFormat.DEFAULT;
    format = configureDelimiter(document, format);
    format = configureHeaders(document, format);

    return format.parse(new StringReader(document));
  }

  /**
   * Configure headers.
   *
   * @param document the document
   * @param format the format
   * @return the CSV format
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private CSVFormat configureHeaders(final String document, final CSVFormat format)
      throws IOException {
    if (hasHeaders(format, document)) {
      return format.withFirstRecordAsHeader();
    }
    return format;
  }

  /**
   * Attempt to guess the delimiter used betwene fields.
   *
   * <p>This method is flawed in the case where string values contain a lot of valid delimiter
   * characters (eg comma) in relation to the number of fields. However, this should largely cancel
   * out over multiple lines.
   *
   * @param document the document
   * @param format the format
   * @return the CSV format
   */
  private CSVFormat configureDelimiter(final String document, final CSVFormat format) {
    int index = StringUtils.ordinalIndexOf(document, "\n", 10);
    if (index < 0) {
      index = document.length();
    }
    final String substring = document.substring(0, index);
    char bestDelimiter = ',';
    long bestCount = 0;
    for (final char delimiter : COMMON_DELIMITERS) {
      final long count = substring.chars().filter(num -> num == delimiter).count();
      if (count > bestCount) {
        bestCount = count;
        bestDelimiter = delimiter;
      }
    }
    return format.withDelimiter(bestDelimiter);
  }

  /**
   * Guess if a header is in place.
   *
   * @param format the format
   * @param text the text
   * @return true, if successful
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private boolean hasHeaders(final CSVFormat format, final String text) throws IOException {
    try (Reader reader = new StringReader(text);
        CSVParser parser = format.parse(reader)) {
      final Iterator<CSVRecord> iterator = parser.iterator();

      // If there are no rows there can be no header
      if (!iterator.hasNext()) {
        return false;
      }

      final CSVRecord next = iterator.next();

      // Header columns should all be unique
      if (Sets.newHashSet(next).size() != next.size()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Output a row to the content handler.
   *
   * @param record the record
   * @param handler the handler
   * @throws SAXException the SAX exception
   */
  private void handleRow(final CSVRecord record, final ContentHandler handler) throws SAXException {
    handler.startElement(URI, TR, TR, EMPTY_ATTRIBUTES);
    for (final String column : record) {
      handleCell(column, handler);
    }
    handler.endElement(URI, TR, TR);
  }

  /**
   * Output a single cell to the content handler.
   *
   * @param column the column
   * @param handler the handler
   * @throws SAXException the SAX exception
   */
  private void handleCell(final String column, final ContentHandler handler) throws SAXException {
    handler.startElement(URI, TD, TD, EMPTY_ATTRIBUTES);
    final char[] charArray = column.toCharArray();
    handler.characters(charArray, 0, charArray.length);
    handler.endElement(URI, TD, TD);
  }

  @Override
  public Set<MediaType> getSupportedTypes(final ParseContext context) {
    return Sets.newHashSet(MediaType.text("csv"), MediaType.text("tab-separated-values"));
  }
}
