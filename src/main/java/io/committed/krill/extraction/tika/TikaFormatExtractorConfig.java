package io.committed.krill.extraction.tika;

import java.util.ArrayList;
import java.util.List;

import org.apache.tika.parser.Parser;

import io.committed.krill.extraction.tika.parsers.CsvParser;
import io.committed.krill.extraction.tika.parsers.JSoupHtmlParser;
import io.committed.krill.extraction.tika.parsers.RtfParser;
import io.committed.krill.extraction.tika.pdf.PdfParser;
import io.committed.krill.extraction.tika.pdf.PdfParserConfig;

/**
 * Configuration class for the {@link TikaFormatExtractor}.
 */
public class TikaFormatExtractorConfig {

  /** The parsers to use in the extractor */
  private final List<Parser> parsers;

  private TikaFormatExtractorConfig(List<Parser> parsers) {
    this.parsers = parsers;
  }

  /**
   * Get the parsers configured.
   *
   * @return the parsers
   */
  protected List<Parser> getParsers() {
    return parsers;
  }


  /**
   * @return create a builder for the {@link TikaFormatExtractor}
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for the {@link TikaFormatExtractor}.
   *
   */
  public static class Builder {

    private boolean csvParser = false;
    private boolean htmlParser = false;
    private boolean pdfParser = false;
    private boolean rtfParser = false;

    private PdfParserConfig pdfParserConfig = new PdfParserConfig();

    /**
     * Include all parsers.
     *
     * @return this
     */
    public Builder withDefaultParsers() {
      csvParser = true;
      htmlParser = true;
      pdfParser = true;
      rtfParser = true;
      return this;
    }

    /**
     * Include the CSV parser.
     *
     * @return this
     */
    public Builder withCsvParser() {
      csvParser = true;
      return this;
    }

    /**
     * Include the HTML parser.
     *
     * @return this
     */
    public Builder withHtmlParser() {
      htmlParser = true;
      return this;
    }

    /**
     * Include the PDF parser.
     *
     * @return this
     */
    public Builder withPdfParser() {
      pdfParser = true;
      return this;
    }

    /**
     * Include the PDF parser with the provided configuration.
     *
     * @param config the PDf parser configuration
     * @return this
     */
    public Builder withPdfParser(PdfParserConfig config) {
      pdfParserConfig = config;
      pdfParser = true;
      return this;
    }

    /**
     * Include the RTF parser.
     *
     * @return this
     */
    public Builder withRtfParser() {
      rtfParser = true;
      return this;
    }

    /**
     * Build the {@link TikaFormatExtractorConfig} as specified.
     *
     * @return the built {@link TikaFormatExtractorConfig}
     */
    public TikaFormatExtractorConfig build() {

      List<Parser> parsers = new ArrayList<>();
      if (csvParser) {
        parsers.add(new CsvParser());
      }
      if (htmlParser) {
        parsers.add(new JSoupHtmlParser());
      }
      if (pdfParser) {
        parsers.add(new PdfParser(pdfParserConfig));
      }
      if (rtfParser) {
        parsers.add(new RtfParser());
      }

      return new TikaFormatExtractorConfig(parsers);
    }
  }
}
