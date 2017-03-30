package io.committed.krill.extraction.pdfbox.interpretation;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import io.committed.krill.extraction.pdfbox.physical.ImageBlock;
import io.committed.krill.extraction.pdfbox.physical.Positioned;
import io.committed.krill.extraction.pdfbox.physical.Style;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * A basic {@link BlockTypeClassifier} that uses simple heuristics to label blocks.
 */
public class SimpleBlockClassifier implements BlockTypeClassifier {

  /** The page sizes. */
  private final Map<Integer, Rectangle2D> pageSizes = new HashMap<>();

  /** The page blocks. */
  private final Map<Integer, Collection<LabellablePositioned>> pageBlocks = new HashMap<>();

  /** The footer region top. */
  private double footerRegionTop;

  /** The header region bottom. */
  private double headerRegionBottom;

  @Override
  public void addPage(int pageIndex, Collection<LabellablePositioned> blocks,
      Rectangle2D pageSize) {
    pageBlocks.put(pageIndex, blocks);
    pageSizes.put(pageIndex, pageSize);
  }

  @Override
  public void label() {
    labelHeader();
    labelFooter();

    labelImageCaptions();
    labelHeadings();
  }

  /**
   * Label header.
   */
  private void labelHeader() {
    Collection<LabellablePositioned> headerBlocks = new ArrayList<>();

    // find blocks in top 14% of pages
    Multimap<Integer, LabellablePositioned> topBlocks = HashMultimap.create();

    for (Entry<Integer, Collection<LabellablePositioned>> entry : pageBlocks.entrySet()) {
      Rectangle2D pageSize = pageSizes.get(entry.getKey());
      List<LabellablePositioned> top = getTopBlocks(pageSize, entry.getValue());
      topBlocks.putAll(entry.getKey(), top);
    }

    Map<Integer, Collection<LabellablePositioned>> allTopBlocks = topBlocks.asMap();
    for (Entry<Integer, Collection<LabellablePositioned>> outer : allTopBlocks.entrySet()) {
      Collection<LabellablePositioned> thisPageBlocks = outer.getValue();
      for (LabellablePositioned thisPageBlock : thisPageBlocks) {
        // find blocks with comparable position, with similar text on any other page
        labelCommonPositionedBlock(thisPageBlock, outer.getKey(), allTopBlocks,
            BlockTypeLabel.HEADER);
        if (thisPageBlock.getLabels().contains(BlockTypeLabel.HEADER)) {
          headerBlocks.add(thisPageBlock);
        }
      }
    }

    headerRegionBottom = headerBlocks.stream().mapToDouble(s -> s.getPosition().getMaxY()).max()
        .orElseGet(() -> 0);

    // label all blocks that fall inside the header region as headers
    for (Collection<LabellablePositioned> blocks : pageBlocks.values()) {
      for (LabellablePositioned labellablePositioned : blocks) {
        if (labellablePositioned.getPosition().getMaxY() < headerRegionBottom) {
          labellablePositioned.addLabel(BlockTypeLabel.HEADER);
        }
      }
    }
  }

  /**
   * Gets the bottom blocks.
   *
   * @param pageSize
   *          the page size
   * @param entry
   *          the entry
   * @return the bottom blocks
   */
  private List<LabellablePositioned> getBottomBlocks(Rectangle2D pageSize,
      Collection<? extends LabellablePositioned> entry) {
    return entry.stream().filter(f -> f.getPosition().getMinY() > pageSize.getMaxY() * 0.86)
        .collect(Collectors.toList());
  }

  /**
   * Gets the top blocks.
   *
   * @param pageSize
   *          the page size
   * @param entry
   *          the entry
   * @return the top blocks
   */
  private List<LabellablePositioned> getTopBlocks(Rectangle2D pageSize,
      Collection<? extends LabellablePositioned> entry) {
    return entry.stream().filter(f -> f.getPosition().getMaxY() < pageSize.getMaxY() * 0.14)
        .collect(Collectors.toList());
  }

