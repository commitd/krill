package io.committed.krill.extraction.pdfbox.interpretation;

/**
 * Labels that can be applied to {@link LabellablePositioned} blocks by {@link
 * BlockTypeClassifier}s.
 */
public enum BlockTypeLabel {

  /** An unclassified block. */
  UNKNOWN,

  /** A block deemed to be part of the page header. */
  HEADER,

  /** A block deemed to be part of the page footer. */
  FOOTER,

  /** A block deemed to a page number. */
  PAGE_NUMBER,

  /** A text block that is deemed to be a caption for a nearby image. */
  CAPTION,

  /** An image block that has had a {@link #CAPTION} block associated with it. */
  IMAGE,

  /** A text block that is deemed to be a heading. */
  HEADING
}
