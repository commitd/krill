package io.committed.krill.extraction.pdfbox.physical;

/** Represents the calculated style for text in a PDF. */
public class Style {

  /** The name. */
  private final String name;

  /** The size. */
  private final float size;

  /** The italic. */
  private final boolean italic;

  /** The bold. */
  private final boolean bold;

  /** The color. */
  private final String color;

  /** The underlined. */
  private boolean underlined = false;

  /**
   * Creates a new style.
   *
   * @param name the font name
   * @param size the font size in points
   * @param bold whether the font is deemed bold
   * @param italic whether the font is deemed italic
   * @param underlined whether the font is deemed underlined
   * @param color the color of the text
   */
  public Style(
      String name, float size, boolean bold, boolean italic, boolean underlined, String color) {
    this.name = name;
    this.size = size;
    this.bold = bold;
    this.italic = italic;
    this.color = color;
    this.underlined = underlined;
  }

  /**
   * Copy constructor.
   *
   * @param style the style to copy
   */
  public Style(Style style) {
    this(
        style.getName(),
        style.getSize(),
        style.isBold(),
        style.isItalic(),
        style.isUnderlined(),
        style.getColor());
  }

  /**
   * Returns the font name - often this will be a PostScript font name, so may differ from the name
   * used in the authoring tool, eg Helvetica vs HelveticaMT.
   *
   * @return the font name.
   */
  public String getName() {
    return name;
  }

  /**
   * Return the font point size (1 unit = 1/72 inch).
   *
   * @return the size.
   */
  public float getSize() {
    return size;
  }

  /**
   * Whether the font is deemed bold. This might be because the weight is suitably heavy, the font
   * is declared as force bold, etc.
   *
   * @return if the font is bold.
   */
  public boolean isBold() {
    return bold;
  }

  /**
   * Whether the font is deemed italic. This might be because the font name contains the word
   * "italic", is declared italic, has a sheer applied, etc.
   *
   * @return if the font is italic.
   */
  public boolean isItalic() {
    return italic;
  }

  /**
   * Returns the colour used to render the text.
   *
   * @return the color.
   */
  public String getColor() {
    return color;
  }

  /**
   * Returns whether this should be considered an underlined style.
   *
   * <p>There is no provision for underlining in PDF files, so it is achieved through drawing lines
   * in the appropriate place - therefore this flag is determined by page analysis alone.
   *
   * @return true, if is underlined
   */
  public boolean isUnderlined() {
    return underlined;
  }

  /**
   * Sets the underlined.
   *
   * @param underlined the new underlined
   */
  public void setUnderlined(boolean underlined) {
    this.underlined = underlined;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (bold ? 1231 : 1237);
    result = prime * result + ((color == null) ? 0 : color.hashCode());
    result = prime * result + (italic ? 1231 : 1237);
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + Float.floatToIntBits(size);
    result = prime * result + (underlined ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Style other = (Style) obj;
    if (bold != other.bold) {
      return false;
    }
    if (color == null) {
      if (other.color != null) {
        return false;
      }
    } else if (!color.equals(other.color)) {
      return false;
    }
    if (italic != other.italic) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (Float.floatToIntBits(size) != Float.floatToIntBits(other.size)) {
      return false;
    }
    if (underlined != other.underlined) {
      return false;
    }
    return true;
  }
}
