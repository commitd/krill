package io.committed.krill.extraction.tika.processors;

import org.apache.tika.metadata.Metadata;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Format processor for PowerPoint (PPT/PPTX).
 * <p>
 * The format of the output is as follows.
 * </p>
 *
 * <pre>
 * html
 * - body
 *   - main class=SlideShow
 *     - article class=Slide
 *       - details: content which fits into the master slide
 *       - section: the content for the actual slide
 *       - aside: the slide's speaker notes
 *     - article class=Slide ... repeating
 *     - aside: Often there are final notes at the end
 * </pre>
 * <p>
 * Note that:
 * </p>
 * <ul>
 * <li>PPTX outputs page number into the notes... if the notes exist
 * <li>PPTX does not have any list items, just paragraphs
 * <li>Neither format has grouping of information by text box
 * </ul>
 */
public class PowerpointFormatProcessor extends AbstractJsoupFormatProcessor {

  @Override
  public Document process(final Metadata metadata, final Document document) {

    // Delete all the junk and empty tags at the top level
    document.select("body > *:empty").remove();

    /// Add the top level main slideshow element
    wrapChildrenOfBodyInTag(document, "<main class=\"SlideShow\"></main>");

    if (!document.select("div.slideShow").isEmpty()) {
      // If we have the slideShow class remove it (as we've added the main above now)
      document.select("div.slideShow").unwrap();
    }

    convertToSlides(document);

    cleanUp(document);

    return document;
  }

  private void convertToSlides(final Document document) {
    // Some Ppt seems to have (particularly .ppt)
    // <div class="slide">
    // <div class=slide-master-content>...</div>
    // <div class=slide-content>...</div>
    // <div class=slide-notes>...</div>
    // </div>
    // But some .pptx doesn't wrap the div class=slide and instead is
    // <div class=slide-content>...</div>
    // <div class=slide-master-content>...</div>
    // <div class=slide-notes>...</div>
    // So...

    // Flatten out the structure
    if (!document.select("div.slide").isEmpty()) {
      // We have the nice way...
      document.select("div.slide").tagName("article").attr("class", "Slide");
      document.select("div.slide-master-content").tagName("details");
      document.select("div.slide-content").tagName("section");
      document.select("div.slide-notes").tagName("aside");
    } else {
      // Missing div.slide way...

      // Wrap up the main content
      document.select("div.slide-content").tagName("section").removeClass("slide-content")
          .wrap("<article class=\"Slide\"></article>");

      // Add slide-master-content to the article
      document.select("article.Slide + div.slide-master-content").forEach(e -> {
        final Element divSlideMasterContent = e.tagName("details");
        e.previousElementSibling().appendChild(divSlideMasterContent);
      });
      document.select("article.Slide + div.slide-notes").forEach(e -> {
        final Element divSlideNotes = e.tagName("aside");
        e.previousElementSibling().appendChild(divSlideNotes);
      });
    }
  }

  private void cleanUp(final Document document) {
    // Often we get p tag under a ul, ol, so lets clean that up as a li
    document.select("ul,ol > p").forEach(l -> l.select("p").wrap("<li></li>"));

    // Lots of paragraph tags with content '*', they aren't in the ppt so lets just remove them
    document.select("p:matches(^\\*$)").remove();

    // Remove any specific sections which are empty (as they are just noise)
    document.select("details:empty,section:empty,aside:empty").remove();

    // Remove the classes
    document.select(".slide-content,.slide-master-content,.slide-notes").removeAttr("class");

    // Powerpoint puts a preview embedded in.. which we don't want
    document.select("div[id=/docProps/thumbnail.jpeg]").remove();
  }
}
