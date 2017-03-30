package io.committed.krill.extraction.tika.processors;

import io.committed.krill.extraction.tika.processors.helpers.RemovePrefixFromListItemNodeVisitor;
import org.apache.tika.metadata.Metadata;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Format processor for Word DOC / DOCX files.
 *
 * <p>
 * The output structure is as follows:
 * </p>
 *
 * <pre>
 * html
 * - body
 *   - main class=Document (elements under here can appear in any order, multiple times)
 *     - header
 *     - footer
 *     - table
 *     - details: a footnote
 *     - p
 *     - img: an image
 *     - figcaption: caption for table or image
 *     - a anchor or link
 *     - blockquote
 *     - h1-6
 *     - ul (ol)
 *
 * </pre>
 * <p>
 * Note that:
 * </p>
 * <ul>
 * <li>There is no page information.. Word is not that type of processor
 * <li>The location of footnote varies between DOC and DOCX. In DOC is near where it would be at the
 * bottom of the page. In DOCX is it placed within the element where it is a footnote for</li>
 * <li>This class makes to attempt to extract ordered lists from numbered list items. There is too
 * much ambiguity around numbered headers, numbered paragraphs and numbered list items. Users
 * wishing to get this type of information should implement logical for a particular document
 * template.
 * <li>Only certain style information is provided by tika, limited to bold, italics and strike
 * through (DOC only).
 * </ul>
 */
public class WordFormatProcessor extends AbstractJsoupFormatProcessor {

  private static final String CLASS = "class";

  /** The marker in DOC files for the start of a footnote. */
  private static final String DOC_FOOTNOTE_MARKER = "�";

  /** Marker for the originating location of a footnote in DOCX. */
  private static final String DOCX_FOOTNOTE_REFERENCE_REGEX = "\\[footnoteRef:\\d+\\]";

  private static final Pattern DOCX_FOOTNOTE_REFERENCE_REGEX_PATTERN = Pattern
      .compile(DOCX_FOOTNOTE_REFERENCE_REGEX);

  /** DOCX structure for the content of a footnote. */
  private static final String DOCX_FOOTNOTE_CONTENT_REGEX = "\\s*\\[\\d+:\\s*" + "(?<content>.*?)"
      + "\\]\\s*";

  private static final Pattern DOCX_FOOTNOTE_CONTENT_REGEX_PATTERN = Pattern
      .compile(DOCX_FOOTNOTE_CONTENT_REGEX);

  /** The DOT which is used to indicate a bullet point (list item) in DOC. */
  private static final String DOT = "·";

  @Override
  public Document process(final Metadata metadata, final Document document) {

    // Under body add a document main
    wrapChildrenOfBodyInTag(document, "<main class=\"Document\"></main>");

    convertHeaderAndFooters(document);

    convertLists(document);

    convertFootnotes(document);

    convertCaptions(document);

    convertQuotes(document);

    // Word puts a preview embedded in.. which we don't want
    document.select("div[id=/docProps/thumbnail.jpeg]").remove();

    // DOCX: Often as a goback link at the bottom, remove...
    final Elements goBack = document.select("p > a[name=_GoBack]:empty");
    if (!goBack.isEmpty()) {
      final Element parent = goBack.parents().first();
      goBack.remove();
      if (parent.childNodeSize() == 0) {
        parent.remove();
      }
    }

    // Change tag of pagebreaks
    changeBreaks(document);

    // Some time have additional information in the classes (eg subtitle)
    removeClassFromHeadings(document);

    return document;
  }

  /**
   * Removes the CSS class from headings.
   *
   * @param document
   *          the document
   */
  private void removeClassFromHeadings(final Document document) {
    document.select("h1,h2,h3,h4,h5,h6").removeAttr(CLASS);

  }

  /**
   * Change the section and page breaks from p to hrs.
   *
   * @param document
   *          the document
   */
  private void changeBreaks(final Document document) {
    document.select("p.pagebreak,p.sectionbreak").tagName("hr");
  }

  /**
   * Convert quotes to HTML5 quote types.
   *
   * @param document
   *          the document
   */
  private void convertQuotes(final Document document) {
    document.select("p.quote,p.intense_Quote").tagName("blockquote").removeAttr(CLASS);

    // In Doc, under the blockquote if often italics, which si just 'the style of the blockquote' so
    // we remove that
    document.select("blockquote > i").unwrap();
  }

