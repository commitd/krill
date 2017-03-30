package io.committed.krill.extraction.tika.html5;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class Html5NbspTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "html5-nbsp.html";

  public Html5NbspTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    assertBody("" +
        "<main class=\"Document\"> \n" +
        " <article> \n" +
        "  <p>This document contains nbsp</p> \n" +
        "  <p> </p> \n" +
        "  <p> We dont' want those</p> \n" +
        " </article> \n" +
        "</main>");
  }

}
