package io.committed.krill.extraction.pdfbox.interpretation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.committed.krill.extraction.pdfbox.interpretation.BlockTypeLabel;
import io.committed.krill.extraction.pdfbox.interpretation.LabellablePositioned;
import io.committed.krill.extraction.pdfbox.physical.ImageBlock;
import io.committed.krill.extraction.pdfbox.physical.TextBlock;
import io.committed.krill.extraction.pdfbox.text.TableBlock;

import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.EnumSet;

public class TestLabellables {

  @Test
  public void testTextBlock() {
    LabellablePositioned block = new TextBlock(Collections.emptyList());
    validate(block);
  }

  @Test
  public void testImageBlock() {
    LabellablePositioned block = new ImageBlock(new Rectangle2D.Double());
    validate(block);
  }

  @Test
  public void testTableBlock() {
    LabellablePositioned block = new TableBlock(Collections.emptyList());
    validate(block);
  }

  private void validate(LabellablePositioned block) {
    block.addLabel(BlockTypeLabel.UNKNOWN);
    assertEquals(block.getLabels(), EnumSet.of(BlockTypeLabel.UNKNOWN));

    block.addLabel(BlockTypeLabel.HEADER);
    block.addLabel(BlockTypeLabel.FOOTER);
    assertEquals(block.getLabels(), EnumSet.of(BlockTypeLabel.HEADER, BlockTypeLabel.FOOTER));
  }


}