  /**
   * Convert captions to HTML5 caption types.
   *
   * @param document
   *          the document
   */
  private void convertCaptions(final Document document) {
    document.select("p.caption").tagName("figcaption").removeAttr(CLASS);

    // In Doc: caption get style of italics by default
    document.select("figcaption > i").unwrap();
  }

  /**
   * Convert header and footers to HTML header and footers.
   *
   * @param document
   *          the document
   */
  private void convertHeaderAndFooters(final Document document) {
    // Correct div tags to header type
    document.select("div.header").tagName("header").removeAttr(CLASS);
    document.select("div.footer").tagName("footer").removeAttr(CLASS);

    // In word all the paragraph under the header and footer will carry the class=header... we
    // remove those to so we have one header and one footer (not lots nested)
    document.select("header .header").removeAttr(CLASS);
    document.select("footer .footer").removeAttr(CLASS);

    // Sometimes paragraphs in the document get the header/footer style thanks to copy and paste,
    // delete that misleading stuff too
    document.select("p.header").removeAttr(CLASS);
    document.select("p.footer").removeAttr(CLASS);
  }

  /**
   * Convert paragraphs which are lists into HTML list and list item types.
   *
   * @param document
   *          the document
   */
  private void convertLists(final Document document) {

    // Correct paragraph tags
    document.select("p.list_paragraph,p.list_Paragraph").tagName("li").removeAttr(CLASS);

    // Convert p tags which start with a dot into listitems
    document.select("p").forEach(e -> {
      if (e.text().startsWith(DOT)) {
        e.tagName("li");
      }
    });

    // Under an ul or ol should be a li
    document.select("ul,ol > p").forEach(l -> l.select("p").wrap("<li></li>"));

    // If not parent of li if not ul or ol then wrap with ul?
    wrapRunsOfChildTag(document, "li",
        p -> "ul".equalsIgnoreCase(p.tagName()) || "ol".equalsIgnoreCase(p.tagName()), "ul");

    // Remove the prefix on the list items
    document.select("li")
        .forEach(element -> element.traverse(new RemovePrefixFromListItemNodeVisitor(DOT)));
  }

  /**
   * Convert footnotes into HTML asides.
   *
   * @param document
   *          the document
   */
  private void convertFootnotes(final Document document) {
    // Footnotes:
    // In Docx they are pulled out as [footnoteRef:1] then a some point [1:The text of the footnote]
    // In Doc we only have the footnote marked by a <?> character starting the paragraph (no
    // reference).

    // So for DOC:
    document.select("p:containsOwn(" + DOC_FOOTNOTE_MARKER + ")").forEach(p -> {
      p.tagName("details").attr(CLASS, "footnode");
      final String newText = p.text().replace(DOC_FOOTNOTE_MARKER, "");
      p.text(newText);
    });

    // For DOCX, first remove the footnote reference
    document.select("*:matchesOwn(" + DOCX_FOOTNOTE_REFERENCE_REGEX + ")").forEach(p -> {
      final String newText = DOCX_FOOTNOTE_REFERENCE_REGEX_PATTERN.matcher(p.ownText())
          .replaceAll("");
      p.text(newText);
    });
    // Then for DOCX mark the footnote text as a footnote
    document.select("*:matchesOwn(" + DOCX_FOOTNOTE_CONTENT_REGEX + ")").forEach(p -> {

      // We delete the footnote text...
      final String oldText = p.ownText();
      final String newText = DOCX_FOOTNOTE_CONTENT_REGEX_PATTERN.matcher(p.ownText())
          .replaceAll("");
      p.text(newText);

      // We create new elements for each footnote in this element.
      // Currently we are creating them as children here, but that's different to DOC (which
      // puts them randomly somewhere)
      final Matcher matcher = DOCX_FOOTNOTE_CONTENT_REGEX_PATTERN.matcher(oldText);
      while (matcher.find()) {
        final Element e = document.createElement("details").addClass("footnote")
            .text(matcher.group("content"));
        p.appendChild(e);
      }
    });
  }

}
