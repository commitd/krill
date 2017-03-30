package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.interpretation.LabellablePositioned;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Text;

import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.List;

/**
 * Segments extracted sequences of text from a PDF into a collection of labelled and positioned
 * blocks on the page.
 */
@FunctionalInterface
public interface PageSegmenter {

  /**
   * Segments the given sequences of text into blocks, potentially using the lines to assist.
   *
   * @param sequences
   *          the text sequences.
   * @param lines
   *          the lines
   * @return the identified and labelled semantic blocks.
   */
  Collection<LabellablePositioned> segment(List<PositionedContainer<Text>> sequences,
      Collection<Line2D> lines);

}
