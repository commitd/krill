package io.committed.krill.extraction.tika.docx;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class DocxMetadataFieldsTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "metadatafields.docx";

  public DocxMetadataFieldsTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <h3>Metadata and fields</h3> \n"
            + " <p>Metadata fields, such as the document title () and date (Tuesday, 1 November 2016) can be automatically inserted and updated.</p> \n"
            + " <p></p> \n"
            + " <p>It would be normal to find a table of contents at the start of a document, but we can insert one anywhere, so one follows this sentence:</p> \n"
            + "</main>");
  }
}
