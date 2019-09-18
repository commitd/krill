package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.interpretation.BlockTypeLabel;
import io.committed.krill.extraction.pdfbox.interpretation.LabellablePositioned;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/** Represents a table found in a PDF. */
public class TableBlock extends PositionedContainer<TableRow> implements LabellablePositioned {

  /** The labels. */
  private EnumSet<BlockTypeLabel> labels = EnumSet.noneOf(BlockTypeLabel.class);

  /**
   * Creates a new {@link TableBlock} from the given {@link TableRow}s.
   *
   * @param rows the rows of the table.
   */
  public TableBlock(List<TableRow> rows) {
    super(rows);
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