  /**
   * Label common positioned block.
   *
   * @param thisPageBlock
   *          the this page block
   * @param page
   *          the page
   * @param otherPages
   *          the other pages
   * @param label
   *          the label
   */
  private void labelCommonPositionedBlock(LabellablePositioned thisPageBlock, int page,
      Map<Integer, Collection<LabellablePositioned>> otherPages, BlockTypeLabel label) {
    int similarBlockCount = 0;
    for (Entry<Integer, Collection<LabellablePositioned>> otherPage : otherPages.entrySet()) {
      if (otherPage.getKey().equals(page)) {
        continue;
      }
      Collection<LabellablePositioned> otherPageBlocks = otherPage.getValue();
      for (LabellablePositioned otherPageBlock : otherPageBlocks) {
        boolean similarPosition = similarPosition(thisPageBlock, otherPageBlock);
        if (similarPosition && otherPageBlock instanceof TextBlock
            && thisPageBlock instanceof TextBlock) {
          boolean similarText = similarText((TextBlock) thisPageBlock, (TextBlock) otherPageBlock);
          if (similarText) {
            similarBlockCount++;
          }
        } else if (similarPosition && otherPageBlock instanceof ImageBlock
            && thisPageBlock instanceof ImageBlock) {
          // could compare images, but assume they are the same
          similarBlockCount++;
          similarPosition(thisPageBlock, otherPageBlock);
        }
      }
    }
    if (similarBlockCount > (0.3 * otherPages.size())) {
      thisPageBlock.addLabel(label);
    }
  }

  /**
   * Similar position.
   *
   * @param firstBlock
   *          the first block
   * @param secondBlock
   *          the second block
   * @return true, if successful
   */
  private static boolean similarPosition(LabellablePositioned firstBlock,
      LabellablePositioned secondBlock) {
    Rectangle2D first = firstBlock.getPosition();
    Rectangle2D second = secondBlock.getPosition();
    boolean similarWidth = withinTolerance(first.getWidth(), second.getWidth(),
        Math.max(first.getWidth(), second.getWidth()) * 0.3);
    boolean similarHeight = withinTolerance(first.getHeight(), second.getHeight(),
        Math.max(first.getHeight(), second.getHeight()) * 0.3);
    boolean similarX = withinTolerance(first.getX(), second.getX(),
        Math.max(first.getWidth(), second.getWidth()) * 0.3);
    boolean similarY = withinTolerance(first.getY(), second.getY(),
        Math.max(first.getHeight(), second.getHeight()) * 0.3);
    return similarWidth && similarHeight && similarX && similarY;
  }

  /**
   * Label footer.
   */
  private void labelFooter() {
    // find blocks in bottom 14% of pages
    Multimap<Integer, LabellablePositioned> topBlocks = HashMultimap.create();
    Collection<LabellablePositioned> footerBlocks = new ArrayList<>();
    for (Entry<Integer, Collection<LabellablePositioned>> entry : pageBlocks.entrySet()) {
      Rectangle2D pageSize = pageSizes.get(entry.getKey());
      List<LabellablePositioned> top = getBottomBlocks(pageSize, entry.getValue());
      topBlocks.putAll(entry.getKey(), top);
    }

    Map<Integer, Collection<LabellablePositioned>> allBlocks = topBlocks.asMap();

    for (Entry<Integer, Collection<LabellablePositioned>> outer : allBlocks.entrySet()) {
      Collection<LabellablePositioned> thisPageBlocks = outer.getValue();
      for (LabellablePositioned thisPageBlock : thisPageBlocks) {
        // find blocks with comparable position, with similar text on any other page
        labelCommonPositionedBlock(thisPageBlock, outer.getKey(), allBlocks, BlockTypeLabel.FOOTER);
        if (thisPageBlock.getLabels().contains(BlockTypeLabel.FOOTER)) {
          footerBlocks.add(thisPageBlock);
        }
      }
    }

    footerRegionTop = footerBlocks.stream().mapToDouble(s -> s.getPosition().getMinY()).min()
        .orElseGet(() -> Double.MAX_VALUE);

    // label all blocks that fall inside the footer region as headers
    for (Collection<LabellablePositioned> blocks : pageBlocks.values()) {
      for (LabellablePositioned labellablePositioned : blocks) {
        if (labellablePositioned.getPosition().getMinY() > footerRegionTop) {
          labellablePositioned.addLabel(BlockTypeLabel.FOOTER);
          if (isPageNumber(labellablePositioned)) {
            labellablePositioned.addLabel(BlockTypeLabel.PAGE_NUMBER);
          }
        }
      }
    }

  }

