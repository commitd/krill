package org.apache.pdfbox.rendering;

import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Workaround for package-protected methods and classes in PDFBox renderer package.
 */
public class RendererPackageWorkaround {

  private PageDrawer pageDrawer;

  private Method method;

  /**
   * Creates a new workaround that contains a PageDrawer (which maintains a cache of font glyph
   * objects).
   * 
   * @throws RuntimeException
   *           if an error occurs during initialisation.
   */
  public RendererPackageWorkaround() throws RuntimeException {
    try {
      pageDrawer = new PageDrawer(new PageDrawerParameters(null, null, true));
      method = PageDrawer.class.getDeclaredMethod("createGlyph2D", PDFont.class);
    } catch (NoSuchMethodException | SecurityException | IOException e) {
      throw new RuntimeException(e);
    }
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      method.setAccessible(true);
      return null;
    });
  }

  /**
   * Returns an AWT path for the given font and code-point.
   * 
   * @param font
   *          the font
   * @param code
   *          the code-point
   * @return an AWT path for the code
   * @throws IOException
   *           if an error occurs obtaining the character
   */
  public GeneralPath getPathForCharacter(PDFont font, int code) throws IOException {
    try {
      Glyph2D invoke = (Glyph2D) method.invoke(pageDrawer, font);
      return invoke.getPathForCharacterCode(code);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new IOException("Failed to get path for character " + code, e);
    }
  }

}
