package io.committed.krill.extraction.tika.docx;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;
import org.junit.Test;

public class DocxAdhocFormattingTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "adhocformatting.docx";

  public DocxAdhocFormattingTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  // NOTE: subscript, superscript, colour here are not working!
  @Test
  /*
   * Will fail due to previous reliance on Apache Tika fork. This should pass in the next version of Tika
   */
  public void testBody() {
    assertBody(
        ""
            + "<main class=\"Document\"> \n"
            + " <h3><a name=\"_Toc465796966\"></a>Adhoc application of formatting</h3> \n"
            + " <p class=\"no_Spacing\">Rather than using styles, text can be altered inline to be <b>bold</b>, <i>italic</i>, <u>underlined</u>, or have <s>strikethrough</s>. It is possible to have subscripts and superscripts, and the colour can be changed. For some reason it is possible to apply <b>text effects</b>.</p> \n"
            + "</main>");
  }
}