  /**
   * Checks if is page number.
   *
   * @param labellablePositioned
   *          the labellable positioned
   * @return true, if is page number
   */
  private boolean isPageNumber(LabellablePositioned labellablePositioned) {
    if (labellablePositioned instanceof TextBlock) {
      TextBlock textBlock = (TextBlock) labellablePositioned;
      String string = textBlock.toString().trim();
      if (string.chars().allMatch(Character::isDigit)
          || string.toLowerCase(Locale.ROOT).startsWith("page")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Label image captions.
   */
  private void labelImageCaptions() {
    for (Entry<Integer, Collection<LabellablePositioned>> entry : pageBlocks.entrySet()) {
      List<ImageBlock> imageBlocks = entry.getValue().stream().filter(s -> s instanceof ImageBlock)
          .map(s -> (ImageBlock) s).collect(Collectors.toList());
      for (ImageBlock imageBlock : imageBlocks) {
        TextBlock caption = findAdjacentBlock(imageBlock, entry.getValue());
        if (caption != null) {
          imageBlock.addLabel(BlockTypeLabel.IMAGE);
          caption.addLabel(BlockTypeLabel.CAPTION);
        }
      }
    }
  }

  /**
   * Overlap horizontally.
   *
   * @param first
   *          the first
   * @param second
   *          the second
   * @param distance
   *          the distance
   * @return true, if successful
   */
  private static boolean overlapHorizontally(Positioned first, Positioned second, float distance) {
    Rectangle2D firstPosition = first.getPosition();
    Rectangle2D secondPosition = second.getPosition();
    double firstMinX = secondPosition.getMinX();
    double firstMaxX = secondPosition.getMaxX();
    double secondMinX = firstPosition.getMinX();
    double secondMaxX = firstPosition.getMaxX();
    return (firstMinX < secondMaxX) && ((secondMinX - firstMaxX) < distance);
  }

  /**
   * Adjacent vertically.
   *
   * @param first
   *          the first
   * @param second
   *          the second
   * @param distance
   *          the distance
   * @return true, if successful
   */
  private static boolean adjacentVertically(Positioned first, Positioned second, double distance) {
    Rectangle2D firstPosition = first.getPosition();
    Rectangle2D secondPosition = second.getPosition();
    if (second.getPosition().isEmpty()) {
      return false;
    }
    return withinTolerance(secondPosition.getMinY(), firstPosition.getMaxY(), distance)
        || withinTolerance(secondPosition.getMaxY(), firstPosition.getMinY(), distance);
  }

  /**
   * Label headings.
   */
  private void labelHeadings() {
    for (Entry<Integer, Collection<LabellablePositioned>> entry : pageBlocks.entrySet()) {
      List<TextBlock> textBlocks = entry.getValue().stream().filter(s -> s instanceof TextBlock)
          .map(s -> (TextBlock) s).collect(Collectors.toList());
      for (TextBlock textBlock : textBlocks) {
        TextBlock candidate = findAdjacentBlock(textBlock, entry.getValue());
        if (candidate == null) {
          continue;
        }
        if ((candidate.getContents().size() < 4) && isMoreSignificant(candidate, textBlock)) {
          candidate.addLabel(BlockTypeLabel.HEADING);
        }
      }
    }
  }

  /**
   * Find adjacent block.
   *
   * @param source
   *          the source
   * @param otherBlocks
   *          the other blocks
   * @return the text block
   */
  private TextBlock findAdjacentBlock(LabellablePositioned source,
      Collection<LabellablePositioned> otherBlocks) {
    double lineHeight;
    if (source instanceof TextBlock) {
      TextBlock textBlock = (TextBlock) source;
      lineHeight = textBlock.getContents().get(0).getPosition().getHeight() * 5;
    } else {
      lineHeight = 12;
    }
    for (LabellablePositioned block : otherBlocks) {
      if (!(block instanceof TextBlock)) {
        continue;
      }
      TextBlock textBlock = (TextBlock) block;
      double distance = source.getPosition().getMaxY() - textBlock.getPosition().getMaxY();
      boolean isAbove = distance > 0 && distance < lineHeight;
      if (overlapHorizontally(source, textBlock, 10f)
          && adjacentVertically(source, textBlock, lineHeight) && isAbove) {
        return textBlock;
      }
    }
    return null;
  }

  /**
   * Checks if is more significant.
   *
   * @param candidateBlock
   *          the candidate block
   * @param referenceBlock
   *          the reference block
   * @return true, if is more significant
   */
  private boolean isMoreSignificant(TextBlock candidateBlock, TextBlock referenceBlock) {
    Style candidate = candidateBlock.getStyle();
    Style reference = referenceBlock.getStyle();
    boolean bigger = candidate.getSize() > reference.getSize();
    boolean bold = candidate.isBold() && !reference.isBold();
    boolean italic = candidate.isItalic() && !reference.isItalic();
    boolean moreShouting = mainlyCaps(candidateBlock) && !mainlyCaps(referenceBlock);
    return moreShouting || bigger || bold || italic;
  }

  /**
   * Mainly caps.
   *
   * @param block
   *          the block
   * @return true, if successful
   */
  private boolean mainlyCaps(TextBlock block) {
    String string = block.toString();
    long upperCount = string.codePoints().filter(Character::isUpperCase).count();
    long count = string.codePoints().filter(Character::isWhitespace).count();
    return upperCount > (string.length() - count) * 0.85f;
  }

  @Override
  public Collection<LabellablePositioned> getBlocks(int pageIndex) {
    return pageBlocks.getOrDefault(pageIndex, Collections.emptyList());
  }

  @Override
  public double getFooterRegionTop() {
    return footerRegionTop;
  }

  @Override
  public double getHeaderRegionBottom() {
    return headerRegionBottom;
  }

  /**
   * Within tolerance.
   *
   * @param first
   *          the first
   * @param second
   *          the second
   * @param tolerance
   *          the tolerance
   * @return true, if successful
   */
  private static boolean withinTolerance(double first, double second, double tolerance) {
    return Math.abs(first - second) <= tolerance;
  }

  /**
   * Similar text.
   *
   * @param thisPageBlock
   *          the this page block
   * @param otherPageBlock
   *          the other page block
   * @return true, if successful
   */
  private static boolean similarText(TextBlock thisPageBlock, TextBlock otherPageBlock) {
    return cleanText(thisPageBlock).equals(cleanText(otherPageBlock));
  }

  /**
   * Clean text - replaces any sequence of digits with 0.
   * <p>
   * Used for determining whether page numbers, chapter header text, are common to multiple pages.
   * </p>
   * 
   * @param otherPageBlock
   *          the other page block
   * @return the string
   */
  private static String cleanText(TextBlock otherPageBlock) {
    return otherPageBlock.toString().replaceAll("\\d+", "0");
  }

  @Override
  public Rectangle2D getPageSize(int pageNumber) {
    return pageSizes.get(pageNumber);
  }

}
