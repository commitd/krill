package io.committed.krill.extraction.pdfbox.text;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeTraverser;

import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Style;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;
import io.committed.krill.extraction.pdfbox.text.RecursiveXyCut.TreeNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

/**
 * A {@link LineBlockGrouper} that groups lines together that are within the blocks identified by
 * applying the recursive X-Y cut algorithm, using proximity heuristics.
 */
public class XyCutBlockGrouper implements LineBlockGrouper {

  @Override
  public List<TextBlock> group(Collection<Line> lines) {
    Map<Style, Collection<Line>> styledGroups = groupByStyle(lines).asMap();
    List<TextBlock> blocks = new ArrayList<>();
    for (Entry<Style, Collection<Line>> styledGroup : styledGroups.entrySet()) {
      blocks.addAll(findBlocks(styledGroup.getValue()));
    }
    return blocks;
  }

  /**
   * Group by style.
   *
   * @param lines
   *          the lines
   * @return the multimap
   */
  private static Multimap<Style, Line> groupByStyle(Collection<Line> lines) {
    Multimap<Style, Line> styledLines = HashMultimap.create();
    lines.forEach(l -> styledLines.put(l.getStyle(), l));
    return styledLines;
  }

  /**
   * Find blocks.
   *
   * @param lines
   *          the lines
   * @return the list
   */
  private List<TextBlock> findBlocks(Collection<Line> lines) {
    List<Line> sortedLines = LineSpacingUtils.sortByBaseline(lines);

    TreeNode<PositionedContainer<Line>> root = new TreeNode<>(
        new PositionedContainer<Line>(new ArrayList<>(sortedLines)));

    new RecursiveXyCut<Line>(5, 5).apply(root);

    List<TextBlock> blocks = new ArrayList<>();
    TreeTraverser<TreeNode<PositionedContainer<Line>>> traverser = TreeTraverser
        .using(TreeNode::getChildren);
    traverser.preOrderTraversal(root).forEach(s -> {
      List<Line> contents = s.getData().getContents();
      if (!contents.isEmpty()) {
        blocks.add(new TextBlock(contents));
      }
    });

    // attempt to merge blocks together until no more can be merged
    int previousBlockCount = 0;
    while (blocks.size() != previousBlockCount) {
      previousBlockCount = blocks.size();
      attemptMergeOneBlock(blocks, this::shouldMergeX);
      if (blocks.size() == previousBlockCount) {
        attemptMergeOneBlock(blocks, this::shouldMergeY);
      }
    }

    return blocks;
  }

  /**
   * Attempt merge one block.
   *
   * @param blocks
   *          the blocks
   * @param mergeTester
   *          the merge tester
   */
  private void attemptMergeOneBlock(List<TextBlock> blocks,
      BiFunction<TextBlock, TextBlock, Boolean> mergeTester) {
    ListIterator<TextBlock> outer = blocks.listIterator();
    while (outer.hasNext()) {
      TextBlock first = outer.next();
      ListIterator<TextBlock> inner = blocks.listIterator();
      while (inner.hasNext()) {
        TextBlock second = inner.next();
        if (first == second) {
          continue;
        }

        if (mergeTester.apply(first, second)) {
          blocks.remove(first);
          blocks.remove(second);
          List<Line> combinedLines = new ArrayList<>();
          combinedLines.addAll(first.getContents());
          combinedLines.addAll(second.getContents());
          blocks.add(new TextBlock(combinedLines));
          return;
        }
      }
    }

  }

  /**
   * Should merge X.
   *
   * @param first
   *          the first
   * @param second
   *          the second
   * @return true, if successful
   */
  private boolean shouldMergeX(TextBlock first, TextBlock second) {
    double firstMaxX = first.getPosition().getMaxX();
    double secondMinX = second.getPosition().getMinX();
    // Compare the baselines - if they aren't the same these are on different lines, so don't merge.
    // We merge by X first and create a block for each line, these will only ever contain one line.
    if (Float.compare(first.getContents().get(0).getBaseline(),
        second.getContents().get(0).getBaseline()) != 0) {
      return false;
    }

    int firstLength = first.toString().length();
    double characterWidth = first.getPosition().getWidth() / firstLength;

    // Allow adjacent blocks to be merged if they are close enough
    double tolerance = 3 * characterWidth;
    return (firstMaxX < secondMinX) && ((secondMinX - firstMaxX) < tolerance);
  }

  /**
   * Should merge Y.
   *
   * @param first
   *          the first
   * @param second
   *          the second
   * @return true, if successful
   */
  protected boolean shouldMergeY(TextBlock first, TextBlock second) {
    double firstMinX = first.getPosition().getMinX();
    double firstMaxX = first.getPosition().getMaxX();
    double secondMinX = second.getPosition().getMinX();
    double secondMaxX = second.getPosition().getMaxX();

    // overlapping X (blocks are sorted by Y then X!)
    if (!(firstMinX <= secondMaxX && secondMinX <= firstMaxX)) {
      return false;
    }

    // are spacings within the groups similar?
    int firstSpacing = LineSpacingUtils.mostFrequentBaselineSpacing(first.getContents());
    int secondSpacing = LineSpacingUtils.mostFrequentBaselineSpacing(second.getContents());
    if (!((firstSpacing == 0 || secondSpacing == 0) ? true
        : withinTolerance(firstSpacing, secondSpacing, 0.1))) {
      return false;
    }

    // is the first line of the new group close enough to the last line of the existing group?
    float firstLastBaseline = first.getContents().get(first.getContents().size() - 1).getBaseline();
    double firstLineHeight = first.getContents().get(first.getContents().size() - 1).getPosition()
        .getHeight();
    float secondFirstBaseline = second.getContents().get(0).getBaseline();

    int firstLineSpacing = (int) (0.75
        * LineSpacingUtils.mostFrequentBaselineSpacing(first.getContents()));
    int secondLineSpacing = (int) (0.75
        * LineSpacingUtils.mostFrequentBaselineSpacing(first.getContents()));

    int spacingDifference = Math.round(Math.abs(firstLastBaseline - secondFirstBaseline));
    if (spacingDifference > firstLineHeight * 2
        || !withinTolerance(firstLineSpacing, secondLineSpacing, 0.25f)) {
      return false;
    }

    return true;
  }

  /**
   * Within tolerance.
   *
   * @param x1
   *          the x 1
   * @param x2
   *          the x 2
   * @param tolerance
   *          the tolerance
   * @return true, if successful
   */
  private boolean withinTolerance(double x1, double x2, double tolerance) {
    if (Double.compare(x2, 0) == 0) {
      return true;
    }
    double ratio = x1 / x2;
    return Math.abs(1 - ratio) < tolerance;
  }

}
