package io.committed.krill.extraction.tika.processors;

import com.google.common.collect.Sets;

import io.committed.krill.extraction.tika.processors.helpers.RemovePrefixFromListItemNodeVisitor;
import org.apache.tika.metadata.Metadata;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.Set;

/**
 * Format Processor for PDF.
 * <p>
 * Attempts to wrap up a PDF as a document see {@link WordFormatProcessor} for details.
 * </p>
 * <p>
 * PDF has the advantage over Word that it understand pages. As such under the main class is a
 * article class=page tag which holds each element.
 * </p>
 */
public class PdfFormatProcessor extends AbstractJsoupFormatProcessor {

  private static final Set<String> COMMON_SUPERSCRIPTS = Sets.newHashSet("st", "nd", "rd", "th");

  // Within PDF this could be any symbol, but at least Word PDFs tend to be this...
  private static final Set<String> LISTITEM_SYMBOLS = Sets.newHashSet("", "•", " o ");

  @Override
  public Document process(final Metadata metadata, final Document document) {
    wrapChildrenOfBodyInTag(document, "<main class=\"Document\"></main>");

    // Remove common subscripts, etc as they aren't properly capture yet
    removeFloatingSuperscript(document);

    // Convert tables to lists - we get tables with rows: | - | List text |
    convertTablesToLists(document);

    // Convert split bullets. Some times we have <p>bullet</p><p>text</p> This is wehn the bullet
    // and the text are very far spaced
    convertSpiltBulletsToLists(document);

    // Convert paragraphs full of list item bullets to lists
    // That's when we have • something • something else • another where we've accidently created a
    // paragraph of them
    convertParagraphsToList(document);

    // Convert lists
    convertLists(document);

    // Add in page breaks between pages
    convertBreaksBetweenPages(document);

    return document;
  }

  private void convertBreaksBetweenPages(final Document document) {
    document.select("article.page ~ article.page").forEach(a -> {
      a.before("<hr class=\"pagebreak\" />");
    });
  }

  private void convertSpiltBulletsToLists(final Document document) {
    document.select("p ~ p").forEach(p -> {
      final Element previous = p.previousElementSibling();
      final String previousText = previous.text().trim();
      if (LISTITEM_SYMBOLS.stream().anyMatch(s -> s.equals(previousText))) {
        // So bullet then paragraph...
        // Delete prvious and turn the other into a list item
        previous.remove();
        p.tagName("li");
      }
    });
  }

  private void convertParagraphsToList(final Document document) {

    document.select("p,li,td").forEach(p -> {
      final String text = p.text();

      for (final String symbol : LISTITEM_SYMBOLS) {
        if (text.contains(symbol)) {
          p.tagName("ul");
          p.empty();
          for (final String s : text.split(symbol)) {
            p.appendElement("li").text(s);
          }

          // Only do this once!
          return;
        }
      }
    });

  }

  private void convertTablesToLists(final Document document) {
    // If a two column table, where the first col is always the same (and non-alphabetic), then drop
    // it and convert the other row to a list.

    document.select("table").forEach(table -> {
      String firstColumnValue = null;
      for (final Element tr : table.select("tr")) {
        final Elements cells = tr.select("td");
        if (cells.size() != 2) {
          return;
        }

        final Element cell = cells.first();
        final String first = cell.text().trim();
        if (firstColumnValue != null) {
          if (!firstColumnValue.equals(first)) {
            return;
          }
        } else {
          firstColumnValue = first;
        }
      }

      // If we are here then all the first columns are the same...
      // So we convert to a list...
      final Element ul = new Element(Tag.valueOf("ul"), "");
      for (final Element tr : table.select("tr")) {
        final Elements cells = tr.select("td");
        ul.appendElement("li").html(cells.last().html());
      }

      table.replaceWith(ul);

    });
  }

  private void removeFloatingSuperscript(final Document document) {
    final Elements allPs = document.select("p");
    allPs.removeIf(p -> !COMMON_SUPERSCRIPTS.contains(p.text().toLowerCase()));
    allPs.remove();
  }

  private void convertLists(final Document document) {
    // Convert p tags which start with a dot into listitems
    document.select("p").forEach(e -> {
      final String text = e.text();
      for (final String symbol : LISTITEM_SYMBOLS) {
        if (text.startsWith(symbol)) {
          e.tagName("li");
        }
      }
    });

    // Under an ul or ol should be a li
    document.select("ul,ol > p").forEach(l -> l.select("p").wrap("<li></li>"));

    // If not parent of li if not ul or ol then wrap with ul?
    wrapRunsOfChildTag(document, "li",
        p -> "ul".equalsIgnoreCase(p.tagName()) || "ol".equalsIgnoreCase(p.tagName()), "ul");

    // Remove the prefix on the list items
    document.select("li").forEach(
        element -> element.traverse(new RemovePrefixFromListItemNodeVisitor(LISTITEM_SYMBOLS)));
  }

}
