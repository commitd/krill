package io.committed.krill.extraction.pdfbox.physical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class StyleTest {

  @Test
  public void testEqual() {
    Style style1 = new Style("style", 1, true, true, true, "color");
    Style style2 = new Style("sty" + "le", 1, true, true, true, "co" + "lor");
    Style style3 = new Style(null, 1, true, true, true, "color");
    Style style4 = new Style(null, 1, true, true, true, "color");
    Style style5 = new Style("style", 1, true, true, true, null);
    Style style6 = new Style("style", 1, true, true, true, null);
    assertEquals(style1, style2);
    assertEquals(style1, style1);
    assertEquals(style3, style4);
    assertEquals(style5, style6);
  }

  @Test
  public void testNotSameClass() {
    Style style1 = new Style("style1", 1, true, true, true, "color");
    assertNotEquals(style1, null);
    assertNotEquals(style1, "string");
  }

  @Test
  public void testNotEqualFont() {
    Style style1 = new Style("style1", 1, true, true, true, "color");
    Style style2 = new Style("style2", 1, true, true, true, "color");
    Style style3 = new Style(null, 1, true, true, true, "color");
    assertNotEquals(style1, style2);
    assertNotEquals(style1, style3);
    assertNotEquals(style3, style1);
  }

  @Test
  public void testNotEqualSize() {
    Style style1 = new Style("style", 1, true, true, true, "color");
    Style style2 = new Style("style", 2, true, true, true, "color");
    assertNotEquals(style1, style2);
  }

  @Test
  public void testNotEqualBold() {
    Style style1 = new Style("style", 1, true, true, true, "color");
    Style style2 = new Style("style", 1, false, true, true, "color");
    assertNotEquals(style1, style2);
  }

  @Test
  public void testNotEqualItalic() {
    Style style1 = new Style("style", 1, true, true, true, "color");
    Style style2 = new Style("style", 1, true, false, true, "color");
    assertNotEquals(style1, style2);
  }

  @Test
  public void testNotEqualUnderline() {
    Style style1 = new Style("style", 1, true, true, true, "color");
    Style style2 = new Style("style", 1, true, true, false, "color");
    assertNotEquals(style1, style2);
  }

  @Test
  public void testNotEqualColor() {
    Style style1 = new Style("style", 1, true, true, true, "color");
    Style style2 = new Style("style", 1, true, true, true, "monochrome");
    Style style3 = new Style("style", 1, true, true, true, null);
    assertNotEquals(style1, style2);
    assertNotEquals(style1, style3);
    assertNotEquals(style3, style1);
  }
}
