package io.committed.krill.extraction.tika.html5;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class Html5InlineTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "html5-inline.html";

  public Html5InlineTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }



  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n" + " <section> \n"
        + "  <h2>Inline elements</h2> \n"
        + "  <p>Some elements, such as <span id=\"spans\">can be used to define elements within other elements</span>. Other inline tags include <a id=\"anchor\">anchors</a> which can also be used as <a href=\"#anchor\">links to content</a>, though it is also possible to link to elements by id (such as the <a href=\"#spans\">previous span</a>). Images, <a href=\"#images\">such as those at the end of the document</a>, are also defined using inline tags.</p> \n"
        + "  <p>It is also possible to logically mark up computer code inline using the code tag:</p> \n"
        + "  <blockquote>\n"
        + "    The main entry point into a C/C++ program is a function called main, which can be defined as either \n"
        + "   <code>int main(void)</code> or \n"
        + "   <code>int main(int argc, char* argv[])</code>. This can be logically rewritten as \n"
        + "   <code>int main(int argc, char** argv)</code>. Some platforms (POSIX, and hence Unix/Windows) add the environment \n"
        + "   <code>int main(int argc, char **argv, char **envp)</code> as a way to internalise \n"
        + "   <code>extern char **environ</code>. On Darwin/OSX add additional parameters are provided through the \n"
        + "   <code>apple</code> argument of the entry point \n"
        + "   <code>int main(int argc, char **argv, char **envp, char **apple)</code> - for example, \n"
        + "   <code>apple[0]</code>contains the path to the currently executing binary (not necessarily the same as the value of \n"
        + "   <code>argv[0]</code> since this can be set to any arbitrary value by \n"
        + "   <code>execve(2)</code>). \n" + "  </blockquote> \n" + " </section> \n" + "</main>");
  }



}
