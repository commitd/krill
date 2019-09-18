package io.committed.krill.extraction.tika.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public abstract class AbstractTikaSpreadsheetFormatExtractorIT
    extends AbstractTikaFormatExtractorIT {

  public AbstractTikaSpreadsheetFormatExtractorIT(final String resourceName) {
    super(resourceName);
  }

  public AbstractTikaSpreadsheetFormatExtractorIT(
      final String resourceName, final boolean generateTestCode) {
    super(resourceName, generateTestCode);
  }

  @Test
  public void noPreview() {
    assertThat(document.select("div[id=/docProps/thumbnail.jpeg]").isEmpty()).isTrue();
  }

  protected Element getSheetTable(final int sheet) {
    final Elements tables = getSheet(sheet).select("table");
    assertThat(tables.size()).isEqualTo(1);
    return tables.get(0);
  }

  protected void assertSpreadsheet(final int numSheets) {
    final Elements mains = document.select("body > main");
    assertThat(mains.size()).isEqualTo(1);
    assertThat(mains.attr("class")).isEqualTo("SpreadSheet");

    final Elements articles = document.select("body > main > article");
    assertThat(articles.size()).isEqualTo(numSheets);
    articles.forEach(e -> assertThat(e.attr("class")).isEqualTo("Sheet"));
  }

  protected void assertSheetTitle(final int sheet, final String title) {
    assertThat(getSheet(sheet).select("h1").text()).isEqualTo(title);
  }

  protected Element getSheet(final int sheet) {
    return document.select("body > main > article").get(sheet);
  }

  protected void assertCell(
      final int sheet, final int row, final int col, final String expectedValue) {
    final String text =
        getSheetTable(sheet).select("tbody > tr").get(row).select("td").get(col).text();
    assertThat(text).isEqualTo(expectedValue);
  }

  protected void assertNumberOfSheetRows(final int sheet, final int expectedRows) {
    assertThat(getSheetTable(sheet).select("tbody > tr").size()).isEqualTo(expectedRows);
  }
}
