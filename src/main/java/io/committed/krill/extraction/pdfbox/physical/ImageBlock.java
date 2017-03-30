package io.committed.krill.extraction.pdfbox.physical;

import io.committed.krill.extraction.pdfbox.interpretation.BlockTypeLabel;
import io.committed.krill.extraction.pdfbox.interpretation.LabellablePositioned;

import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the location of an image in a page (does not contain the image).
 */
public class ImageBlock implements LabellablePositioned {

  /** The position. */
  private final Rectangle2D position;

  /** The labels. */
  private EnumSet<BlockTypeLabel> labels = EnumSet.noneOf(BlockTypeLabel.class);

  /**
   * Create a new {@link ImageBlock} at the given position.
   *
   * @param position
   *          the position.
   */
  public ImageBlock(Rectangle2D position) {
    this.position = position;
  }

  @Override
  public Rectangle2D getPosition() {
    return position;
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
