package io.committed.krill.extraction.tika.processors;

import org.apache.tika.metadata.Metadata;
import org.jsoup.nodes.Document;

/**
 * Format processor for Excel (XLS/XSLX) files.
 * <p>
 * Cleans up CSS classes and standardises on the output below. The HTML structure of a spreadsheet
 * is standardised as follows:
 * </p>
 *
 * <pre>
 *   html
 *   - body
 *     - main class=SpreadSheet
 *       - article class=Sheet
 *         - h1 : the sheet tile
 *         - table
 *           - (standard HTML table)
 *         - section - a floating element
 *         - other misc items (p, etc tags)
 *       - article class=Sheet (being sheet 2)
 * </pre>
 *
 * <p>
 * Note:
 * </p>
 *
 * <ul>
 * <li>DOCX does not support section for floating text. It will be output as p tags.</li>
 * </ul>
 */
public class ExcelFormatProcessor extends AbstractJsoupFormatProcessor {

  /*
   * (non-Javadoc)
   * 
   * @see
   * io.committed.krill.extraction.tika.processors.AbstractJsoupFormatProcessor#process(org.apache.
   * tika.metadata.Metadata, org.jsoup.nodes.Document)
   */
  @Override
  public Document process(final Metadata metadata, final Document document) {

    // Under body add a document main
    wrapChildrenOfBodyInTag(document, "<main class=\"SpreadSheet\"></main>");

    document.select("div.page").tagName("article").attr("class", "Sheet");

    // Doc: Element in floating textboxes have class outside
    document.select("div.outside").tagName("section").removeAttr("class");

    // Excel puts a preview embedded in.. which we don't want
    document.select("div[id=/docProps/thumbnail.jpeg]").remove();

    return document;
  }

}
