package io.committed.krill.extraction.pdfbox.physical;

import io.committed.krill.extraction.pdfbox.interpretation.BlockTypeLabel;
import io.committed.krill.extraction.pdfbox.interpretation.LabellablePositioned;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a block of text in a page.
 */
public class TextBlock extends PositionedStyledContainer<Line> implements LabellablePositioned {

  /** The labels. */
  private EnumSet<BlockTypeLabel> labels = EnumSet.noneOf(BlockTypeLabel.class);

  /**
   * Create a new {@link TextBlock} with the given {@link Line}s of content.
   *
   * @param content
   *          the content.
   */
  public TextBlock(List<Line> content) {
    super(content);
  }

  @Override
  public Set<BlockTypeLabel> getLabels() {
    return labels;
  }

  @Override
  public void addLabel(BlockTypeLabel label) {
    this.labels = EnumSet.of(label, labels.toArray(new BlockTypeLabel[labels.size()]));
    if (label != BlockTypeLabel.UNKNOWN) {
      labels.remove(BlockTypeLabel.UNKNOWN);
    }
  }

}
