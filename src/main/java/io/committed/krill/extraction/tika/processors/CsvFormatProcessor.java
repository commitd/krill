package io.committed.krill.extraction.tika.processors;

import org.apache.tika.metadata.Metadata;
import org.jsoup.nodes.Document;

/**
 * Format processor for CSV/TSV files.
 *
 * <p>This acts to merely add the document type of spreadsheet and a single sheet to the class.
 *
 * <p>See {@link ExcelFormatProcessor} for details of the spreadsheet structure.
 */
public class CsvFormatProcessor extends AbstractJsoupFormatProcessor {

  @Override
  public Document process(final Metadata metadata, final Document document) {

    document
        .select("table")
        .wrap("<main class=\"SpreadSheet\"><article class=\"Sheet\"></article></main>");

    return document;
  }
}
