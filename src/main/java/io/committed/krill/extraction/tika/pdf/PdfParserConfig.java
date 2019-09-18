package io.committed.krill.extraction.tika.pdf;

import java.io.Serializable;

public class PdfParserConfig implements Serializable {

  /** generated serialisation id. */
  private static final long serialVersionUID = -9072751151203453308L;

  /** The emit absolute positioning flag. */
  private boolean emitAbsolutePositioning = false;

  /** The ignore terse table extraction flag. */
  private boolean ignoreTerseTables = false;

  /** The disable grid table extraction flag. */
  private boolean disableGridTableExtraction = false;

  /** The disable simple table extraction flag. */
  private boolean disableSimpleTableExtraction = false;

  /** The maximum baseline separation multiplier. */
  private float maxBaselineSeparationMultiplier = 3f;

  /** The X-Y cut x spacing for grouping. */
  private int groupXSpacing = 5;

  /** The X-Y cut y spacing for grouping. */
  private int groupYSpacing = 5;

  /** The X-Y cut projection scale for grouping. */
  private int xyCutProjectionScale = 10;

  /** The group spacing tolerance. */
  private double spacingGroupTolerance = 0.1;

  /** The break spacing tolerance. */
  private double spacingBreakTolerance = 0.25;

  /** The character spacing tolerance. */
  private double spacingCharacterTolerance = 3.0;

  /**
   * Get the flag for emitting the absolute position.
   *
   * @return flag to emit absolute positioning
   */
  public boolean isEmitAbsolutePositioning() {
    return emitAbsolutePositioning;
  }

  /**
   * Get the max baseline separation multiplier.
   *
   * @return the max baseline separation multiplier
   */
  public float getMaxBaselineSeprationMultiplier() {
    return maxBaselineSeparationMultiplier;
  }

  /**
   * Get the x spacing for the X-Y cut grouping algorithm.
   *
   * @return the x spacing
   */
  public int getGroupXSpacing() {
    return groupXSpacing;
  }

  /**
   * Get the y spacing for the X-Y cut grouping algorithm.
   *
   * @return the y spacing
   */
  public int getGroupYSpacing() {
    return groupYSpacing;
  }

  /**
   * Get the X-Y cut grouping projection scale.
   *
   * @return the xy cut projection scale
   */
  public int getXyCutProjectionScale() {
    return xyCutProjectionScale;
  }

  /**
   * Get the spacing group tolerance.
   *
   * @return the tolerance for the group spacing
   */
  public double getSpacingCharacterTolerance() {
    return spacingCharacterTolerance;
  }

  /**
   * Get the spacing group tolerance.
   *
   * @return the tolerance for the group spacing
   */
  public double getSpacingGroupTolerance() {
    return spacingGroupTolerance;
  }

  /**
   * Get the spacing break tolerance.
   *
   * @return the tolerance for the break spacing
   */
  public double getSpacingBreakTolerance() {
    return spacingBreakTolerance;
  }

  /**
   * Get the ignore terse tables flags.
   *
   * @return should ignore tables
   */
  public boolean isIgnoreTerseTables() {
    return ignoreTerseTables;
  }

  /**
   * Get the disable terse extraction flags.
   *
   * @return should disable table extraction
   */
  public boolean isDisableSimpleTableExtraction() {
    return disableSimpleTableExtraction;
  }

  /**
   * Get the disable grid table extraction flag.
   *
   * @return should disable table extraction
   */
  public boolean isDisableGridTableExtraction() {
    return disableGridTableExtraction;
  }

  /**
   * Flag for emitting the absolute positioning.
   *
   * @param emitAbsolutePositioning true to emit
   * @return this
   */
  public PdfParserConfig withEmitAbsolutePositioning(boolean emitAbsolutePositioning) {
    this.emitAbsolutePositioning = emitAbsolutePositioning;
    return this;
  }

  /**
   * When searching for baselines we test for line breaks. If the x separation of the text is too
   * far it is not considered to be a line break. The max separation distance is the average width
   * times this multiplier.
   *
   * @param maxBaselineSeparationMultiplier the multiplier of the average width
   * @return this
   */
  public PdfParserConfig withMaxBaselineSeparationMultiplier(
      float maxBaselineSeparationMultiplier) {
    this.maxBaselineSeparationMultiplier = maxBaselineSeparationMultiplier;
    return this;
  }

  /**
   * Set the x spacing for the X-Y cut grouping algorithm.
   *
   * @param groupXSpacing the x spacing
   * @return this
   */
  public PdfParserConfig withGroupXSpacing(int groupXSpacing) {
    this.groupXSpacing = groupXSpacing;
    return this;
  }

  /**
   * Set the y spacing for the X-Y cut grouping algorithm.
   *
   * @param groupYSpacing the y spacing
   * @return this
   */
  public PdfParserConfig withGroupYSpacing(int groupYSpacing) {
    this.groupYSpacing = groupYSpacing;
    return this;
  }

  /**
   * Set the X-Y cut grouping projection scale.
   *
   * @param xyCutProjectionScale the xy cut projection scale
   * @return this
   */
  public PdfParserConfig withXyCutProjectionScale(int xyCutProjectionScale) {
    this.xyCutProjectionScale = xyCutProjectionScale;
    return this;
  }

  /**
   * Set the tolerance for the character spacing as a multiple of the character width.
   *
   * @param spacingCharacterTolerance the tolerance
   * @return this
   */
  public PdfParserConfig withSpacingCharacterTolerance(double spacingCharacterTolerance) {
    this.spacingCharacterTolerance = spacingCharacterTolerance;
    return this;
  }

  /**
   * Set the tolerance for the group spacing.
   *
   * @param spacingGroupTolerance the tolerance
   * @return this
   */
  public PdfParserConfig withSpacingGroupTolerance(double spacingGroupTolerance) {
    this.spacingGroupTolerance = spacingGroupTolerance;
    return this;
  }

  /**
   * Set the tolerance for the break spacing.
   *
   * @param spacingBreakTolerance the tolerance
   * @return this
   */
  public PdfParserConfig withSpacingBreakTolerance(double spacingBreakTolerance) {
    this.spacingBreakTolerance = spacingBreakTolerance;
    return this;
  }

  /**
   * Set true to ignore overly terse tables (those with very dense amounts of text). This is not a
   * generic solution, but does prevent some multi-column documents being identified as tables.
   *
   * @param ignoreTerseTables flag
   * @return this
   */
  public PdfParserConfig withIgnoreTerseTables(boolean ignoreTerseTables) {
    this.ignoreTerseTables = ignoreTerseTables;
    return this;
  }

  /**
   * Set true to disable simple table extraction. (this means that lines will be grouped into text
   * blocks rather than tables).
   *
   * @param disableSimpleTableExtraction with true to disable table extraction
   * @return this
   */
  public PdfParserConfig withDisableSimpleTableExtraction(boolean disableSimpleTableExtraction) {
    this.disableSimpleTableExtraction = disableSimpleTableExtraction;
    return this;
  }

  /**
   * Set true to disable grid based table extraction.
   *
   * @param disableGridTableExtraction with true to disable table extraction
   * @return this
   */
  public PdfParserConfig withDisableGridTableExtraction(boolean disableGridTableExtraction) {
    this.disableGridTableExtraction = disableGridTableExtraction;
    return this;
  }
}
