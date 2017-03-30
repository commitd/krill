package io.committed.krill.extraction.tika.processors.helpers;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import java.util.Arrays;
import java.util.Collection;

/**
 * Removes the prefix (eg DOT) from list item.
 */
public final class RemovePrefixFromListItemNodeVisitor implements NodeVisitor {
  private final Collection<String> dots;

  public RemovePrefixFromListItemNodeVisitor(final String... dots) {
    this(Arrays.asList(dots));
  }

  public RemovePrefixFromListItemNodeVisitor(final Collection<String> dots) {
    this.dots = dots;
  }

  @Override
  public void tail(final Node node, final int depth) {
    // Do nothing
  }

  @Override
  public void head(final Node node, final int depth) {
    if (node instanceof TextNode) {
      final TextNode t = (TextNode) node;
      // Only doing dot here, but likely numbers, etc too (need to see examples in the wild!)
      final String s = t.text();
      for (final String dot : dots) {
        if (s.startsWith(dot)) {
          t.text(s.replaceFirst(dot + "\\s*", ""));
          break;
        }
      }
    }
  }
}
