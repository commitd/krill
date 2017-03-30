package io.committed.krill.extraction.pdfbox.interpretation;

import java.awt.geom.Rectangle2D;
import java.util.Collection;

/**
 * Once blocks have been extracted, a {@link BlockTypeClassifier} can be used to label them with
 * {@link BlockTypeLabel}s.
 *
 * <p>
 * {@link #addPage(int, Collection, Rectangle2D)} is called to add pages to the classifier (ideally
 * all pages, as implementations may wish to compare the locations of blocks between pages). Once
 * this has been completed, {@link #label()} must be called to process the pages. Then the other
 * methods can be called to retrieve data as needed.
 * </p>
 *
 */
public interface BlockTypeClassifier {

  /**
   * Adds pages to this classifier.
   *
   * @param pageIndex
   *          the 0-indexed page number.
   * @param blocks
   *          the blocks for the page.
   * @param cropBox
   *          the "crop box" for the page.
   */
  void addPage(int pageIndex, Collection<LabellablePositioned> blocks, Rectangle2D cropBox);

  /**
   * Life-cycle method to invoke labelling of added blocks.
   */
  void label();

  /**
   * Returns the blocks for a given page that this classifier was asked to classify.
   *
   * @param pageIndex
   *          the 0-indexed page number.
   * @return the blocks for the page, in no prescribed order.
   */
  Collection<LabellablePositioned> getBlocks(int pageIndex);

  /**
   * Gets the y coordinate of the bottom of the header region.
   *
   * @return the y coordinate of the bottom of the header region, or (note negative)
   *         -{@link Double#MAX_VALUE}, if there is no header region.
   */
  double getHeaderRegionBottom();

  /**
   * Gets the y coordinate of the top of the footer region.
   *
   * @return the y coordinate of the top of the footer region, or {@link Double#MAX_VALUE}, if there
   *         is no header region.
   */
  double getFooterRegionTop();

  /**
   * Return the size of the page.
   * 
   * @param pageNumber
   *          the page
   * @return the size
   */
  Rectangle2D getPageSize(int pageNumber);

}
