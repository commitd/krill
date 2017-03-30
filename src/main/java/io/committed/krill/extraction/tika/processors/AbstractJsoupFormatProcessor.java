package io.committed.krill.extraction.tika.processors;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import org.apache.tika.metadata.Metadata;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.function.Predicate;

/**
 * Abstract base {@link FormatProcessor} providing support methods.
 */
public abstract class AbstractJsoupFormatProcessor implements FormatProcessor {

  @Override
  public final String process(final Metadata metadata, final String html) {
    final Document input = Jsoup.parse(html);
    final Document output = process(metadata, input);
    return output.html();
  }

  /**
   * Process the JSoup document.
   *
   * @param metadata
   *          the metadata
   * @param document
   *          the document (which may be manipulated and returned)
   * @return the document (typically will be the same as document parameter)
   */
  public abstract Document process(final Metadata metadata, final Document document);

  /**
   * Helper to wrap all children of the body element in a particular tag.
   * <p>
   * Useful for adding main class="Document" around all children to signify the type.
   * </p>
   *
   * @param document
   *          the document
   * @param html
   *          the HTML to wrap (just like Jsoup's Element.wrap() function)
   */
  protected void wrapChildrenOfBodyInTag(final Document document, final String html) {
    final Elements children = document.body().children().remove();
    document.body().append(html);
    final Element main = document.body().children().first();
    main.insertChildren(0, children);
  }

  /**
   * Wraps runs of a tag with a parent tag.
   * <p>
   * Jsoup's wrap wraps each element in another tag. But often we want to wrap a group of
   * consecutive elements with under a single (new) parent. For example, a run of tr (table row)
   * tags should be at least wrapped in a table.
   * </p>
   *
   * @param document
   *          the document
   * @param childTag
   *          the child tag we are look for runs of
   * @param parentAlreadyValid
   *          a test to determine if a child is already correct wrapped by a parent
   * @param wrappingParentTag
   *          the parent tag to wrap with (if a valid parent is not already there)
   */
  protected void wrapRunsOfChildTag(final Document document, final String childTag,
      final Predicate<Element> parentAlreadyValid, final String wrappingParentTag) {
    final Multimap<Element, Element> map = LinkedHashMultimap.create();

    // Build a map of parent to li child
    document.select(childTag).forEach(e -> {
      final Element p = e.parent();
      if (!parentAlreadyValid.test(p)) {
        map.put(p, e);
      }
    });

    map.asMap().forEach((parent, children) -> {
      Element ul = null;
      // Add ul for each run of li's
      for (final Element c : children) {
        if (ul == null || (ul.nextElementSibling() != null && !ul.nextElementSibling().equals(c))) {
          ul = new Element(Tag.valueOf(wrappingParentTag), "");
          c.replaceWith(ul);
        }

        ul.appendChild(c);
      }
    });
  }
}
