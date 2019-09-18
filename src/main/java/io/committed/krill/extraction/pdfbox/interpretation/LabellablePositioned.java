package io.committed.krill.extraction.pdfbox.interpretation;

import io.committed.krill.extraction.pdfbox.physical.Positioned;
import java.util.Set;

/**
 * Allows a {@link Positioned} item to be labelled by a {@link BlockTypeClassifier} and those labels
 * to be later processed for rendering.
 */
public interface LabellablePositioned extends Positioned {

  /**
   * Returns the set of {@link BlockTypeLabel} for this {@link Positioned} item.
   *
   * @return a {@link Set} of {@link BlockTypeLabel}s.
   */
  Set<BlockTypeLabel> getLabels();

  /**
   * Adds a label to the set - not intended to be called by client code, only {@link
   * BlockTypeClassifier}s.
   *
   * @param label the label to add.
   */
  void addLabel(BlockTypeLabel label);
}
