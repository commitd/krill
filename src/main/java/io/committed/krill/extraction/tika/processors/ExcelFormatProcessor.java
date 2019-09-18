package io.committed.krill.extraction.tika.processors;

import org.apache.commons.lang3.StringUtils;
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

    // Excel puts a preview embedded in.. which we don't want
    document.select("div[id=/docProps/thumbnail.jpeg]").remove();

    // Doc: Element in floating textboxes have class outside
    document.select("div.outside").tagName("section").removeAttr("class");

    // Wrap the sheets as a div
    document.select("div,div.page").tagName("article").attr("class", "Sheet");


    // Delete any empty p
    document.select("article > p:empty").remove();

    // For whatever reason floating text is outside a p tag, so add one back in.
    document.select("article:matchText").forEach(e -> {
      String text = e.text();
      if(!text.isEmpty() && !StringUtils.isBlank(text)) {
        e.wrap("<section></section>");
      }
    });
    document.select("article > section:empty").remove();

    return document;
  }

}
