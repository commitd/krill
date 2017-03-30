package io.committed.krill.extraction.pdfbox.interpretation;

import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * A very basic {@link ReadingOrder} that sorts blocks by their top Y coordinate (or, in the case of
 * {@link TextBlock}s, the baseline of the first line of text in the block) and then by minimum x
 * position.
 */
public class SimpleReadingOrder implements ReadingOrder {

  @Override
  public List<LabellablePositioned> order(Collection<LabellablePositioned> blocks) {
    List<LabellablePositioned> pageBlocks = new ArrayList<>();
    pageBlocks.addAll(blocks);

    Comparator<LabellablePositioned> byY = Comparator.comparing(this::getBaseline);
    Comparator<LabellablePositioned> byX = Comparator.comparing(s -> s.getPosition().getMinX());
    pageBlocks.sort(byY.thenComparing(byX));
    return pageBlocks;
  }

  /**
   * Gets the baseline.
   *
   * @param positioned
   *          the positioned
   * @return the baseline
   */
  private double getBaseline(LabellablePositioned positioned) {
    if (positioned instanceof TextBlock) {
      TextBlock textBlock = (TextBlock) positioned;
      if (!textBlock.getContents().isEmpty()) {
        Line line = textBlock.getContents().get(0);
        return line.getBaseline();
      }
    }
    return positioned.getPosition().getMinY();
  }

}
