package io.committed.krill.extraction.pdfbox.physical;

/** Allows the baseline of text components to be retrieved. */
@FunctionalInterface
public interface Baselined {

  /**
   * Returns the baseline of some text.
   *
   * @return the baseline.
   */
  float getBaseline();
}
