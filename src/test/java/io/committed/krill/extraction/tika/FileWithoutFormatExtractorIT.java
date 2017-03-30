package io.committed.krill.extraction.tika;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.committed.krill.extraction.impl.DefaultExtraction;
import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class FileWithoutFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  // This image is licenced under Creative Commons, allowing Commercial, other usage
  // https://www.flickr.com/photos/52786697@N00/5159564109/in/photolist-8RW7DT-gf3vP7-cDgroC-cDgtWj-6CFbcH-8RZcBG-9UC5ox-6Lteuc-sZhYA-8RZcjC-5Nd3ZT-2eMEVx-7dgtso-8RW6e2-7NdmsR-4eZEJ-2ufRjc-nWkWrF-6nSkbb-bUX21Q-9hAVCz-kbNpc1-sZhG2-8iifQz-9W6mov-6CKj75-6AbS1K-oWk1hx-ohoxDv-6uzeYN-7CbSLZ-dvwJD-6CFaNg-6CKkko-6CKiMG-gyUbr8-2jQu3n-6CFa8K-EZDRG-6CFbqZ-ghj1Nr-d7a7jj-euFceJ-4pXLxo-mGeVU-Eofxg-51WSoT-6CDRZb-Eo8dG-9UC8zk
  // https://www.flickr.com/photos/52786697@N00/
  // It was downlaoded from Flickr, resized to 150x150
  private static final String RESOURCE_NAME = "whale.jpg";

  public FileWithoutFormatExtractorIT() {
    super(RESOURCE_NAME);
  }

  @Test
  public void testHasMetadata() {
    assertThat(((DefaultExtraction) extraction).getMetadata().isEmpty()).isFalse();
  }

}
